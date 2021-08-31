import jenkins.model.Jenkins


class Seeder {
  /**
  * Creates pipeline job
  * @param {dslFactory} dslFactory
  * @param {Map} jobs  - job configuration list
  *             [
  *                 [//job1
  *                     name:{String}    Name of the generated job
  *                     pipeline:{ArrayList<String>|String} Path of the -pipeline.groovy script(s)
  *                     dsl:{ArrayList<String>|String} Path of the -dsl.groovy script(s)
  *                 ],
  *                 [//job2
  *                     name:{String}
  *                     pipeline:
  *                     dsl:
  *                 ]
  *             ]
  * @note: job object is passed to dsl factory itself so you can extend it
  *        with own fileds you'd like to use in pipeline-specific dsl
  */
  static pipeline(dslFactory, jobs=[], seed_ref) {

    if (!jobs) {
      throw new NullPointerException('job list should be not null')
    }

    for (def job in jobs) {
      def _dslFactory = null

      if (!job.name) {
          throw new IllegalArgumentException('Missed job name')
      }

      _dslFactory = dslFactory.pipelineJob(job.name) {
          //authenticationToken(job.token)
          logRotator(30, 30, 30, 30)
          quietPeriod(10)
      }

      def pipeline_script = ""

      if (job.gitLabConnection) {
        // pipeline_script += "// seeder.groovy,51: DEBUG: found gitLabConnection = ${job.gitLabConnection}\n"
        // println("DEBUG: found gitLabConnection = ${job.gitLabConnection}")
        def connection = job.gitLabConnection
        _dslFactory.with {
          configure {
            it / 'properties' / 'com.dabsquared.gitlabjenkins.connection.GitLabConnectionProperty' {
                'gitLabConnection'(connection)
            }
          }
        } // dslFactory
      }


      if(job.pipeline) {
        if (job.pipeline instanceof String) {
          job.pipeline=[job.pipeline]
        }
        for (file in job.pipeline) {
          println("processing pipeline file = " + file)
          def inputStream = dslFactory.streamFileFromWorkspace(file)
          String text = new java.io.BufferedReader(
          new java.io.InputStreamReader(inputStream))
            .lines()
            .collect(java.util.stream.Collectors.joining("\n"))
            .replaceAll(/^(package\s+.*)/,'// $1')
            .replaceAll(/(import\s+.*)/,'// $1')
          pipeline_script += "////// Content of ${file} BEGIN //////\n"
          pipeline_script += text
          pipeline_script += "\n////// Content of ${file} END //////"
        }
        _dslFactory.with {
          definition {
            cps {
              script(pipeline_script)
              sandbox()
            }
          }
        }
        pipeline_script = null
      }

      if(job.dsl) {
        GroovyShell shell = new GroovyShell()
        if (job.dsl instanceof String) {
          job.dsl=[job.dsl]
        }
        for (dsl_file in job.dsl) {
          println("processing dsl file = " + dsl_file)
          def script = shell.parse(dslFactory.readFileFromWorkspace(dsl_file))
          script.pipeline(_dslFactory,seed_ref,job)
        }
      }

    } // for job in jobs
  }//static pipeline
}//class Seeder

// MAIN

def seed_ref = null
def jobs = null

try {
  seed_ref = seed_ref ? seed_ref : readFileFromWorkspace('.seed_ref')
} catch (err) {
  seed_ref = "master"
}

try {
  jobs = jobs ? jobs : readFileFromWorkspace('.jobs.json')
} catch (err) {
  jobs = readFileFromWorkspace('.jobs.json')
}

jobs = new groovy.json.JsonSlurper().parseText(jobs)
println "Seeder: found jobs = $jobs"

Seeder.pipeline(this,jobs,seed_ref)
