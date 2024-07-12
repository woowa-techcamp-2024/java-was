package codesquad.webserver.template;

import java.util.Map;

public interface TemplateEngine {
    String render(String template, Map<String, Object> model);
}
