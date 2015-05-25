package org.assertj.forge.facet;

import org.jboss.forge.addon.projects.ProjectFacet;

/**
 * Created by Alexander Bischof on 22.05.15.
 */
public interface AssertJFacet extends ProjectFacet {
    void setAssertJVersion(String version);

    void setAssertJGeneratorVersion(String version);

    String getAssertJVersion();

    String getAssertJGeneratorVersion();

    void addPackages(Iterable<String> packages);

    void addClasses(Iterable<String> classes);

    void addExcludes(Iterable<String> excludes);

    void addIncludes(Iterable<String> includes);

    void setNotHierarchical(Boolean notHierarchical);

    void setEntryPointClassPackage(String entryPointClassPackage);

    void setTargetDirectory(String targetDirectory);

    void setNotGenerateAssertions(Boolean generateAssertions);

    void setNotGenerateBddAssertions(Boolean generateBddAssertions);

    void setNotGenerateSoftAssertions(Boolean generateSoftAssertions);

    void setNotGenerateJUnitSoftAssertions(Boolean generateJUnitSoftAssertions);
}
