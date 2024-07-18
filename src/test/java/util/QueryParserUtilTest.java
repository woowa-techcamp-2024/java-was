package util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.Test;

class QueryParserUtilTest {

    @Test
    void testParseEmptyQuery() {
        Map<String, String> result = QueryParserUtil.parseQuery("");
        assertTrue(result.isEmpty(), "Parsing an empty query should return an empty map.");
    }

    @Test
    void testParseSingleParameter() {
        Map<String, String> result = QueryParserUtil.parseQuery("key=value");
        assertEquals(1, result.size(), "Map should contain exactly one entry.");
        assertEquals("value", result.get("key"), "Value for 'key' should be 'value'.");
    }

    @Test
    void testParseMultipleParameters() {
        Map<String, String> result = QueryParserUtil.parseQuery("key1=value1&key2=value2");
        assertEquals(2, result.size(), "Map should contain exactly two entries.");
        assertEquals("value1", result.get("key1"), "Value for 'key1' should be 'value1'.");
        assertEquals("value2", result.get("key2"), "Value for 'key2' should be 'value2'.");
    }

    @Test
    void testParseEncodedCharacters() {
        Map<String, String> result = QueryParserUtil.parseQuery("key=%20value%20");
        assertEquals(1, result.size(), "Map should contain exactly one entry.");
        assertEquals(" value ", result.get("key"), "Value for 'key' should be decoded to ' value '.");
    }

    @Test
    void testParseMulti() {
        String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
        String body = "--" + boundary + "\r\n" +
            "Content-Disposition: form-data; name=\"field1\"\r\n" +
            "\r\n" +
            "value1\r\n" +
            "--" + boundary + "\r\n" +
            "Content-Disposition: form-data; name=\"field2\"\r\n" +
            "Content-Type: text/plain\r\n" +
            "\r\n" +
            "value2\r\n" +
            "--" + boundary + "--";

        Map<String, Map<String, byte[]>> result = QueryParserUtil.parseMulti(body.getBytes(), boundary);

        assertEquals(2, result.size());

        assertTrue(result.containsKey("field1"));
        assertArrayEquals("value1".getBytes(), result.get("field1").get("content"));

        assertTrue(result.containsKey("field2"));
        assertArrayEquals("value2".getBytes(), result.get("field2").get("content"));
    }
}