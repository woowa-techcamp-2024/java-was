package codesquad.handler;


import codesquad.http.HttpMethod;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Handler {
    private Logger log = LoggerFactory.getLogger(Handler.class);

    public void handle(HttpRequest request, HttpResponse response){
        log.info("request: "+request);
        HttpMethod method = request.getMethod();
        if(method.isPost()){
            doPost(request, response);
        }else if(method.isGet()){
            doGet(request, response);
        }
        log.info("response: "+response);
    }

    protected void doGet(HttpRequest request, HttpResponse response){}

    protected void doPost(HttpRequest request, HttpResponse response){}
}
