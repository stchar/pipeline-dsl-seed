# Jenkins Pipeline Seeder Project
This repository contains Pipeline and GroovyDslPlugin scripts to populate Jenkins
pipeline jobs

## Repository Structure
```
.
│
├── scm           # Scm Jenkins jobs
│   ├── dsl       # Contains jobs configuration files
│   └── pipeline  # Contains pipeline scripts of the job
│       └── jobs-seeder-pipeline.groovy # pipeline script which contains main
│                                       # configuration to call seeder script
│
└── dsl_seeder.groovy # Groovy script to seed jobs within provided configuration

```

## Contribution

###  Testing
Download & build job-dsl-plugin
```
git clone https://github.com/jenkinsci/job-dsl-plugin.git
cd job-dsl-plugin
./gradlew :job-dsl-core:oneJar
```

```
java -jar $DSL_JAR seed_dsl.groovy ['name':'scm-publisher-sandbox'),'dsl':'scm/dsl/scm-publisher.groovy','pipeline':'scm/pipeline/scm-publisher.groovy']

# this will produce config.xml file of the job
```

## Deployment
Wrap `dsl_seeder.groovy` as jenkins freestyle project with dsl plugin e.g. `dsl-seeder`

### SandBoxing
1. Run `dsl-seeder` with following arguments:
```
[
    'name':'scm-publisher-sandbox'),
    'dsl':'scm/dsl/scm-publisher.groovy',
    'pipeline':'scm/pipeline/scm-publisher.groovy',
]
```
2. Run `scm-publisher-sandbox` to populate other pipeline scripts

### Production
1. Run `dsl-seeder` with following arguments:
```
[
    name:'scm-publisher'),
    dsl:'scm/dsl/scm-publisher.groovy',
    pipeline:'scm/pipeline/scm-publisher.groovy',
]
2. Run `scm-publisher-sandbox` to populate other pipeline scripts


#### Additional Links
https://github.com/jenkinsci/job-dsl-plugin/wiki/User-Power-Moves#run-a-dsl-script-locally
https://jenkinsci.github.io/job-dsl-plugin/#path/pipelineJob-parameters
