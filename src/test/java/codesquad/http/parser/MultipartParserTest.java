package codesquad.http.parser;

import codesquad.http.MultipartFile;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MultipartParserTest {

    @Test
    void multipart_form_data_형식_데이터를_파싱할_수_있다() throws IOException {
        String contentType = "multipart/form-data; boundary=----TestBoundary";
        byte[] body = (
                "------TestBoundary\r\n" +
                        "Content-Disposition: form-data; name=\"file\"; filename=\"test.txt\"\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "\r\n" +
                        "hello\r\n" +
                        "------TestBoundary\r\n" +
                        "Content-Disposition: form-data; name=\"content\"\r\n" +
                        "\r\n" +
                        "test\r\n" +
                        "------TestBoundary--\r\n"
        ).getBytes();

        Map<String, Object> multipart = MultipartParser.parse(body, contentType);

        assertThat(multipart.get("file")).isInstanceOf(MultipartFile.class);
        assertThat(((MultipartFile) multipart.get("file")).name()).isEqualTo("file");
        assertThat(((MultipartFile) multipart.get("file")).filename()).isEqualTo("test.txt");
        assertThat(((MultipartFile) multipart.get("file")).contentType()).isEqualTo("text/plain");
        assertThat(((MultipartFile) multipart.get("file")).content()).isEqualTo("hello".getBytes());

        assertThat(multipart.get("content")).isInstanceOf(String.class);
        assertThat(multipart.get("content")).isEqualTo("test");
    }
}