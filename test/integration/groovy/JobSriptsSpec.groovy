import groovy.io.FileType
import hudson.model.Item
import hudson.model.View
import org.jenkinsci.plugins.workflow.libs.GlobalLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.LibraryRetriever
import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.dsl.GeneratedItems
import javaposse.jobdsl.dsl.GeneratedJob
import javaposse.jobdsl.dsl.GeneratedView
import javaposse.jobdsl.dsl.JobManagement
import javaposse.jobdsl.plugin.JenkinsJobManagement
import jenkins.model.Jenkins
import org.junit.ClassRule
import org.jvnet.hudson.test.JenkinsRule
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import com.mkobit.jenkins.pipelines.codegen.LocalLibraryRetriever
import groovy.json.JsonSlurper

/**
 * Tests that all dsl scripts in the jobs directory will compile.
 * All config.xml's are written to build/debug-xml.
 */
class JobScriptsSpec extends Specification {

    @Shared
    @ClassRule
    public JenkinsRule jenkinsRule = new JenkinsRule()

    @Shared
    public File outputDir = new File('build/debug-xml')

    def setupSpec() {
        outputDir.deleteDir()
    }

    @Unroll
    def 'Test job #job.name'(LinkedHashMap job) {
        given:
        JobManagement jm = new JenkinsJobManagement(System.out, [:], new File('.'))

        when:
        def seeder = new File ('jobs/seeder.groovy')
        GeneratedItems items = new DslScriptLoader(jm).runScript(seeder.text)
        writeItems items

        then:
        noExceptionThrown()

        where:
        job << jobs
    }


    /**
     * Write the config.xml for each generated job and view to the build dir.
     */
    def writeItems(GeneratedItems items) {
        Jenkins jenkins = jenkinsRule.jenkins

        items.jobs.each { GeneratedJob generatedJob ->
            String jobName = generatedJob.jobName
            Item item = jenkins.getItemByFullName(jobName)
            String text = new URL(jenkins.rootUrl + item.url + 'config.xml').text
            writeFile new File(outputDir, 'jobs'), jobName, text
        }

        items.views.each { GeneratedView generatedView ->
            String viewName = generatedView.name
            View view = jenkins.getView(viewName)
            String text = new URL(jenkins.rootUrl + view.url + 'config.xml').text
            writeFile new File(outputDir, 'views'), viewName, text
        }
    }

    /**
     * Write a single XML file, creating any nested dirs.
     */
    def writeFile(File dir, String name, String xml) {
        List tokens = name.split('/')
        File folderDir = tokens[0..<-1].inject(dir) { File tokenDir, String token ->
            new File(tokenDir, token)
        }
        folderDir.mkdirs()

        File xmlFile = new File(folderDir, "${tokens[-1]}.xml")
        xmlFile.text = xml
    }

    def List getJobs() {
      def jobs = new JsonSlurper().parseText(new File(".jobs.json").text)
      println "jobs = $jobs"
      return jobs
    }
}
