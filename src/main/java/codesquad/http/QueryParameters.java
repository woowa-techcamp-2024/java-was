package codesquad.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class QueryParameters {

    private final Map<String, List<String>> parameters = new HashMap<>();

    public static QueryParameters empty() {
        return new QueryParameters();
    }

    public void addValue(String name, String value) {
        parameters.computeIfAbsent(name, key -> new ArrayList<>())
                .add(value);
    }

    public boolean isExists() {
        return !parameters.isEmpty();
    }

    public Optional<String> getFirstValue(String name) {
        if (parameters.containsKey(name)) {
            String firstValue = parameters.get(name)
                    .get(0);
            return Optional.of(firstValue);
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "QueryParameters{" +
                "parameters=" + parameters +
                '}';
    }

}
