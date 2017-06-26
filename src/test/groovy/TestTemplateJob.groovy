import org.junit.Before
import org.junit.Test

import com.lesfurets.jenkins.unit.BasePipelineTest

//import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString
//import static org.junit.Assert.assertTrue

class TestTemplateJob extends BasePipelineTest {

  @Override
  @Before
  void setUp() throws Exception {
      scriptRoots += 'jobs'
      super.setUp()
      def scmBranch = "feature_test"
      helper.registerAllowedMethod("sh", [Map.class], {c -> 'bcc19744'})
      binding.setVariable('scm', [
                      $class                           : 'GitSCM',
                      branches                         : [[name: scmBranch]]
      ])
  }

  @Test
  void should_execute_without_errors() throws Exception {
      def script = loadScript("jobs/template/pipeline/template.groovy")
      printCallStack()
  }
}
