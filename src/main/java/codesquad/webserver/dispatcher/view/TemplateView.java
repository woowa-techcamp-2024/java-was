package codesquad.webserver.dispatcher.view;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Component;
import codesquad.webserver.template.TemplateEngine;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TemplateView implements View {

    private static final Logger logger = LoggerFactory.getLogger(TemplateView.class);

    private final TemplateEngine templateEngine;

    @Autowired
    public TemplateView(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public ViewResult render(Map<String, ?> model) {
        String template = (String) model.get(ModelKey.CONTENT);
        if (template == null) {
            logger.error("Template content is missing in the model");
        }

        String renderedContent = templateEngine.render(template, (Map<String, Object>) model);
        byte[] renderedBytes = renderedContent.getBytes(StandardCharsets.UTF_8);

        return new ViewResult(renderedBytes, new ArrayList<>(), new ArrayList<>(), 200);

    }
}