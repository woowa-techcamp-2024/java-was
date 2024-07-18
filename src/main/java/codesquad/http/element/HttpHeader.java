package codesquad.http.element;

import java.util.ArrayList;
import java.util.List;

public class HttpHeader {
    private final List<String> header = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < header.size(); i++) {
            stringBuilder.append(header.get(i));
            if (i != header.size() - 1) stringBuilder.append("; ");
        }
        return stringBuilder.toString();
    }

    public void append(String value) { header.add(value); }

    public String getFirstHeaderValue() { return header.get(0); }
    
    public String getHeaderValue(String key) {
        for (String value: header) {
            if (value.startsWith(key)) return value.split("=")[1].trim();
        }
        return null;
    }
}
