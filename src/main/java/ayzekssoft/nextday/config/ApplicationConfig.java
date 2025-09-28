package ayzekssoft.nextday.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

public class ApplicationConfig {
    private static final String PROPERTIES_RESOURCE = "/application.properties";

    private static final Properties properties = new Properties();

    static {
        try (InputStream in = ApplicationConfig.class.getResourceAsStream(PROPERTIES_RESOURCE)) {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
    }

    public static String getDatabaseUrl() {
        return Objects.requireNonNull(properties.getProperty("db.url"), "db.url is required");
    }

    public static String getDatabaseUser() {
        return Objects.requireNonNull(properties.getProperty("db.user"), "db.user is required");
    }

    public static String getDatabasePassword() {
        return Objects.requireNonNull(properties.getProperty("db.password"), "db.password is required");
    }

    public static Path getStorageDirectory() {
        String dir = properties.getProperty("app.storageDir", ".nextday");
        Path home = Paths.get(System.getProperty("user.home"));
        Path storage = home.resolve(dir);
        try {
            Files.createDirectories(storage);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create storage directory: " + storage, e);
        }
        return storage;
    }

    public static String getDatabaseName() {
        String url = getDatabaseUrl();
        int q = url.indexOf('?');
        String base = q >= 0 ? url.substring(0, q) : url;
        int idx = base.lastIndexOf('/');
        if (idx < 0 || idx + 1 >= base.length()) {
            throw new IllegalArgumentException("db.url must include database name: " + url);
        }
        return base.substring(idx + 1);
    }

    public static String getAdminDatabaseUrl() {
        String url = getDatabaseUrl();
        int q = url.indexOf('?');
        String suffix = q >= 0 ? url.substring(q) : "";
        String base = q >= 0 ? url.substring(0, q) : url;
        int idx = base.lastIndexOf('/');
        if (idx < 0) return url; // fallback
        return base.substring(0, idx + 1) + "postgres" + suffix;
    }
}
