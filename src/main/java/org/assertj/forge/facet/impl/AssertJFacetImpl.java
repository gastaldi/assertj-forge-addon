package org.assertj.forge.facet.impl;

import org.apache.maven.model.Model;
import org.assertj.forge.facet.AssertJFacet;
import org.jboss.forge.addon.dependencies.Coordinate;
import org.jboss.forge.addon.dependencies.builder.CoordinateBuilder;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.facets.AbstractFacet;
import org.jboss.forge.addon.maven.plugins.ConfigurationBuilder;
import org.jboss.forge.addon.maven.plugins.ConfigurationElementBuilder;
import org.jboss.forge.addon.maven.plugins.ExecutionBuilder;
import org.jboss.forge.addon.maven.plugins.MavenPluginBuilder;
import org.jboss.forge.addon.maven.projects.MavenFacet;
import org.jboss.forge.addon.maven.projects.MavenPluginFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.DependencyFacet;

import java.util.Properties;

import static org.jboss.forge.addon.maven.plugins.ConfigurationBuilder.create;

/**
 * Created by Alexander Bischof on 22.05.15.
 */
public class AssertJFacetImpl extends AbstractFacet<Project> implements AssertJFacet {

    private static final String ASSERTJ_VERSION_PROPERTY = "version.assertj";
    private static final String ASSERTJ_GENERATOR_VERSION_PROPERTY = "version.assertj-generator";

    private String assertJVersion = "2.0.0";
    private String assertJGeneratorVersion = "1.6.0";
    private Iterable<String> packages;
    private Iterable<String> classes;
    private Iterable<String> excludes;
    private Iterable<String> includes;
    private Boolean notHierarchical;
    private String entryPointClassPackage;
    private String targetDirectory;
    private Boolean notGenerateAssertions;
    private Boolean notGenerateBddAssertions;
    private Boolean notGenerateSoftAssertions;
    private Boolean notGenerateJUnitSoftAssertions;

    private static final Coordinate PLUGIN_COORDINATE = CoordinateBuilder
        .create().setGroupId("org.assertj")
        .setArtifactId("assertj-assertions-generator-maven-plugin")
        .setVersion("${" + ASSERTJ_GENERATOR_VERSION_PROPERTY + "}");

    @Override
    public boolean install() {
        addAssertJVersionProperty();
        addMavenPlugin();
        addDependencies();
        return isInstalled();
    }

    private void addDependencies() {
        DependencyBuilder assertjDependency = DependencyBuilder.create()
                                                               .setGroupId("org.assertj")
                                                               .setArtifactId("assertj-core")
                                                               .setVersion("${" + ASSERTJ_VERSION_PROPERTY + "}")
                                                               .setScopeType("test");
        DependencyFacet facet = getFaceted().getFacet(DependencyFacet.class);
        facet.addDirectDependency(assertjDependency);

    }

    private void addMavenPlugin() {
        MavenPluginFacet pluginFacet = getFaceted().getFacet(
            MavenPluginFacet.class);

        MavenPluginBuilder plugin = MavenPluginBuilder
            .create()
            .setCoordinate(PLUGIN_COORDINATE)
            .addExecution(
                ExecutionBuilder.create()
                                .addGoal("generate-assertions"));

        //Configuration
        ConfigurationBuilder configurationBuilder = create();
        plugin.setConfiguration(configurationBuilder);

        //Packages
        addPackagesToConfiguration(configurationBuilder);

        //Classes
        addClassesToConfiguration(configurationBuilder);

        //Includes
        addIncludesToConfiguration(configurationBuilder);

        //Excludes
        addExcludesToConfiguration(configurationBuilder);

        //TargetDirectory
        addSingleValueToConfiguration(configurationBuilder, targetDirectory, "targetDirectory");

        //EntryPointClassPackage
        addSingleValueToConfiguration(configurationBuilder, entryPointClassPackage, "entryPointClassPackage");

        //Hierarchical
        if (notHierarchical != null) {
            addSingleValueToConfiguration(configurationBuilder, false, "notHierarchical");
        }

        //GenerateAssertions
        if (notGenerateAssertions != null) {
            addSingleValueToConfiguration(configurationBuilder, false, "notGenerateAssertions");
        }

        //GenerateBddAssertions
        if (notGenerateBddAssertions != null) {
            addSingleValueToConfiguration(configurationBuilder, false, "notGenerateBddAssertions");
        }

        //GenerateSoftAssertions
        if (notGenerateSoftAssertions != null) {
            addSingleValueToConfiguration(configurationBuilder, false, "notGenerateSoftAssertions");
        }

        //GenerateJUnitSoftAssertions
        if (notGenerateJUnitSoftAssertions != null) {
            addSingleValueToConfiguration(configurationBuilder, false, "notGenerateJUnitSoftAssertions");
        }

        pluginFacet.addPlugin(plugin);
    }

