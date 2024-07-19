package codesquad.webserver.http;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpRequest {

    private final HttpRequestLine httpRequestLine;
    private final HttpHeaders headers;
    private final byte[] body;

    private Map<String, String> params = new HashMap<>();
    private Map<String, MultiPartFormData> multiPartFormData = new HashMap<>();

    public HttpRequest(HttpRequestLine httpRequestLine, HttpHeaders headers, byte[] body) {
        this.httpRequestLine = httpRequestLine;
        this.headers = headers;
        this.body = body;
    }

    public HttpVersion getVersion() {
        return httpRequestLine.getVersion();
    }

    public HttpMethod getMethod() {
        return httpRequestLine.getMethod();
    }

    public HttpPath getPath() {
        return httpRequestLine.getPath();
    }

    public byte[] getBody() {
        return body;
    }

    public MultiPartFormData getMultiPartFormData(String name) {
        return multiPartFormData.get(name);
    }

    public Map<String, String> getQueryStrings() {
        return Collections.unmodifiableMap(getPath().getQueryString());
    }

    public Optional<String> getHeaderValue(String headerName) {
        Map<String, String> header = headers.getHeaders();
        return Optional.ofNullable(header.get(headerName));
    }

    public String getBodyParamValue(String paramName) {
        return params.get(paramName);
    }

    public Cookie getCookie() {
        Optional<String> optCookieValues = getHeaderValue("Cookie");
        if (optCookieValues.isEmpty()) {
            return null;
        }

        String cookieValues = optCookieValues.get();
        String[] cookieValueParts = cookieValues.split(";");
        String[] nameAndValue = cookieValueParts[0].split("=");
        return new Cookie(nameAndValue[0], nameAndValue[1]);
    }

    public void setMultiPartFormData(Map<String, MultiPartFormData> multiPartFormData) {
        this.multiPartFormData = multiPartFormData;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "httpRequestLine=" + httpRequestLine +
                ", headers=" + headers +
                ", body=" + Arrays.toString(body) +
                ", params=" + params +
                ", multiPartFormData=" + multiPartFormData +
                '}';
    }
}
