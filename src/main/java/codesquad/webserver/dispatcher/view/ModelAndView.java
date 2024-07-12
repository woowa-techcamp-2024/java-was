package codesquad.webserver.dispatcher.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private ViewName viewName;
    private Map<String, Object> model = new HashMap<>();

    public ModelAndView(ViewName viewName) {
        this.viewName = viewName;
    }

    public ModelAndView addAttribute(String key, Object value) {
        model.put(key, value);
        return this;
    }

    public ViewName getViewName() {
        return viewName;
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }
}
