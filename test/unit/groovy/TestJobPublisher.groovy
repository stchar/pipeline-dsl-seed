import static com.lesfurets.jenkins.unit.global.lib.LibraryConfiguration.library
import static com.lesfurets.jenkins.unit.global.lib.LocalSource.localSource

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.lesfurets.jenkins.unit.declarative.DeclarativePipelineTest

//import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString
import static org.junit.Assert.assertEquals

class TestJobPublisher extends DeclarativePipelineTest {

  @Override
  @Before
  void setUp() throws Exception {
      scriptRoots += 'jobs'
      super.setUp()
      binding.setVariable("env",[JOB_NAME:"job-publisher"])
      helper.registerAllowedMethod("git", [Map])
      helper.registerAllowedMethod("writeFile", [Map])
      helper.registerAllowedMethod("readFile", [String], { name ->
        switch(name) {
          case ".jobs":
          //case ".test_suites":
            println("calling readFile with args: ${name}")
            return new File(name).text
          default:
            return ""
        }
      })
      helper.registerAllowedMethod("jobDsl", [Map], null)
      helper.registerAllowedMethod("build", [Map], null)
      binding.setVariable('flavor',null)
      binding.setVariable('seed_ref',"master")
  }

  @Test
  void should_execute_without_errors() throws Exception {
    def script = runScript("Jenkinsfile")
    printCallStack()
  }

  @Test
  void review_branch_flavor() throws Exception {
    binding.setVariable('params',[seed_ref: "feature/branch"])
    def script = runScript("Jenkinsfile")
    printCallStack()
  }

}
