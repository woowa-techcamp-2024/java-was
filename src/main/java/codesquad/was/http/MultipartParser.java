package codesquad.was.http;

import static codesquad.was.http.type.CharsetType.UTF_8;

import codesquad.utils.StringUtils;
import codesquad.was.http.type.MimeType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipartParser {

    private static final String CRLF = "\r\n";
    private static final byte CR = 13;
    private static final String DASH = "--";
    private static final int ONLY_FORM_DATA_SIZE = 1;
    private static final int MIME_DATA_SIZE = 2;

    private final Logger log = LoggerFactory.getLogger(MultipartParser.class);

    private final byte[] bodyData;
    private int offset;
    private final String splitBoundary;
    private final String endBoundary;
    private final Map<String, String> formParameters = new HashMap<>();
    private MultipartFile multipartFile;

    public MultipartParser(final byte[] bodyData, final List<String> mimeTypeValues) throws IOException {
        this.bodyData = bodyData;
        String boundarySymbol = getBoundarySymbol(mimeTypeValues);
        splitBoundary = DASH + boundarySymbol;
        endBoundary = DASH + boundarySymbol + DASH;

        while (!(new String(readUntilSymbol(CR), UTF_8.getCharset())).equals(endBoundary)) {
            Map<String, String> contentDispositionValues = readContentDisposition();

            if (contentDispositionValues.size() == ONLY_FORM_DATA_SIZE) {
                skipByte(2);
                putFormParameters(contentDispositionValues);
            }
            if (contentDispositionValues.size() == MIME_DATA_SIZE) {
                MimeType mimeType = getMimeType(new String(readUntilSymbol(CR), UTF_8.getCharset()));

                if (isEmptyMultipart(mimeType, contentDispositionValues)) {
                    skipByte(4);
                    continue;
                }
                skipByte(2);
                multipartFile = parseMultipartFile(contentDispositionValues, mimeType);
            }
        }
    }

    private String getBoundarySymbol(final List<String> mimeTypeValues) {
        return mimeTypeValues.get(1).split("=")[1];
    }

    private Map<String, String> readContentDisposition() throws UnsupportedEncodingException {
        return getContentDispositions(
                new String(readUntilSymbol(CR), UTF_8.getCharset()));
    }

    private byte[] readUntilSymbol(final byte symbol) {
        int count = countLine(symbol);
        byte[] bytes = new byte[count];

        for (int i = offset; i < offset + count; i++) {
            bytes[i - offset] = bodyData[i];
        }
        offset += count;

        skipByte(2);

        return bytes;
    }

    private byte[] readUntilSymbol(final String symbol) throws UnsupportedEncodingException {
        byte[] symbolBytes = symbol.getBytes(UTF_8.getCharset());
        int start = offset;
        int end = offset;
        while (!isEndOfBoundary(symbolBytes, end)) {
            end++;
        }
        byte[] findBytes = new byte[end - start - symbolBytes.length];
        offset = end - symbolBytes.length;
        System.arraycopy(bodyData, start, findBytes, 0, end - start - symbolBytes.length);

        skipByte(2);

        return findBytes;
    }

    private void putFormParameters(final Map<String, String> contentDispositionValues) {
        String formKey = contentDispositionValues.get("name");
        String formValue = new String(readUntilSymbol(CR));

        formParameters.put(formKey, formValue);
    }

    private boolean isEmptyMultipart(final MimeType mimeType, final Map<String, String> contentDispositionValues) {
        return mimeType == MimeType.APPLICATION_OCTET_STREAM || contentDispositionValues.get("filename")
                .isEmpty();
    }

    private MultipartFile parseMultipartFile(final Map<String, String> contentDispositionValues,
                                             final MimeType mimeType) throws UnsupportedEncodingException {
        String filename = contentDispositionValues.get("filename");

        byte[] multipartBytes = readUntilSymbol(CRLF + splitBoundary);
        log.debug("read multipart file byte = {}", multipartBytes.length);

        return new MultipartFile(
                filename,
                StringUtils.getFilenameExtension(filename),
                multipartBytes,
                mimeType
        );
    }

    private boolean isEndOfBoundary(final byte[] symbolBytes, final int end) {
        if (end - offset < symbolBytes.length) {
            return false;
        }
        byte[] bytes = new byte[symbolBytes.length];
        for (int i = end - symbolBytes.length; i < end; i++) {
            bytes[i - (end - symbolBytes.length)] = bodyData[i];
        }

        for (int i = 0; i < symbolBytes.length; i++) {
            if (symbolBytes[i] != bytes[i]) {
                return false;
            }
        }
        return true;
    }

    private int countLine(final byte specificByte) {
        int count = 0;
        for (int i = offset; i < bodyData.length; i++) {
            if (bodyData[i] == specificByte) {
                break;
            }
            count++;
        }
        return count;
    }

    private void skipByte(int count) {
        offset += count;
    }

    private Map<String, String> getContentDispositions(final String header) {
        String wholeValue = header.split(":")[1];
        String[] values = wholeValue.split(";");
        String[] onlyValues = Arrays.copyOfRange(values, 1, values.length);

        return Arrays.stream(onlyValues)
                .map(onlyValue -> onlyValue.split("="))
                .collect(Collectors.toMap(
                        split -> split[0].trim(),
                        split -> split[1].trim().substring(1, split[1].trim().length() - 1)
                ));
    }

    private MimeType getMimeType(final String contentAndMimeType) {
        String[] contentAndMimeTypes = contentAndMimeType.split(":");

        return MimeType.findMimeType(contentAndMimeTypes[1].trim());
    }

    public Map<String, String> getFormParameters() {
        return formParameters;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }
}
