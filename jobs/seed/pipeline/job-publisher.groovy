// Sandbox is for validation of MR or manual testting
flavor = 'sandbox'

try {
  seed_ref = seed_ref ? seed_ref : 'master'
} catch (e) {
  seed_ref = 'master'
}


// Seed url couldbe replaced by you
def seed_url = "https://github.com/headcrabmeat/pipeline-dsl-seed.git"

// dumb clone to populate gitlab plugin internals
node('master') {
  stage('prebuild') {
    git(url:seed_url, branch: seed_ref)
    if(seed_ref.equals('master')) {
        flavor = null
    }
    git(url:seed_url, branch: seed_ref)
    config = load 'jobs/config.groovy'
  }

  stage('unit-tests') {
    sh "./gradlew test"
  }

  stage('staging') {
    writeFile(file:'.jobs',text:config.jobs.inspect())
    writeFile(file:'.seed_ref',text:seed_ref)

    // Call seeder script to deploy jobs in the configuration
    jobDsl(targets: 'jobs/seeder.groovy',
      unstableOnDeprecation: true,
      failOnMissingPlugin: true)
  }

  stage('integration-tests') {
    for (suite in test_suites) {
      load suite
    }
  }
}
