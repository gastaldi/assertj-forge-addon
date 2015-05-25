package org.assertj.forge.ui;

import org.assertj.forge.facet.AssertJFacet;
import org.jboss.forge.addon.facets.FacetFactory;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.UIInputMany;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;

import javax.inject.Inject;
import java.util.concurrent.Callable;

import static java.util.Arrays.asList;

public class AssertJGeneratorSetupCommand extends AbstractProjectCommand {
    @Inject
    private AssertJFacet facet;

    @Inject
    private FacetFactory facetFactory;

    @Inject
    private ProjectFactory projectFactory;

    @Inject
    @WithAttributes(shortName = 'v', label = "AssertJ-Core version", type = InputType.DROPDOWN)
    private UISelectOne<String> version;

    @Inject
    @WithAttributes(shortName = 'p', label = "AssertJ-Generator packages")
    private UIInputMany<String> packages;

    @Inject
    @WithAttributes(shortName = 'c', label = "AssertJ-Generator classes")
    private UIInputMany<String> classes;

    @Inject
    @WithAttributes(shortName = 'e', label = "AssertJ-Generator excludes")
    private UIInputMany<String> excludes;

    @Inject
    @WithAttributes(shortName = 'f', label = "AssertJ-Generator classes")
    private UIInputMany<String> includes;

    @Inject
    @WithAttributes(shortName = 'h', label = "AssertJ-Generator not hierarchical")
    private UISelectOne<Boolean> notHierarchical;

    @Inject
    @WithAttributes(shortName = 'g', label = "AssertJ-Generator not generate Assertions")
    private UISelectOne<Boolean> notGenerateAssertions;

    @Inject
    @WithAttributes(shortName = 'b', label = "AssertJ-Generator not generate BddAssertions")
    private UISelectOne<Boolean> notGenerateBddAssertions;

    @Inject
    @WithAttributes(shortName = 's', label = "AssertJ-Generator not generate SoftAssertions")
    private UISelectOne<Boolean> notGenerateSoftAssertions;

    @Inject
    @WithAttributes(shortName = 'j', label = "AssertJ-Generator not generate JUnitSoftAssertions")
    private UISelectOne<Boolean> notGenerateJUnitSoftAssertions;

    @Inject
    @WithAttributes(shortName = 'E', label = "AssertJ-Generator entryPointClassPackage")
    private UISelectOne<String> entryPointClassPackage;

    @Inject
    @WithAttributes(shortName = 't', label = "AssertJ-Generator targetDir")
    private UISelectOne<String> targetDir;

    @Override
    public UICommandMetadata getMetadata(UIContext context) {
        return Metadata.forCommand(AssertJGeneratorSetupCommand.class).name(
            "assertj-assertions-generator-setup");
    }

    @Override
    public void initializeUI(UIBuilder builder) throws Exception {

        builder.add(version)
               .add(packages)
               .add(classes)
               .add(excludes)
               .add(includes)
               .add(notHierarchical)
               .add(notGenerateAssertions)
               .add(notGenerateBddAssertions)
               .add(notGenerateSoftAssertions)
               .add(notGenerateJUnitSoftAssertions)
               .add(entryPointClassPackage)
               .add(targetDir)
        ;

        //Version default and values
        version.setDefaultValue(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return facet.getAssertJVersion();
            }
        });
        version.setValueChoices(new Callable<Iterable<String>>() {
            @Override
            public Iterable<String> call() throws Exception {
                return asList("1.6.0", "1.7.1", "2.0.0");
            }
        });
    }

    @Override
    public Result execute(UIExecutionContext context) throws Exception {
        facet.setAssertJVersion(version.getValue());

        Iterable<String> packageIterable = this.packages.getValue();
        Iterable<String> classesIterable = this.classes.getValue();
        if (!packageIterable.iterator().hasNext() && !classesIterable.iterator().hasNext()) {
            return Results.fail("At least one package or class is required");
        }
        facet.addPackages(packageIterable);
        facet.addClasses(classesIterable);
        facet.addIncludes(includes.getValue());
        facet.addExcludes(excludes.getValue());

        facet.setNotHierarchical(notHierarchical.getValue());

        facet.setNotGenerateAssertions(notGenerateAssertions.getValue());
        facet.setNotGenerateBddAssertions(notGenerateBddAssertions.getValue());
        facet.setNotGenerateSoftAssertions(notGenerateSoftAssertions.getValue());
        facet.setNotGenerateJUnitSoftAssertions(notGenerateJUnitSoftAssertions.getValue());

        facet.setEntryPointClassPackage(entryPointClassPackage.getValue());

        facet.setTargetDirectory(targetDir.getValue());

        facetFactory.install(getSelectedProject(context), facet);

        return Results
            .success("Command 'assertj-assertions-generator-setup' successfully executed!");
    }

    @Override
    protected boolean isProjectRequired() {
        return true;
    }

    @Override
    protected ProjectFactory getProjectFactory() {
        return projectFactory;
    }
}