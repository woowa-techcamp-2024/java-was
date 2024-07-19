package codesquad.helper;

import codesquad.configuration.TestDatabaseConfiguration;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestDatabaseExtension implements BeforeAllCallback {

    private static boolean initialized = false;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        if (!initialized) {
            TestDatabaseConfiguration.initializeDatabase();
            initialized = true;
        }
    }
    
}
