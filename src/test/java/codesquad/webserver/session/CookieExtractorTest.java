package codesquad.webserver.session;


import codesquad.application.session.CookieExtractor;
import codesquad.webserver.Mock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CookieExtractorTest {

    private static final String SID = "a";

    @Test
    public void getSid(){
        assertEquals(SID, CookieExtractor.getSid(Mock.makeHttpRequestWithCookie(SID)));
    }

}
