package codesquad.handler;

import codesquad.database.PostH2Database;
import codesquad.database.SessionDatabase;
import codesquad.file.FileReader;
import codesquad.http.HttpStatus;
import codesquad.http.request.HttpRequest;
import codesquad.http.response.HttpResponse;
import codesquad.model.Post;
import codesquad.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

public class WritePageHandler implements Handler {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final static SessionDatabase sessionDatabase = new SessionDatabase();
    private final static PostH2Database postH2Database = new PostH2Database();

    @Override
    public HttpResponse handle(HttpRequest request) throws RuntimeException {
        if (request.method().equals("GET")) {
            return doGet(request);
        }
        if (request.method().equals("POST")) {
            try {
                return doPost(request);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new HttpResponse(HttpStatus.METHOD_NOT_ALLOWED, "text", new byte[0]);
    }


    public HttpResponse doGet(HttpRequest request) throws RuntimeException {
        byte[] content = FileReader.getContent("/write/write.html");
        String s = new String(content);
        Optional<String> cookie = request.getCookie();
        if(cookie.isPresent()) {
            Optional<User> userOptional = sessionDatabase.findUserBySessionId(cookie.get().split("=")[1]);
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                s = s.replace("{{username}}", user.getName());
            }
        }
        return new HttpResponse(HttpStatus.OK, "html", s.getBytes());
    }


    public HttpResponse doPost(HttpRequest request) throws IOException {
        log.info(request.toString());
        Optional<String> cookie = request.getCookie();
        if(cookie.isPresent()) {
            Optional<User> userOptional = sessionDatabase.findUserBySessionId(cookie.get().split("=")[1]);
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                postH2Database.addPost(new Post(user, parseFormData(request)));

            }
        }

        return new HttpResponse("/");
    }

    private String parseFormData(HttpRequest request) throws IOException {
//        Map<String, Object> formData = new HashMap<>();
//
//        String contentType = request.headers().get("Content-Type");
//        String boundary = contentType.split("boundary=")[1];
//        byte[] body = request.body();
        byte[] body = request.body();
        String content = new String(body);
        return URLDecoder.decode(content.split("=")[1], "UTF-8");
    }

//    public static void parseMultipartData(byte[] body, String boundary) throws IOException {
//        String boundaryString = "--" + boundary;
//        byte[] boundaryBytes = boundaryString.getBytes();
//
//        int pos = 0;
//        while ((pos = indexOf(body, boundaryBytes, pos)) != -1) {
//            int nextBoundaryPos = indexOf(body, boundaryBytes, pos + boundaryBytes.length);
//            if (nextBoundaryPos == -1) {
//                break;
//            }
//
//            int start = pos + boundaryBytes.length + 2; // skip boundary and CRLF
//            int end = nextBoundaryPos - 2; // skip CRLF before next boundary
//            byte[] part = Arrays.copyOfRange(body, start, end);
//
//            parsePart(part);
//
//            pos = nextBoundaryPos;
//        }
//    }
//
//    private static void parsePart(byte[] part) throws IOException {
//        String partAsString = new String(part);
//        String[] headersAndBody = partAsString.split("\r\n\r\n", 2);
//        if (headersAndBody.length < 2) {
//            return;
//        }
//
//        String headers = headersAndBody[0];
//        byte[] body = headersAndBody[1].getBytes();
//
//        String[] headerLines = headers.split("\r\n");
//        String contentDisposition = null;
//        String contentType = null;
//        for (String headerLine : headerLines) {
//            if (headerLine.startsWith("Content-Disposition")) {
//                contentDisposition = headerLine;
//            } else if (headerLine.startsWith("Content-Type")) {
//                contentType = headerLine;
//            }
//        }
//
//        if (contentDisposition != null && contentDisposition.contains("filename")) {
//            String filename = contentDisposition.split("filename=")[1].replaceAll("\"", "");
//            saveFile(body, filename);
//        } else if (contentDisposition != null && contentDisposition.contains("name=\"title\"")) {
//            String title = new String(body);
//            System.out.println("Title: " + title.trim());
//        } else if (contentDisposition != null && contentDisposition.contains("name=\"content\"")) {
//            String content = new String(body);
//            System.out.println("Content: " + content.trim());
//        }
//    }
//
//    private static void saveFile(byte[] fileData, String filename) throws IOException {
//        try (FileOutputStream fos = new FileOutputStream(filename)) {
//            fos.write(fileData);
//        }
//        System.out.println("File saved as: " + filename);
//    }
//
//    private static int indexOf(byte[] outerArray, byte[] smallerArray, int start) {
//        for (int i = start; i < outerArray.length - smallerArray.length + 1; ++i) {
//            boolean found = true;
//            for (int j = 0; j < smallerArray.length; ++j) {
//                if (outerArray[i + j] != smallerArray[j]) {
//                    found = false;
//                    break;
//                }
//            }
//            if (found) {
//                return i;
//            }
//        }
//        return -1;
//    }

}