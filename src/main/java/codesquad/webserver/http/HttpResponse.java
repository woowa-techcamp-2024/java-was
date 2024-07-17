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

    public void setCookie(String sid){
        httpHeader.setValue("Set-Cookie", "sid="+sid +"; "+ "Path=/");
    }

    public void removeCookie(){
        httpHeader.setValue("Set-Cookie", "sid= ''; Path=/; Max-Age=0");
    }

    public static HttpResponse ok(String bodyMessage){
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Content-Type", ContentTypeMapping.getContentType(".html"));
        httpHeader.add("Content-Length", bodyMessage.getBytes().length +"");
        return new HttpResponse(200, "OK", httpHeader, bodyMessage);
    }


    public static HttpResponse ok(String fileExtension, byte[] fileData){
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Content-Type", ContentTypeMapping.getContentType(fileExtension));
        httpHeader.add("Content-Length", fileData.length+"");
        return new HttpResponse(200, "OK", httpHeader, fileData);
    }

    public static HttpResponse redirect(String path){
        String bodyMessage = "redirect";
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Location", path);
        return new HttpResponse(303, "See Other", httpHeader, bodyMessage);
    }

    public static HttpResponse notFound(){
        String bodyMessage = "Not Found";
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Content-Type", ContentTypeMapping.getContentType(".html"));
        httpHeader.add("Content-Length", bodyMessage.getBytes().length +"");
        return new HttpResponse(404, "NOT FOUND", httpHeader, bodyMessage);
    }

    public static HttpResponse methodNotAllowed(){
        String bodyMessage = "Method Not Allowed";
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Content-Type", ContentTypeMapping.getContentType(".html"));
        httpHeader.add("Content-Length", bodyMessage.getBytes().length +"");
        return new HttpResponse(405, "Method Not Allowed", httpHeader, bodyMessage);
    }

    public static HttpResponse badRequest(){
        String bodyMessage = "Bad Request";
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Content-Type", ContentTypeMapping.getContentType(".html"));
        httpHeader.add("Content-Length", bodyMessage.getBytes().length +"");
        return new HttpResponse(400, "Bad Request", httpHeader, bodyMessage);
    }

    public static HttpResponse serverError(){
        String bodyMessage = "Internal Server Error";
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Content-Type", ContentTypeMapping.getContentType(".html"));
        httpHeader.add("Content-Length", bodyMessage.getBytes().length +"");
        return new HttpResponse(500, "Internal Server Error", httpHeader, bodyMessage);
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
