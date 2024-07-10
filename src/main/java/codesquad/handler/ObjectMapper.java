package codesquad.handler;

import codesquad.http.QueryParameters;
import codesquad.http.parser.ParsersFactory;
import codesquad.http.parser.QueryParametersParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ObjectMapper {

    private static ObjectMapper instance;

    private final Logger log = LoggerFactory.getLogger(ObjectMapper.class);

    private final QueryParametersParser queryParametersParser = ParsersFactory.getQueryParametersParser();

    private ObjectMapper() {
    }

    public static ObjectMapper getInstance() {
        if (instance == null) {
            instance = new ObjectMapper();
        }
        return instance;
    }

    public <T> T readQueryString(String queryString, Class<T> recordClass) {
        try {
            QueryParameters parameters = queryParametersParser.parse(queryString);

            RecordComponent[] recordComponents = recordClass.getRecordComponents();
            List<Object> arguments = new ArrayList<>();
            for (RecordComponent recordComponent : recordComponents) {
                Object argument = findArgument(recordComponent, parameters);
                arguments.add(argument);
            }

            Constructor<T> constructor = recordClass.getDeclaredConstructor(
                    Arrays.stream(recordComponents)
                            .map(RecordComponent::getType)
                            .toArray(Class[]::new)
            );
            return constructor.newInstance(arguments.toArray());
        } catch (ReflectiveOperationException e) {
            log.error(e.getMessage(), e);
        }

        throw new IllegalArgumentException("[ERROR] Query String을 읽는 중 오류가 발생했습니다.");
    }

    private Object findArgument(RecordComponent recordComponent, QueryParameters parameters) {
        String componentName = recordComponent.getName();
        Class<?> componentType = recordComponent.getType();

        String value = parameters.getFirstValue(componentName)
                .orElseThrow(() -> new IllegalArgumentException(String.format("[ERROR] %s 필드의 값이 없습니다.", componentName)));
        return convert(value, componentType);
    }

    private Object convert(String value, Class<?> type) {
        if (type.isAssignableFrom(String.class)) {
            return value;
        }
        if (type.isAssignableFrom(Integer.class)) {
            return Integer.parseInt(value);
        }
        if (type.isAssignableFrom(Long.class)) {
            return Long.parseLong(value);
        }
        if (type.isAssignableFrom(Double.class)) {
            return Double.parseDouble(value);
        }
        if (type.isAssignableFrom(Boolean.class)) {
            return Boolean.parseBoolean(value);
        }
        return null;
    }

}
