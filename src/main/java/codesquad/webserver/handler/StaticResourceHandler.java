package codesquad.webserver.handler;

import codesquad.webserver.http.ContentTypeMapping;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.util.FileReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticResourceHandler implements ApiHandler {
    private static final Logger log = LoggerFactory.getLogger(DynamicResourceHandler.class);

    public HttpResponse handle(HttpRequest request) {
        return handle(request.getPath());
    }

    public static HttpResponse handle(String path){
        log.info("[StaticResourceHandler] handle: "+ path);
        if(FileReader.hasUploadFile("." + path)){
            byte[] bytes = FileReader.readFilToByte("." + path);
            String extension = path.substring(path.lastIndexOf(".")+1);
            return HttpResponse.ok(extension, bytes);
        }
        if (!FileReader.hasFile(path)) {
            return HttpResponse.notFound();
        }
        try{
            String content = FileReader.readFileToString(path);
            byte[] body = FileReader.stringToByteArray(content);
            String fileExtension = path.substring(path.lastIndexOf("."));
            return HttpResponse.ok(fileExtension, body);
        }catch (IOException e){
            return HttpResponse.notFound();
        }
    }

    public boolean canHandle(HttpRequest request) {
        Pattern URL_PATTERN = Pattern.compile(".*" + "(" + String.join("|", ContentTypeMapping.getFileExtensions()) + ")$", Pattern.CASE_INSENSITIVE);
        return URL_PATTERN.matcher(request.getPath()).matches();
    }
}
