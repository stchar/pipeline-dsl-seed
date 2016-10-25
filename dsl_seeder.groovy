import jenkins.model.Jenkins


class Seeder {
  /**
  * Creates pipeline job
  * @param {dslFactory} dslFactory
  * @param {Map} jobs  - job configuration list
                [
                    [//job1
  *                     name:{String}    Name of the generated job
  *                     pipelineScript:{String} Path of the -pipeline.goovy script
  *                     dslScript:{String} Path of the -dsl.goovy script
  *                 ],
  *                 [//job2
  *                     name:{String}    Overrides generated name of the job
  *                     pipelineScript:{String} Overrides path of the pipeline.goovy script
  *                     dslScript:{String} Overrides path of the pipeline.goovy script
  *                 ]
                ]

  */
  static pipeline(dslFactory, jobs=[:]) {
    for job in jobs {
        _dslFactory = null
        if (!job.name) {
            throw DataFormatException('Structure of projects data set' )
        }

        _dslFactory = dslFactory.pipelineJob(job.name) {
            //authenticationToken(job.token)
            logRotator(30, 30, 30, 30)
            quietPeriod(180)
            concurrentBuild(true)
            parameters{
                //SCM axilarry scripts
                stringParam('tools_ref', tools_ref, 'SCM pipepine & dsl scripts')
            }

        }

        if(job.pipelineScript) {
            _dslFactory.with {
                definition {
                    cps {
                        script(dslFactory.readFileFromWorkspace(job.pipelineScript))
                        sandbox(false)
                    }
                }
            }
        }

        if(job.dslScript) {
            GroovyShell shell = new GroovyShell()
            def script = shell.parse(dslFactory.readFileFromWorkspace(job.dslScript))
            script.pipeline(_dslFactory)
        }

        // Auto-approve groovy see https://issues.jenkins-ci.org/browse/JENKINS-31201
        def scriptApproval = Jenkins.instance
            .getExtensionList('org.jenkinsci.plugins.scriptsecurity.scripts.ScriptApproval')[0]

        scriptApproval.approveScript(scriptApproval.hash(
            dslFactory.readFileFromWorkspace(job.pipelineScript), 'groovy'))


    } // for job in jobs
  }//static pipeline
}//class Seeder


projects_map = evaluate(jobs_map)
println("jobs_map =" + jobs_map)
Seeder.pipeline(this, jobs_map)
