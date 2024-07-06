package codesquad.webserver.http;

import java.util.Set;

public class HttpResponse {
    private final int statusCode;
    private final String statusMessage;

    private final HttpHeader httpHeader;
    private final byte[] contents;

    private HttpResponse(int statusCode, String statusMessage, HttpHeader httpHeader, String bodyMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.httpHeader = httpHeader;
        this.contents = bodyMessage.getBytes();
    }

    private HttpResponse(int statusCode, String statusMessage, HttpHeader httpHeader, byte[] fileData) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.httpHeader = httpHeader;
        this.contents = fileData;
    }

    public static HttpResponse createOkResponse(String bodyMessage){
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Content-Type", ContentTypeMapping.getContentType(".html"));
        httpHeader.add("Content-Length", bodyMessage.getBytes().length +"");
        return new HttpResponse(200, "OK", httpHeader, bodyMessage);
    }


    public static HttpResponse createOkResponse(String fileExtension, byte[] fileData){
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Content-Type", ContentTypeMapping.getContentType(fileExtension));
        httpHeader.add("Content-Length", fileData.length+"");
        return new HttpResponse(200, "OK", httpHeader, fileData);
    }

    public static HttpResponse createRedirectResponse(){
        String bodyMessage = "redirect";
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Location", "/index");
        return new HttpResponse(303, "See Other", httpHeader, bodyMessage);
    }

    public static HttpResponse createNotFoundResponse(){
        String bodyMessage = "Not Found";
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Content-Type", ContentTypeMapping.getContentType(".html"));
        httpHeader.add("Content-Length", bodyMessage.getBytes().length +"");
        return new HttpResponse(404, "NOT FOUND", httpHeader, bodyMessage);
    }

    public byte[] getContents() {
        return contents;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }


    public Set<String> getHeaderKeys(){
        return httpHeader.getKeys();
    }

    public String getHeaderValue(String key){
        return httpHeader.getValue(key);
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "statusCode=" + statusCode +
                ", statusMessage='" + statusMessage + '\'' +
                '}';
    }
}
