package codesquad.webserver.util;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class JsonStringConverter {
    public static <T> String collectionToJsonString(Collection<T> objects){
        if(objects == null || objects.isEmpty()){
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for(T obj : objects){
            if(!first){
                sb.append(",");
            }
            sb.append(objectToJsonString(obj));
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }

    public static<T> String objectToJsonString(T obj){
        if(obj == null){
            return "null";
        }
        Class<?> clazz = obj.getClass();
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (!first) {
                sb.append(",");
            }
            try {
                sb.append("\"").append(field.getName()).append("\":")
                        .append(valueToJsonString(field.get(obj)));
                first = false;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        sb.append("}");
        return sb.toString();
    }

    private static String valueToJsonString(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "\"" + escapeJson(value.toString()) + "\"";
        } else if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        } else if(value instanceof LocalDateTime){
            return "\"" + ((LocalDateTime) value).format(DateTimeFormatter.ISO_DATE_TIME) + "\"";
        }
        else {
            return objectToJsonString(value);
        }
    }

    private static String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        StringBuilder escaped = new StringBuilder();
        for (char c : input.toCharArray()) {
            switch (c) {
                case '"':
                    escaped.append("\\\"");
                    break;
                case '\\':
                    escaped.append("\\\\");
                    break;
                case '\b':
                    escaped.append("\\b");
                    break;
                case '\f':
                    escaped.append("\\f");
                    break;
                case '\n':
                    escaped.append("\\n");
                    break;
                case '\r':
                    escaped.append("\\r");
                    break;
                case '\t':
                    escaped.append("\\t");
                    break;
                default:
                    if (c < ' ') {
                        escaped.append(String.format("\\u%04x", (int) c));
                    } else {
                        escaped.append(c);
                    }
            }
        }
        return escaped.toString();
    }
}
