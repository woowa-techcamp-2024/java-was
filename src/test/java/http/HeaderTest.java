package http;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class HeaderTest {

    @ParameterizedTest
    @MethodSource("normalHeader")
    void 정상적인_헤더_생성(String normalHeaderString, String key, String value) throws IOException {
        // given
        Header header = Header.from(new BufferedReader(new StringReader(normalHeaderString)));

        // when
        String result = header.getValue(key);

        // then
        assertEquals(value, result);
    }

    @Test
    @DisplayName("헤더 키가 없는 경우 빈 문자열을 반환한다.")
    void 헤더_키가_없는_경우() throws IOException {
        // given
        Header header = Header.from(new BufferedReader(new StringReader("Host: localhost:8080\r\n")));

        // when
        String result = header.getValue("No-Exist");

        // then
        assertEquals("", result);
    }


    @ParameterizedTest
    @DisplayName("헤더의 형식이 잘못된 경우 예외를 던진다.")
    @MethodSource("invalidHeader")
    void 헤더_형식이_잘못된_경우(String invalidHeaderString) throws IOException{
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            // when
            Header.from(new BufferedReader(new StringReader(invalidHeaderString)));
        });
    }

    static Stream<Arguments> normalHeader() {
        return Stream.of(
            Arguments.of("Host: localhost:8080\r\n", "Host", "localhost:8080"),
            Arguments.of("Content-Type: application/json\r\n", "Content-Type", "application/json"),
            Arguments.of("Accept: */*\r\n", "Accept", "*/*"),
            Arguments.of("User-Agent: Mozilla/5.0\r\n", "User-Agent", "Mozilla/5.0"),
            Arguments.of("Cache-Control: no-cache\r\n", "Cache-Control", "no-cache"),
            Arguments.of("Authorization: Bearer token\r\n", "Authorization", "Bearer token"),
            Arguments.of("Referer: http://example.com\r\n", "Referer", "http://example.com"),
            Arguments.of("Connection: keep-alive\r\n", "Connection", "keep-alive"),
            Arguments.of("Content-Length: 1234\r\n", "Content-Length", "1234"),
            Arguments.of("Cookie: sessionId=abc123\r\n", "Cookie", "sessionId=abc123"),
            Arguments.of("Empty-Header: \r\n", "Empty-Header", ""),
            Arguments.of("Host: localhost:8080\r\nContent-Type: application/json\r\n", "Content-Type", "application/json"),
            Arguments.of("Host: localhost:8080\r\nContent-Type: application/json\r\n", "Host", "localhost:8080"),
            Arguments.arguments("Multiple-Values: value1\r\nMultiple-Values: value2\r\n", "Multiple-Values", "value1, value2")
        );

    }

    static Stream<Arguments> invalidHeader() {
        return Stream.of(
            Arguments.of("InvalidHeaderFormat\r\n"),
            Arguments.of(": MissingKey\r\n"),
            Arguments.of("NoColonSeparator\r\n")
        );
    }
}