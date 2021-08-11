// Sandbox is for validation of MR or manual testting
flavor = 'sandbox'

try {
  seed_ref = seed_ref ? seed_ref : 'master'
} catch (e) {
  seed_ref = 'master'
}

if(seed_ref.equals('master')) {
  flavor = null
}

// Seed url couldbe replaced by you
def seed_url = "https://github.com/stchar/pipeline-dsl-seed.git"
def dep_url = "https://github.com/stchar/pipeline-dsl-seed-dep.git"

// dumb clone to populate gitlab plugin internals
node('master') {
  stage('prebuild') {
    dir('pipeline-dsl-seed-dep') {
       git(url:dep_url, branch: seed_ref)
    }
    dir('pipeline-dsl-seed'){
      git(url:seed_url, branch: seed_ref)
    }
  }

  dir('pipeline-dsl-seed'){
    stage('configure') {
      sh "./gradlew getJobs"
    }
    stage('test') {
      sh "./gradlew test"
    }
    stage('staging') {
      // Call seeder script to deploy jobs in the configuration
      jobDsl(targets: 'jobs/seeder.groovy',
          unstableOnDeprecation: true,
          failOnMissingPlugin: true)
    }
    test_suites = evaluate((String) readFile(".test_suites"))
    stage('e2e-tests') {
      for (suite in test_suites) {
        load suite
      }
    }
  }
}