    private void addSingleValueToConfiguration(ConfigurationBuilder configurationBuilder, Object value,
                                               String configurationElementName) {
        if (value != null) {
            configurationBuilder.addConfigurationElement(
                ConfigurationElementBuilder
                    .create().createConfigurationElement(configurationElementName).setText(value.toString()));
        }
    }

    private void addPackagesToConfiguration(ConfigurationBuilder configurationBuilder) {
        addIterableToConfiguration(configurationBuilder, packages, "packages");
    }

    private void addClassesToConfiguration(ConfigurationBuilder configurationBuilder) {
        addIterableToConfiguration(configurationBuilder, classes, "classes");
    }

    private void addExcludesToConfiguration(ConfigurationBuilder configurationBuilder) {
        addIterableToConfiguration(configurationBuilder, excludes, "excludes");
    }

    private void addIncludesToConfiguration(ConfigurationBuilder configurationBuilder) {
        addIterableToConfiguration(configurationBuilder, includes, "includes");
    }

    private void addIterableToConfiguration(ConfigurationBuilder configurationBuilder, Iterable<String> iterable,
                                            String configurationElementName) {
        if (iterable != null && iterable.iterator().hasNext()) {
            ConfigurationElementBuilder classesConfiguration =
                ConfigurationElementBuilder.create().createConfigurationElement(configurationElementName);
            configurationBuilder.addConfigurationElement(classesConfiguration);

            for (String aClass : iterable) {
                classesConfiguration.addChild("param").setText(aClass);
            }
        }
    }

    private void addAssertJVersionProperty() {
        MavenFacet maven = getFaceted().getFacet(MavenFacet.class);
        Model pom = maven.getModel();
        Properties properties = pom.getProperties();
        properties.setProperty(ASSERTJ_VERSION_PROPERTY, assertJVersion);
        properties.setProperty(ASSERTJ_GENERATOR_VERSION_PROPERTY, assertJGeneratorVersion);
        maven.setModel(pom);
    }

    @Override
    public boolean isInstalled() {
        MavenPluginFacet facet = getFaceted().getFacet(MavenPluginFacet.class);
        return facet.hasPlugin(PLUGIN_COORDINATE);
    }

    @Override
    public void setAssertJVersion(String version) {
        this.assertJVersion = version;
    }

    @Override
    public void setAssertJGeneratorVersion(String version) {
        this.assertJGeneratorVersion = version;
    }

    @Override
    public String getAssertJVersion() {
        return assertJVersion;
    }

    @Override
    public String getAssertJGeneratorVersion() {
        return assertJGeneratorVersion;
    }

    @Override
    public void addPackages(Iterable<String> packages) {
        this.packages = packages;
    }

    @Override
    public void addClasses(Iterable<String> classes) {
        this.classes = classes;
    }

    @Override
    public void addExcludes(Iterable<String> excludes) {
        this.excludes = excludes;
    }

    @Override
    public void addIncludes(Iterable<String> includes) {
        this.includes = includes;
    }

    @Override
    public void setNotHierarchical(Boolean notHierarchical) {
        this.notHierarchical = notHierarchical;
    }

    @Override
    public void setEntryPointClassPackage(String entryPointClassPackage) {
        this.entryPointClassPackage = entryPointClassPackage;
    }

    @Override
    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    @Override
    public void setNotGenerateAssertions(Boolean notGenerateAssertions) {
        this.notGenerateAssertions = notGenerateAssertions;
    }

    @Override
    public void setNotGenerateBddAssertions(Boolean notGenerateBddAssertions) {
        this.notGenerateBddAssertions = notGenerateBddAssertions;
    }

    @Override
    public void setNotGenerateSoftAssertions(Boolean notGenerateSoftAssertions) {
        this.notGenerateSoftAssertions = notGenerateSoftAssertions;
    }

    @Override
    public void setNotGenerateJUnitSoftAssertions(Boolean notGenerateJUnitSoftAssertions) {
        this.notGenerateJUnitSoftAssertions = notGenerateJUnitSoftAssertions;
    }
}
