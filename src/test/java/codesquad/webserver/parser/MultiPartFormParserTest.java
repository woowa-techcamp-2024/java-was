package codesquad.webserver.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MultiPartFormParserTest {

    MultiPartFormParser multiPartFormParser = new MultiPartFormParser();

    @Test
    @DisplayName("")
    void test() throws IOException {
        String requestBody = """
                ------WebKitFormBoundary1ufQbus05PuavAMb
                Content-Disposition: form-data; name="title"

                이미지 게시글 제목
                ------WebKitFormBoundary1ufQbus05PuavAMb
                Content-Disposition: form-data; name="content"

                이미지 게시글 내용
                ------WebKitFormBoundary1ufQbus05PuavAMb
                Content-Disposition: form-data; name="image"; filename="test.png"
                Content-Type: application/octet-stream
                                
                ------WebKitFormBoundary1ufQbus05PuavAMb--""";

        InputStream inputStream = new ByteArrayInputStream(requestBody.getBytes("UTF-8"));
        String boundary = "----WebKitFormBoundary1ufQbus05PuavAMb";
        Assertions.assertThatThrownBy(() -> MultiPartFormParser.parse(inputStream, boundary))
                .isInstanceOf(IOException.class);

//        assertEquals("이미지 게시글 제목", formData.getTextField("title"));
//        assertEquals("이미지 게시글 내용", formData.getTextField("content"));

//        ImageFile imageFile = formData.getImageFile("image");
//        assertNotNull(imageFile);
//        assertEquals("test.png", imageFile.getFileName());
//        assertEquals("application/octet-stream", imageFile.getContentType());
    }
}