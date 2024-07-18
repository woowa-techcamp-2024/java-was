//package codesquad.webserver.parser;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import codesquad.webserver.httprequest.HttpRequest;
//import java.io.BufferedReader;
//import java.io.StringReader;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.Map;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//public class MultipartParserTest {
//
//    @Test
//    @DisplayName("멀티파트 폼 데이터를 정상적으로 파싱해야 한다")
//    public void testParseMultipartFormData() throws Exception {
//        // Given
//        String rawRequest = "--boundary\r\n" +
//                "Content-Disposition: form-data; name=\"field1\"\r\n\r\n" +
//                "value1\r\n" +
//                "--boundary\r\n" +
//                "Content-Disposition: form-data; name=\"file1\"; filename=\"example.txt\"\r\n\r\n" +
//                "This is the content of the file.\r\n" +
//                "--boundary--";
//        BufferedReader reader = new BufferedReader(new StringReader(rawRequest));
//        HttpRequest request = new HttpRequest();
//        request.setHeaders(Map.of("Content-Type", List.of("multipart/form-data; boundary=boundary")));
//
//        // When
//        MultipartParser.parse(reader, request);
//        Map<String, List<String>> fields = request.getMultipartFields();
//        Map<String, List<HttpRequest.FileItem>> files = request.getMultipartFiles();
//
//        // Then
//        assertEquals(1, fields.size(), "파싱된 필드의 개수가 1개여야 합니다.");
//        assertEquals("value1", fields.get("field1").get(0));
//
//        assertEquals(1, files.size(), "파싱된 파일의 개수가 1개여야 합니다.");
//        HttpRequest.FileItem fileItem = files.get("file1").get(0);
//        assertNotNull(fileItem);
//        assertEquals("example.txt", fileItem.getFilename());
//        assertEquals("This is the content of the file.\r\n", new String(fileItem.getContent(), StandardCharsets.UTF_8));
//    }
//
//    @Test
//    @DisplayName("빈 멀티파트 폼 데이터를 파싱하면 빈 필드와 파일 맵을 반환해야 한다")
//    public void testParseEmptyMultipartFormData() throws Exception {
//        // Given
//        String rawRequest = "--boundary--";
//        BufferedReader reader = new BufferedReader(new StringReader(rawRequest));
//        HttpRequest request = new HttpRequest();
//        request.setHeaders(Map.of("Content-Type", List.of("multipart/form-data; boundary=boundary")));
//
//        // When
//        MultipartParser.parse(reader, request);
//        Map<String, List<String>> fields = request.getMultipartFields();
//        Map<String, List<HttpRequest.FileItem>> files = request.getMultipartFiles();
//
//        // Then
//        assertTrue(fields.isEmpty(), "파싱된 필드가 없어야 합니다.");
//        assertTrue(files.isEmpty(), "파싱된 파일이 없어야 합니다.");
//    }
//}
