package codesquad.webserver.dispatcher.requesthandler;

import codesquad.webserver.dispatcher.view.ModelAndView;
import codesquad.webserver.dispatcher.view.ModelKey;
import codesquad.webserver.dispatcher.view.ViewName;
import codesquad.webserver.filereader.FileReader;
import codesquad.webserver.httprequest.HttpRequest;

public abstract class AbstractRequestHandler implements RequestHandler {

    protected final FileReader fileReader;

    protected AbstractRequestHandler(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    @Override
    public ModelAndView handle(HttpRequest request) {
        switch (request.getRequestLine().getMethod()) {
            case GET:
                return handleGet(request);
            case POST:
                return handlePost(request);
            case PUT:
                return handlePut(request);
            case DELETE:
                return handleDelete(request);
            default:
                return handleMethodNotAllowed(request);
        }
    }

    protected ModelAndView handleGet(HttpRequest request) {
        return handleMethodNotAllowed(request);
    }

    protected ModelAndView handlePost(HttpRequest request) {
        return handleMethodNotAllowed(request);
    }

    protected ModelAndView handlePut(HttpRequest request) {
        return handleMethodNotAllowed(request);
    }

    protected ModelAndView handleDelete(HttpRequest request) {
        return handleMethodNotAllowed(request);
    }

    protected ModelAndView handleMethodNotAllowed(HttpRequest request) {
        ModelAndView mv = new ModelAndView(ViewName.EXCEPTION_VIEW);
        mv.addAttribute(ModelKey.STATUS_CODE, 405);
        mv.addAttribute(ModelKey.ERROR_MESSAGE, "Method Not Allowed");
        mv.addAttribute(ModelKey.METHOD, request.getRequestLine().getMethod());
        mv.addAttribute(ModelKey.PATH, request.getRequestLine().getPath());
        return mv;
    }
}
