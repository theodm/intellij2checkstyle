# intellij2checkstyle [![pipeline status](https://gitlab.com/theodm94/intellij2checkstyle/badges/master/pipeline.svg)](https://theodm94.gitlab.io/intellij2checkstyle/tests/) [![coverage report](https://gitlab.com/theodm94/intellij2checkstyle/badges/master/coverage.svg)](https://theodm94.gitlab.io/intellij2checkstyle/coverage/)

Intellij2checkstyle converts inspection reports of the IntelliJ Idea IDE to the useful [checkstyle](http://checkstyle.sourceforge.net/) xml format. The project also contains tools to simplify the execution of the [IntelliJ Command line inspector](https://www.jetbrains.com/help/idea/running-inspections-offline.html).

The project currently consists of the following sub-projects:
- intellij2checkstyle-core: Shared Library for the dependent tools, which contains the basic functionality.
- intellij2checkstyle-cmd: Command line client to invoke the conversion or the command line inspector.

## Setup

### Prepare Project

#### Share Inspection Profile and Code Style

Make sure your inspection profiles, code style settings and scopes are stored as project settings. 

With default settings IntelliJ will apply new inspections with updated versions of IntelliJ. You might want to disable this behaviour with the setting *Disable new inspections by default*. 

Further information:
- [Customizing Profiles @ Jetbrains](https://www.jetbrains.com/help/idea/customizing-profiles.html)
- [Enabling & Disableing Inspections @ Jetbrains](https://www.jetbrains.com/help/idea/disabling-and-enabling-inspections.html)

#### Include configuration files in Source Control

Make sure the following files and folder are under source control:

- ./.idea/inspectionProfiles/**
- ./.idea/codeStyles/** <sup><sub>*</sub></sup>
- ./.idea/scopes/** <sup><sub>**</sub></sup>

<sup><sub>*</sub></sup> Only needs to be included if inspections which make use of the project-level code style settings are used.

<sup><sub>**</sub></sup> Only needs to be included if only a specific project-level scope will be used for the analysis.

The .gitignore file might look like this:

```
/.idea/*
!/.idea/codeStyles
!/.idea/scopes
!/.idea/inspectionProfiles
```

#### Gradle

Make sure a name for the root project was defined. 

The root project name can be defined in the settings.gradle file inside your project folder. If not defined, you can define it like this:

```
rootProject.name = "<your project name>"
```

Background:

IntelliJ will create internal module names based on the defined settings and if not found, will fallback to the project folder name. 
This folder name may be different on different environments. Consistent module names are needed for custom scopes, otherwise the analysis will be unreliable.

### Setup Continous Integration

#### Gitlab CI

Define a new job for the inspection analysis. Your docker image must satisfy the following conditions:

- JAVA_HOME environment variable must be set and pointing to an jdk installation
- JAVA_HOME/bin must be inside the PATH environment variable
- IDEA_HOME environment variable must be set and pointing to an installation of an supported version of IntelliJ IDEA

In the future probably a docker image supplied with all needed tools will be provided. For the moment you can use the following image, which is used for testing intellij2checkstyle: `registry.gitlab.com/theo.dm94_forks/intellij2checkstyle:intellij-ce-current`

A Job for the analysis might look like this:

```
lint:
  image: registry.gitlab.com/theodm94/intellij2checkstyle:intellij-ce-current
  script:
    - curl -L https://gitlab.com/theodm94/intellij2checkstyle/uploads/88d6b30bd40c07d8f49c23511a0ff324/intellij2checkstyle-cmd-1.0.0-RC1-all.jar -o intellij2checkstyle.jar
    - mkdir --parents ./reports/i2c/
    - java -jar intellij2checkstyle.jar inspect . --output-file ./reports/i2c/output.xml --scope MainScope
  artifacts:
    paths:
    - ./reports/i2c
    when: always
```

This script defines a job which downloads the command line clients and executes it on the project. The output will resides in ./reports/i2c and will be passed as artifact for consumption by dependent jobs.

## Contributing

Contribution Guide: [Click here](./CONTRIBUTING.md).

## Latest Results (master)

- [Coverage report](https://theodm94.gitlab.io/intellij2checkstyle/coverage/) <sup><sub>*</sub></sup>
- [Test report](https://theodm94.gitlab.io/intellij2checkstyle/tests/) <sup><sub>*</sub></sup>

<sup><sub>*</sub></sup> Currently only the results of unit tests will be reported here. For 
further information look into the build and test artifacts. Coverage is reported too low because 
of Issues with JaCoCo, which currently doesn't report kotlin inline functions and generated 
methods. 

## Alternatives

- [idea-cli-inspector @ Github](https://github.com/bentolor/idea-cli-inspector)
