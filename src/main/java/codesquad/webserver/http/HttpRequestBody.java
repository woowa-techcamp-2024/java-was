package codesquad.webserver.http;

import java.util.Map;

public class HttpRequestBody {

    private final Map<String, String> params;

    public HttpRequestBody(Map<String, String> params) {
        this.params = params;
    }

    public static HttpRequestBody ofEmpty() {
        return new HttpRequestBody(Map.of());
    }

    public boolean isExists() {
        return params != null && !params.isEmpty();
    }

    public int size() {
        return params.size();
    }

    public String getParamValue(String paramName) {
        return params.get(paramName);
    }

    @Override
    public String toString() {
        return "" + params;
    }
}
