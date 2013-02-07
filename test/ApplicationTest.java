import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.net.InetAddresses;
import org.codehaus.jackson.JsonNode;
import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import restrict.RestrictToHostGroupAction;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {

    @Test
    public void testAddresses() {
        assertThat(InetAddresses.isInetAddress("0:0:0:0:0:0:0:1")).isTrue();
    }

    @Test
    public void trimIPv6() {
        assertThat((new RestrictToHostGroupAction()).trimRemoteAddress("0:0:0:0:0:0:0:1")).isEqualTo("0:0:0:0:0:0:0:1");
        assertThat((new RestrictToHostGroupAction()).trimRemoteAddress("0:0:0:0:0:0:0:1%1")).isEqualTo("0:0:0:0:0:0:0:1");
        assertThat((new RestrictToHostGroupAction()).trimRemoteAddress("192.168.0.11")).isEqualTo("192.168.0.11");
    }

    @Test 
    public void testAccessGranted() {
        assertThat((new RestrictToHostGroupAction()).addressMatchesPattern("192.168.0.1", "192.168.0.1")).isTrue();
        assertThat((new RestrictToHostGroupAction()).addressMatchesPattern("0:0:0:0:0:0:0:1", "0:0:0:0:0:0:0:1")).isTrue();

        assertThat((new RestrictToHostGroupAction()).addressMatchesPattern("192.168.0.1", "192.168.0.")).isTrue();
        assertThat((new RestrictToHostGroupAction()).addressMatchesPattern("192.168.1.1", "192.168.0.")).isFalse();
        assertThat((new RestrictToHostGroupAction()).addressMatchesPattern("192.168.1.1", "192.168.0")).isFalse();
        assertThat((new RestrictToHostGroupAction()).addressMatchesPattern("192.168.1.1", "192.168.")).isTrue();
        assertThat((new RestrictToHostGroupAction()).addressMatchesPattern("192.168.1.1", "192.168.1")).isTrue();
        assertThat((new RestrictToHostGroupAction()).addressMatchesPattern("192.168.11.1", "192.168.1")).isFalse();
    }
   
}
