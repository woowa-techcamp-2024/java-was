package codesquad.webserver.dispatcher.requesthandler;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Component;
import codesquad.webserver.annotation.Controller;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.ModelKey;
import codesquad.webserver.dispatcher.view.ViewName;
import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;
import java.io.IOException;

@Controller
@RequestMapping(path = "/register")
public class RegisterRequestHandler extends AbstractRequestHandler {

    private static final String FILE_PATH = "/registration/index.html";

    @Autowired
    public RegisterRequestHandler(FileReader fileReader) {
        super(fileReader);
    }

    @Override
    protected ModelAndView handleGet(HttpRequest request) {
        try {
            FileReader.FileResource file = fileReader.read(FILE_PATH);
            return new ModelAndView(ViewName.TEMPLATE_VIEW)
                    .addAttribute(ModelKey.CONTENT, file.readFileContent());
        } catch (IOException e) {
            return new ModelAndView(ViewName.EXCEPTION_VIEW)
                    .addAttribute(ModelKey.STATUS_CODE, 404)
                    .addAttribute(ModelKey.ERROR_MESSAGE, "Registration page not found");
        }
    }
}
