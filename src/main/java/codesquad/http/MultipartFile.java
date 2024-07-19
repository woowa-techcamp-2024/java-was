package codesquad.http;

public record MultipartFile(String name, String filename, String contentType, byte[] content) {
}
