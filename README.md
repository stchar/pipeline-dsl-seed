# Jenkins Pipeline Seeder Project
This repository contains Pipeline and GroovyDslPlugin scripts to test and deploy Jenkins
pipeline jobs right form the jenkins

## Repository Structure
```
.
├── jobs
│   │
│   └── seeder.groovy     # Groovy script to seed jobs with provided configuration
│
└── test
    ├── integration
    │   └── groovy
    │       └── JobSriptsSpec.groovy   # Reads the configuration run deployment in a test environment
    └── unit
        └── groovy
            └── TestJenkinsfile.groovy # Example of junit tests written using JenkinsPipelineUnit
                                        # to test job-publisher
```

## Contribution

###  Testing
```
# Download dependency project
git clone https://github.com/stchar/pipeline-dsl-seed-dep ../pipeline-dsl-seed-dep

# Process config.groovy files to
# Get list of jobs to deploy
./gradlew getJobs

# Run tests
./gradlew check
```

## Deployment
See https://github.com/stchar/pipeline-dsl-seed-dep docs for details


#### Additional Links
* https://github.com/jenkinsci/job-dsl-plugin/wiki/User-Power-Moves#run-a-dsl-script-locally
* https://jenkinsci.github.io/job-dsl-plugin/#path/pipelineJob-parameters
* https://www.cloudbees.com/blog/top-10-best-practices-jenkins-pipeline-plugin
* https://github.com/sheehan/job-dsl-gradle-example
* https://github.com/lesfurets/JenkinsPipelineUnit
* https://github.com/mkobit/jenkins-pipeline-shared-libraries-gradle-plugin
