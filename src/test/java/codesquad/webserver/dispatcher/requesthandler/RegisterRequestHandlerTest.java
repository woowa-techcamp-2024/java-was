package codesquad.webserver.dispatcher.requesthandler;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.ModelKey;
import codesquad.webserver.dispatcher.view.ViewName;
import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.parser.RequestLine;
import codesquad.webserver.parser.enums.HttpMethod;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegisterRequestHandlerTest {

    private RegisterRequestHandler registerRequestHandler;
    private FileReader mockFileReader;

    @BeforeEach
    void setUp() {
        mockFileReader = new MockFileReader();
        registerRequestHandler = new RegisterRequestHandler(mockFileReader);
    }

    @Test
    @DisplayName("정상적인 등록 페이지 요청을 처리한다")
    void handleGetRequestSuccessfully() throws IOException {
        HttpRequest request = createTestHttpRequest();

        ModelAndView result = registerRequestHandler.handleGet(request);

        assertThat(result.getViewName()).isEqualTo(ViewName.TEMPLATE_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.CONTENT, "mock registration page content");
    }

    @Test
    @DisplayName("등록 페이지 파일을 찾을 수 없을 때 예외 뷰를 반환한다")
    void handleGetRequestWithFileNotFound() {
        mockFileReader = new MockFileReaderWithError();
        registerRequestHandler = new RegisterRequestHandler(mockFileReader);
        HttpRequest request = createTestHttpRequest();

        ModelAndView result = registerRequestHandler.handleGet(request);

        assertThat(result.getViewName()).isEqualTo(ViewName.EXCEPTION_VIEW);
        assertThat(result.getModel()).containsEntry(ModelKey.STATUS_CODE, 404);
        assertThat(result.getModel()).containsEntry(ModelKey.ERROR_MESSAGE, "Registration page not found");
    }

    private HttpRequest createTestHttpRequest() {
        RequestLine requestLine = new RequestLine(HttpMethod.GET, "/register", "/register", "HTTP/1.1");
        Map<String, List<String>> headers = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        String body = "";
        return new HttpRequest(requestLine, headers, params, body);
    }

    private static class MockFileReader extends FileReader {
        @Override
        public FileResource read(String path) {
            return new FileResource(null, "index.html") {
                @Override
                public String readFileContent() {
                    return "mock registration page content";
                }
            };
        }
    }

    private static class MockFileReaderWithError extends FileReader {
        @Override
        public FileResource read(String path) throws IOException {
            throw new IOException("File not found");
        }
    }
}
