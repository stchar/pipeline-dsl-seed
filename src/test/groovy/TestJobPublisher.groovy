import static com.lesfurets.jenkins.unit.global.lib.LibraryConfiguration.library
import static com.lesfurets.jenkins.unit.global.lib.LocalSource.localSource

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.lesfurets.jenkins.unit.BasePipelineTest

//import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString
import static org.junit.Assert.assertEquals

class TestJobPublisher extends BasePipelineTest {

  @Override
  @Before
  void setUp() throws Exception {
      scriptRoots += 'jobs'
      super.setUp()
      binding.setVariable("env",[JOB_NAME:"job-publisher"])
      helper.registerAllowedMethod("git", [Map.class], null)
      helper.registerAllowedMethod("writeFile", [Map.class], null)
      helper.registerAllowedMethod("jobDsl", [Map.class], null)
      helper.registerAllowedMethod("build", [Map.class], null)
      binding.setVariable('flavour',null)
      binding.setVariable('seed_ref',"master")
  }

  @Test
  void should_execute_without_errors() throws Exception {
    def script = loadScript("seed/pipeline/job-publisher.groovy")
    //printCallStack()
  }

  @Test
  void review_branch_flavour() throws Exception {
    binding.setVariable('seed_ref',"feature/branch")
    def script = loadScript("seed/pipeline/job-publisher.groovy")
    assertEquals("Verify seed_ref","feature/branch",binding.getVariable("seed_ref"))
    assertEquals("Verify flavour is sandbox","sandbox",binding.getVariable("flavour"))
    //printCallStack()
  }

}
