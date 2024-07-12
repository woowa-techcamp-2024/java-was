package util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class HttpStatusCodeTest {

    static Stream<Object[]> statusCodeProvider() {
        return Stream.of(
            new Object[]{HttpStatusCode.OK, 200, "OK"},
            new Object[]{HttpStatusCode.CREATED, 201, "Created"},
            new Object[]{HttpStatusCode.NO_CONTENT, 204, "No Content"},
            // Add other status codes as needed
            new Object[]{HttpStatusCode.INTERNAL_SERVER_ERROR, 500, "Internal Server Error"}
        );
    }

    @ParameterizedTest
    @MethodSource("statusCodeProvider")
    void testGetStatusCode(HttpStatusCode httpStatusCode, int expectedCode, String expectedMessage) {
        assertEquals(expectedCode, httpStatusCode.getStatusCode(), "Status code does not match for " + httpStatusCode);
    }

    @ParameterizedTest
    @MethodSource("statusCodeProvider")
    void testGetStatusMessage(HttpStatusCode httpStatusCode, int expectedCode, String expectedMessage) {
        assertEquals(expectedMessage, httpStatusCode.getStatusMessage(), "Status message does not match for " + httpStatusCode);
    }
}