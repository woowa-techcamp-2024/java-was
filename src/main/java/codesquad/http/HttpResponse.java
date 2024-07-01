package codesquad.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
    private final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private DataOutputStream outputStream;
    private Map<String, String> headers = new HashMap<>();
    public HttpResponse(OutputStream outputStream) {
        this.outputStream = new DataOutputStream(outputStream);
    }

    public void setBodyFile(String path){
        try{
            String STATIC_DIRECTORY_PATH = "./src/main/resources/static";
            byte[] body = Files.readAllBytes(new File(STATIC_DIRECTORY_PATH +path).toPath());
            setSuccessStatusHeader();
            headers.put("Content-Length", body.length+"");
            processHeader();
            setResponseBody(body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setBodyMessage(String body) {
        byte[] contents = body.getBytes();
        setSuccessStatusHeader();
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Content-Length", contents.length +"");
        processHeader();
        setResponseBody(contents);
    }

    private void setSuccessStatusHeader(){
        try{
            outputStream.writeBytes("HTTP/1.1 200 OK\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void setNewLine(){
        try{
            outputStream.writeBytes("\r\n");
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }

    private void setResponseBody(byte[] body){
        try{
            outputStream.write(body, 0, body.length);
            outputStream.writeBytes("\r\n");
            outputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void processHeader(){
        try{
            for(String key : headers.keySet()){
                outputStream.writeBytes(key+": "+headers.get(key)+"\r\n");
            }
            setNewLine();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                ", headers=" + headers +
                '}';
    }
}
