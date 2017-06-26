# Jenkins Pipeline Seeder Project
This repository contains Pipeline and GroovyDslPlugin scripts to test and deploy Jenkins
pipeline jobs right form the jenkins

## Repository Structure
```
.
├── jobs
│   ├── config.groovy     # contains a configuration of jobs to deploy
│   │
│   ├── seeder.groovy     # Groovy script to seed jobs with provided configuration
│   │
│   ├── seed                       # A project to test and publish
│   │   │                          # your Jenkins jobs
│   │   │
│   │   ├── dsl
│   │   │   └── job-publisher.groovy  # default input arguments
│   │   │                             # configuration to call seeder script
|   │   │                             # and is used to sand-boxing the projects
│   │   └── pipeline
│   │       └── job-publisher.groovy  # Contains pipeline script to read the
|   │                                 # configuration, and to call seeder script
│   │
│   └── template                      # project with examples of scripts
│       ├── dsl
│       │   └── template.groovy
│       └── pipeline
│           └── template.groovy
└── src
    └── test
        └── groovy
            ├── JobSriptsSpec.groovy    # Reads the configuration run deployment in a test environment
            └── TestTemplateJob.groovy  # Example of junit tests written using JenkinsPipelineUnit
                                        # to test template job
```

## Contribution

###  Testing
```
./gradlew test

# Runing gradle behind a proxy
./gradlew -Dhttps.proxyHost=127.0.0.1 -Dhttps.proxyPort=3128 test
```

## Deployment
Wrap `seed/pipeline/job-publisher.groovy` as jenkins pipeline project and run it

### SandBoxing
`job-publisher` handle git branch except master as a sandbox one.

#### Additional Links
* https://github.com/jenkinsci/job-dsl-plugin/wiki/User-Power-Moves#run-a-dsl-script-locally
* https://jenkinsci.github.io/job-dsl-plugin/#path/pipelineJob-parameters
* https://www.cloudbees.com/blog/top-10-best-practices-jenkins-pipeline-plugin
* https://github.com/sheehan/job-dsl-gradle-example
* https://github.com/lesfurets/JenkinsPipelineUnit
