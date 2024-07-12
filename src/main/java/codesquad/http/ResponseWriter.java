package codesquad.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseWriter {

    private final Logger log = LoggerFactory.getLogger(ResponseWriter.class);

    private final OutputStream outputStream;

    public ResponseWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(HttpResponse httpResponse) {
        try {
            outputStream.write(httpResponse.toBytes());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
