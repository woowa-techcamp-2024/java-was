package codesquad.webserver.template;

import codesquad.webserver.annotation.Component;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SimpleTemplateEngine implements TemplateEngine {
    private static final Logger logger = LoggerFactory.getLogger(SimpleTemplateEngine.class);

    @Override
    public String render(String template, Map<String, Object> model) {
        String rendered = template;
        while (true) {
            String previous = rendered;
            rendered = processForStatements(rendered, model);
            rendered = processIfStatements(rendered, model);
            rendered = processVariables(rendered, model);
            if (rendered.equals(previous)) {
                break;
            }
        }
        return rendered;
    }


    private String processForStatements(String template, Map<String, Object> model) {
        Pattern pattern = Pattern.compile("\\{\\{#for\\s+(\\w+)\\s+in\\s+(\\w+(?:\\.\\w+)*)\\}\\}(.*?)\\{\\{/for\\}\\}",
                Pattern.DOTALL);
        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String item = matcher.group(1);
            String collection = matcher.group(2);
            String content = matcher.group(3);
            Object listObj = resolveKey(collection, model);
            if (listObj instanceof List<?>) {
                List<?> list = (List<?>) listObj;
                StringBuilder repeatedContent = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    Object element = list.get(i);
                    Map<String, Object> itemModel = new HashMap<>(model);
                    itemModel.put(item, element);
                    itemModel.put(item + "@index", i);
                    String processedContent = processForStatements(content, itemModel);
                    processedContent = processIfStatements(processedContent, itemModel);
                    processedContent = processVariables(processedContent, itemModel);
                    repeatedContent.append(processedContent);
                }
                matcher.appendReplacement(sb, Matcher.quoteReplacement(repeatedContent.toString()));
            } else {
                matcher.appendReplacement(sb, "");
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String processIfStatements(String template, Map<String, Object> model) {
        Pattern pattern = Pattern.compile("\\{\\{#if\\s+(.+?)\\}\\}(.*?)(?:\\{\\{else\\}\\}(.*?))?\\{\\{/if\\}\\}",
                Pattern.DOTALL);
        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String condition = matcher.group(1);
            String ifContent = matcher.group(2);
            String elseContent = matcher.group(3) != null ? matcher.group(3) : "";
            boolean conditionResult = evaluateCondition(condition, model);
            if (conditionResult) {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(ifContent));
            } else {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(elseContent));
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private boolean evaluateCondition(String condition, Map<String, Object> model) {
        if (condition.contains(" > ")) {
            String[] parts = condition.split(" > ");
            Object left = resolveKey(parts[0].trim(), model);
            Object right = resolveKey(parts[1].trim(), model);
            if (left instanceof Number && right instanceof Number) {
                return ((Number) left).doubleValue() > ((Number) right).doubleValue();
            } else if (left instanceof List && right instanceof Number) {
                return ((List<?>) left).size() > ((Number) right).intValue();
            }
        } else if (condition.contains(" == ")) {
            String[] parts = condition.split(" == ");
            Object left = resolveKey(parts[0].trim(), model);
            Object right = resolveKey(parts[1].trim(), model);
            return Objects.equals(left, right);
        } else if (condition.contains(" != ")) {
            String[] parts = condition.split(" != ");
            Object left = resolveKey(parts[0].trim(), model);
            Object right = resolveKey(parts[1].trim(), model);
            return !Objects.equals(left, right);
        }

        Object value = resolveKey(condition, model);
        return value != null && (value instanceof Boolean ? (Boolean) value : Boolean.parseBoolean(value.toString()));
    }

    private String processVariables(String template, Map<String, Object> model) {
        Pattern pattern = Pattern.compile("\\{\\{\\s*(\\w+(?:\\.\\w+)*)\\s*\\}\\}");
        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            Object value = resolveKey(key, model);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(value != null ? value.toString() : ""));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private Object resolveKey(String key, Map<String, Object> model) {
        String[] parts = key.split("\\.");
        Object value = model;
        for (String part : parts) {
            if (value == null) {
                return null;
            }
            if (value instanceof Map) {
                value = ((Map<?, ?>) value).get(part);
            } else if (value instanceof List) {
                if ("length".equals(part)) {
                    return ((List<?>) value).size();
                } else {
                    try {
                        int index = Integer.parseInt(part);
                        value = ((List<?>) value).get(index);
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        return null;
                    }
                }
            } else {
                value = getFieldValue(value, part);
            }
        }
        return value;
    }

    private Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("Field access error: ", e);
            return null;
        }
    }
}