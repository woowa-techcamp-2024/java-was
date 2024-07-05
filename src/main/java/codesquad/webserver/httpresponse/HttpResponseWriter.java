package codesquad.webserver.httpresponse;

import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseWriter {
    public void writeResponse(OutputStream out, HttpResponse response) throws IOException {
        out.write(response.getByte());
        out.flush();
    }
}
