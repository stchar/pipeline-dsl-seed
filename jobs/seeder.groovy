import jenkins.model.Jenkins


class Seeder {
  /**
  * Creates pipeline job
  * @param {dslFactory} dslFactory
  * @param {Map} jobs  - job configuration list
  *             [
  *                 [//job1
  *                     name:{String}    Name of the generated job
  *                     pipeline:{String} Path of the -pipeline.goovy script
  *                     dsl:{String} Path of the -dsl.goovy script
  *                 ],
  *                 [//job2
  *                     name:{String}    Overrides generated name of the job
  *                     pipeline:{String} Overrides path of the pipeline.goovy script
  *                     dsl:{String} Overrides path of the pipeline.goovy script
  *                 ]
  *             ]
  */
  static pipeline(dslFactory, jobs=[], seed_ref) {
    if (!jobs) {
      throw NullPointerException('job list should be not bull' )
    }
    for (def job in jobs) {
        def _dslFactory = null

        if (!job.name) {
            throw DataFormatException('Incorrect structure of jobs data set' )
        }

        _dslFactory = dslFactory.pipelineJob(job.name) {
            //authenticationToken(job.token)
            logRotator(30, 30, 30, 30)
            quietPeriod(10)
            concurrentBuild(true)
            parameters{
                //SCM axilarry scripts
                //TODO Add descritptions
                stringParam('seed_ref', seed_ref, 'Version of the jenkins-dsl-seed repository')
            }
        }

        if(job.pipeline) {
            println("processing pipeline file = " + job.pipeline)
            _dslFactory.with {
                definition {
                    cps {
                        script(dslFactory.readFileFromWorkspace(job.pipeline))
                        sandbox(false)
                    }
                }
            }
        }

        if(job.dsl) {
            println("processing dsl file = " + job.dsl)
            GroovyShell shell = new GroovyShell()
            def script = shell.parse(dslFactory.readFileFromWorkspace(job.dsl))
            script.pipeline(_dslFactory,seed_ref,job)
        }

        // Auto-approve groovy see https://issues.jenkins-ci.org/browse/JENKINS-31201
        def scriptApproval = Jenkins.instance
            .getExtensionList('org.jenkinsci.plugins.scriptsecurity.scripts.ScriptApproval')[0]

        scriptApproval.approveScript(scriptApproval.hash(
            dslFactory.readFileFromWorkspace(job.pipeline), 'groovy'))


    } // for job in jobs
  }//static pipeline
}//class Seeder

def seed_ref = null
def jobs = null

try {
  seed_ref = seed_ref ? seed_ref : readFileFromWorkspace('.seed_ref')
} catch (Exception err) {
  seed_ref = readFileFromWorkspace('.seed_ref')
}

try {
  jobs = jobs ? jobs : readFileFromWorkspace('.jobs')
} catch (Exception err) {
  jobs = readFileFromWorkspace('.jobs')
}

Seeder.pipeline(this,evaluate(jobs),seed_ref)
