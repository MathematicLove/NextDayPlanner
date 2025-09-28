package ayzekssoft.nextday.util;

import ayzekssoft.nextday.config.ApplicationConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class ImageStorage {
    public static Path storeImage(Path source) throws IOException {
        String ext = "";
        String name = source.getFileName().toString();
        int dot = name.lastIndexOf('.');
        if (dot > 0) ext = name.substring(dot);
        Path target = ApplicationConfig.getStorageDirectory().resolve("images").resolve(UUID.randomUUID() + ext);
        Files.createDirectories(target.getParent());
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        return target;
    }
}


