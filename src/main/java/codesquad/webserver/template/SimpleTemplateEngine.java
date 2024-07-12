package codesquad.webserver.template;

import codesquad.webserver.annotation.Component;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        rendered = processForStatements(rendered, model);
        rendered = processIfStatements(rendered, model);
        rendered = processVariables(rendered, model);
        return rendered;
    }

    private String processVariables(String template, Map<String, Object> model) {
        Pattern pattern = Pattern.compile("\\{\\{\\s*(\\w+(?:\\.\\w+)*)\\s*\\}\\}");
        Matcher matcher = pattern.matcher(template);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            Object value = resolveKey(key, model);
            matcher.appendReplacement(sb, value != null ? value.toString() : "");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private Object resolveKey(String key, Map<String, Object> model) {
        String[] parts = key.split("\\.");
        Object value = model;
        for (String part : parts) {
            if (value instanceof Map) {
                value = ((Map<?, ?>) value).get(part);
            } else {
                value = getFieldValue(value, part);
            }
            if (value == null) {
                return null;
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

    private String processIfStatements(String template, Map<String, Object> model) {
        Pattern pattern = Pattern.compile("\\{\\{#if\\s+(\\w+(?:\\.\\w+)*)\\}\\}(.*?)\\{\\{else\\}\\}(.*?)\\{\\{\\/if\\}\\}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            String ifContent = matcher.group(2);
            String elseContent = matcher.group(3);
            Object value = resolveKey(key, model);
            if (value != null && Boolean.parseBoolean(value.toString())) {
                matcher.appendReplacement(sb, ifContent);
            } else {
                matcher.appendReplacement(sb, elseContent);
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String processForStatements(String template, Map<String, Object> model) {
        Pattern pattern = Pattern.compile("\\{\\{#for\\s+(\\w+)\\s+in\\s+(\\w+)\\}\\}(.*?)\\{\\{\\/for\\}\\}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String item = matcher.group(1);
            String collection = matcher.group(2);
            String content = matcher.group(3);
            Object listObj = model.get(collection);
            if (listObj instanceof List<?>) {
                List<?> list = (List<?>) listObj;
                StringBuilder repeatedContent = new StringBuilder();
                for (Object element : list) {
                    Map<String, Object> itemModel = new HashMap<>(model);
                    itemModel.put(item, element);
                    repeatedContent.append(processVariables(content, itemModel));
                }
                matcher.appendReplacement(sb, repeatedContent.toString());
            } else {
                matcher.appendReplacement(sb, "");
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
