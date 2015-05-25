JBoss Forge AssertJ Assertion Generator Addon
============
This is the JBoss Forge AssertJ Assertion Generator Addon.

Installation
============
The AssertJ Assertion Generator addon is listed in the Forge plugin repository so installation is trivial.
In Forge console, type:

	forge -i org.assertj.forge:assertj-forge-addon,1.0.0

Within Forge shell:

	addon-install --coordinate  org.assertj.forge:assertj-forge-addon,1.0.0

That's it! The addon will be downloaded and installed.

Setting up AssertJ Assertion Generator
==============
To configure the necessary assertj library and the assertj-generator-plugin of your project, use the following command:

	assertj-assertions-generator-setup
	
Note: At least one package or class is required.

Parameter description
===============
    -E, --entryPointClassPackage
        AssertJ-Generator entryPointClassPackage -  [String] Valid choices: [] 
 
    -b, --notGenerateBddAssertions
        AssertJ-Generator not generate BddAssertions -  [Boolean] Valid choices: ["true" "false" ] 
 
    -c, --classes
        AssertJ-Generator classes -  [String] 
 
    -e, --excludes
        AssertJ-Generator excludes -  [String] 
 
    -f, --includes
        AssertJ-Generator classes -  [String] 
 
    -g, --notGenerateAssertions
        AssertJ-Generator not generate Assertions -  [Boolean] Valid choices: ["true" "false" ] 
 
    -h, --notHierarchical
        AssertJ-Generator not hierarchical -  [Boolean] Valid choices: ["true" "false" ] 
 
    -j, --notGenerateJUnitSoftAssertions
        AssertJ-Generator not generate JUnitSoftAssertions -  [Boolean] Valid choices: ["true" "false" ] 
 
    -p, --packages
        AssertJ-Generator packages -  [String] 
 
    -s, --notGenerateSoftAssertions
        AssertJ-Generator not generate SoftAssertions -  [Boolean] Valid choices: ["true" "false" ] 
 
    -t, --targetDir
        AssertJ-Generator targetDir -  [String] Valid choices: [] 
 
    -v, --version
        AssertJ-Core version -  [String] Valid choices: ["1.6.0" "1.7.1" "2.0.0" ] 
