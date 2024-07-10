package util;

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
}