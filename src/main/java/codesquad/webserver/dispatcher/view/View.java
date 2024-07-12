package codesquad.webserver.dispatcher.view;

import java.util.Map;

public interface View {
    ViewResult render(Map<String, ?> model);
}
