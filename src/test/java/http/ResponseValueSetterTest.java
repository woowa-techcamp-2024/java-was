package http;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import exception.GeneralException;
import http.startline.ResponseLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResponseValueSetterTest {

    HttpResponse httpResponse;
    HttpRequest httpRequest;

    @BeforeEach
    void setUp() {
        httpResponse = new HttpResponse(
            new ResponseLine(),
            Header.emptyHeader(),
            new byte[0]
        );
    }

    @Test
    void testSuccessWithBody() {
        byte[] body = "test body".getBytes();
        ResponseValueSetter.success(httpResponse, body);

        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();

        assertEquals(200, responseLine.getStatusCode());
        assertEquals("OK", responseLine.getStatusMessage());
        assertEquals(String.valueOf(body.length),
            httpResponse.getHeader().getValue("Content-Length"));
        assertArrayEquals(body, httpResponse.getBody());
    }

    @Test
    void testSuccessWithoutBody() {
        ResponseValueSetter.success(httpResponse);
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();

        assertEquals(200, responseLine.getStatusCode());
        assertEquals("OK", responseLine.getStatusMessage());
        assertEquals("0", httpResponse.getHeader().getValue("Content-Length"));
        assertArrayEquals(new byte[0],httpResponse.getBody());
    }

    @Test
    void testRedirect() {
        String urlPath = "/redirect";
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        ResponseValueSetter.redirect(httpRequest, httpResponse, urlPath);

        assertEquals(302, responseLine.getStatusCode());
        assertEquals("Found", responseLine.getStatusMessage());
        assertEquals(urlPath, httpResponse.getHeader().getValue("Location"));
    }

    @Test
    void testFail() {
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        GeneralException error = new GeneralException("Error", 500);
        ResponseValueSetter.fail(httpResponse, error);

        assertEquals(500, responseLine.getStatusCode());
        assertEquals("Error", responseLine.getStatusMessage());
        assertEquals("0", httpResponse.getHeader().getValue("Content-Length"));
        assertArrayEquals(new byte[0], httpResponse.getBody());
    }
}