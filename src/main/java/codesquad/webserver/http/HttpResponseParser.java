package codesquad.webserver.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseParser {
    public static void parse(OutputStream outputStream, HttpResponse response){
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        try{
            writeStartLine(dataOutputStream, response);
            writeHeader(dataOutputStream, response);
            writeContent(response, dataOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeStartLine(DataOutputStream dataOutputStream, HttpResponse response) throws IOException {
        dataOutputStream.writeBytes("HTTP/1.1"+" "+response.getStatusCode()+" "+response.getStatusMessage()+"\r\n");
    }

    private static void writeHeader(DataOutputStream dataOutputStream, HttpResponse response) throws IOException {
        for(String key : response.getHeaderKeys()){
            dataOutputStream.writeBytes(key + ": "+response.getHeaderValue(key)+"\r\n");
        }
        writeEmptyLine(dataOutputStream);
    }

    private static void writeEmptyLine(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeBytes("\r\n");
    }

    private static void writeContent(HttpResponse response, DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.write(response.getContents(), 0, response.getContents().length);
    }
}
