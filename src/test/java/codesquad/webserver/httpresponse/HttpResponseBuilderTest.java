package codesquad.webserver.httpresponse;

import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.filereader.FileReader.FileResource;
import codesquad.webserver.session.cookie.HttpCookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class HttpResponseBuilderTest {

    private HttpResponseBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new HttpResponseBuilder();
    }

    @Test
    @DisplayName("기본 상태 코드와 메시지를 설정한다")
    void testDefaultStatusCodeAndMessage() {
        HttpResponse response = builder.build();
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getStatusMessage()).isEqualTo("OK");
    }

    @Test
    @DisplayName("상태 코드를 설정할 수 있다")
    void testSetStatusCode() {
        HttpResponse response = builder.statusCode(404).build();
        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("상태 메시지를 설정할 수 있다")
    void testSetStatusMessage() {
        HttpResponse response = builder.statusMessage("Not Found").build();
        assertThat(response.getStatusMessage()).isEqualTo("Not Found");
    }

    @Test
    @DisplayName("헤더를 추가하고 가져올 수 있다")
    void testAddAndGetHeader() {
        HttpResponse response = builder.header("Content-Type", "text/html").build();
        assertThat(response.getHeader("Content-Type")).containsExactly("text/html");
    }

    @Test
    @DisplayName("여러 헤더를 추가하고 가져올 수 있다")
    void testAddAndGetMultipleHeaders() {
        HttpResponse response = builder.header("Set-Cookie", "cookie1=value1")
                .header("Set-Cookie", "cookie2=value2")
                .build();
        assertThat(response.getHeader("Set-Cookie")).containsExactly("cookie1=value1", "cookie2=value2");
    }

    @Test
    @DisplayName("쿠키를 추가하고 가져올 수 있다")
    void testAddAndGetCookies() {
        HttpCookie cookie = new HttpCookie("session", "1234");
        HttpResponse response = builder.cookie(cookie).build();
        assertThat(response.getCookies()).hasSize(1);
        assertThat(response.getCookies().get(0).getName()).isEqualTo("session");
        assertThat(response.getCookies().get(0).getValue()).isEqualTo("1234");
    }

    @Test
    @DisplayName("응답 본문을 설정하고 가져올 수 있다")
    void testSetAndGetBody() {
        byte[] body = "Hello, World!".getBytes();
        HttpResponse response = builder.body(body).build();
        assertThat(response.getBody()).isEqualTo(body);
    }

    @Test
    @DisplayName("HTTP 응답을 올바르게 생성한다")
    void testGenerateHttpResponse() {
        byte[] body = "Hello, World!".getBytes();
        HttpResponse response = builder.statusCode(200)
                .statusMessage("OK")
                .header("Content-Type", "text/html")
                .body(body)
                .build();

        byte[] responseBytes = response.generateHttpResponse();
        String responseString = new String(responseBytes);

        assertThat(responseString).contains("HTTP/1.1 200 OK");
        assertThat(responseString).contains("Content-Type: text/html");
        assertThat(responseString).contains("Hello, World!");
    }

    @Test
    @DisplayName("OK 응답 빌더를 생성한다")
    void testOkBuilder() {
        HttpResponse response = HttpResponseBuilder.ok().build();
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getStatusMessage()).isEqualTo("OK");
    }

    @Test
    @DisplayName("Not Found 응답 빌더를 생성한다")
    void testNotFoundBuilder() {
        HttpResponse response = HttpResponseBuilder.notFound().build();
        assertThat(response.getStatusCode()).isEqualTo(404);
        assertThat(response.getStatusMessage()).isEqualTo("Not Found");
    }

    @Test
    @DisplayName("파일로부터 HTTP 응답을 생성한다")
    void testBuildFromFile() throws IOException {
        String fileContent = "This is a test file.";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        FileResource resource = new FileReaderMock.FileResourceMock(inputStream, "test.txt");

        HttpResponse response = HttpResponseBuilder.buildFromFile(resource);
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getHeader("Content-Type")).containsExactly("text/plain");
        assertThat(response.getBody()).isEqualTo(fileContent.getBytes());
    }

    @Test
    @DisplayName("지정된 상태 코드와 메시지로 파일로부터 HTTP 응답을 생성한다")
    void testBuildFromFileWithStatus() throws IOException {
        String fileContent = "This is a test file.";
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        FileResource resource = new FileReaderMock.FileResourceMock(inputStream, "test.txt");

        HttpResponse response = HttpResponseBuilder.buildFromFile(resource, 404, "Not Found");
        assertThat(response.getStatusCode()).isEqualTo(404);
        assertThat(response.getStatusMessage()).isEqualTo("Not Found");
        assertThat(response.getHeader("Content-Type")).containsExactly("text/plain");
        assertThat(response.getBody()).isEqualTo(fileContent.getBytes());
    }

    @Test
    @DisplayName("404 파일을 읽을 수 없을 때 기본 404 응답을 생성한다")
    void testBuildNotFoundFromFile() {
        HttpResponse response = HttpResponseBuilder.notFound()
                .body("NotFound".getBytes())
                .build();
        assertThat(response.getStatusCode()).isEqualTo(404);
        assertThat(response.getStatusMessage()).isEqualTo("Not Found");
        assertThat(new String(response.getBody())).isEqualTo("NotFound");
    }

    @Test
    @DisplayName("403 파일을 읽을 수 없을 때 기본 403 응답을 생성한다")
    void testBuildForbiddenFromFile() {
        // 403.html 파일이 없는 경우를 모킹하여 테스트
        HttpResponse response = HttpResponseBuilder.forbidden()
                .body("Forbidden".getBytes())
                .build();
        assertThat(response.getStatusCode()).isEqualTo(403);
        assertThat(response.getStatusMessage()).isEqualTo("Forbidden");
        assertThat(new String(response.getBody())).isEqualTo("Forbidden");
    }

    @Test
    @DisplayName("임의의 위치로 리디렉션 응답을 생성한다")
    void testRedirectBuilder() {
        String location = "/redirected";
        HttpResponse response = HttpResponseBuilder.redirect(location).build();
        assertThat(response.getStatusCode()).isEqualTo(302);
        assertThat(response.getStatusMessage()).isEqualTo("Found");
        assertThat(response.getHeader("Location")).containsExactly(location);
    }

    @Test
    @DisplayName("Method Not Allowed 응답 빌더를 생성한다")
    void testMethodNotAllowedBuilder() {
        HttpResponse response = HttpResponseBuilder.methodNotAllowed().build();
        assertThat(response.getStatusCode()).isEqualTo(405);
        assertThat(response.getStatusMessage()).isEqualTo("Method Not Allowed");
    }

    @Test
    @DisplayName("Internal Server Error 응답 빌더를 생성한다")
    void testServerErrorBuilder() {
        HttpResponse response = HttpResponseBuilder.serverError().build();
        assertThat(response.getStatusCode()).isEqualTo(500);
        assertThat(response.getStatusMessage()).isEqualTo("Internal Server Error");
    }

    // Mock implementation of FileReader.FileResource for testing
    static class FileReaderMock extends FileReader {
        @Override
        public FileResource read(String requestPath) {
            String content = "Forbidden file content";
            InputStream inputStream = new ByteArrayInputStream(content.getBytes());
            return new FileResourceMock(inputStream, requestPath);
        }

        static class FileResourceMock extends FileResource {
            private final InputStream inputStream;
            private final String fileName;

            public FileResourceMock(InputStream inputStream, String fileName) {
                super(inputStream, fileName);
                this.inputStream = inputStream;
                this.fileName = fileName;
            }

            @Override
            public InputStream getInputStream() {
                return inputStream;
            }

            @Override
            public String getFileName() {
                return fileName;
            }
        }
    }
}
