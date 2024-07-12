package codesquad.resource;

import java.util.Optional;

public class DirectoryIndexResolver {

    private static DirectoryIndexResolver instance;

    private DirectoryIndexResolver() {
    }

    public static DirectoryIndexResolver getInstance() {
        if (instance == null) {
            instance = new DirectoryIndexResolver();
        }
        return instance;
    }

    public Optional<Resource> resolve(String uri) {
        Optional<Resource> readResource = ResourcesReader.readResource(uri);
        if (readResource.isPresent()) {
            Resource resource = readResource.get();
            if (resource.isFile()) {
                return Optional.of(resource);
            }

            readResource = ResourcesReader.readResource(resource.getFileName() + "/index.html");
            if (readResource.isPresent()) {
                resource = readResource.get();
                return Optional.of(resource);
            }
        }
        return Optional.empty();
    }
}
