package codesquad.was.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import codesquad.was.http.type.MimeType;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MultipartParserTest {

    @Test
    void 멀티파트_요청을_파싱할_수_있다() throws IOException {
        // given
        String multipartRequest = "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\n"
                + "Content-Disposition: form-data; name=\"text1\"\r\n"
                + "\r\n"
                + "This is the content of the first text field.\r\n"
                + "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\n"
                + "Content-Disposition: form-data; name=\"file1\"; filename=\"example.png\"\r\n"
                + "Content-Type: image/png\r\n"
                + "\r\n"
                + "This is the content of the file.\r\n"
                + "------WebKitFormBoundary7MA4YWxkTrZu0gW--";
        MultipartParser multipartParser = new MultipartParser(multipartRequest.getBytes(),
                List.of("multipart/form-data", "boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW"));

        // when
        MultipartFile multipartFile = multipartParser.getMultipartFile();
        Map<String, String> formParameters = multipartParser.getFormParameters();

        // then
        assertAll(
                () -> assertThat(multipartFile.getMimeType()).isEqualTo(MimeType.PNG),
                () -> assertThat(multipartFile.getOriginalFilename()).isEqualTo("example.png"),
                () -> assertThat(formParameters.containsKey("text1")).isTrue(),
                () -> assertThat(formParameters.containsValue("This is the content of the first text field.")).isTrue()
        );
    }

}
