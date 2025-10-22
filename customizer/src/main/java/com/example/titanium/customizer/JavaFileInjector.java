package com.example.titanium.customizer;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class JavaFileInjector {

    public void injectJavaFiles(Path jarPath, Path sourceDir) throws IOException {
        System.out.println("Injecting .java files from " + sourceDir + " into " + jarPath);

        try (FileSystem fs = createZipFileSystem(jarPath, false)) {
            Path targetInJar = fs.getPath("API");
            if (!Files.exists(targetInJar)) {
                Files.createDirectories(targetInJar);
            }

            try (Stream<Path> stream = Files.walk(sourceDir)) {
                stream.filter(path -> path.toString().endsWith(".java"))
                      .forEach(sourceFile -> {
                          try {
                              Path destFile = targetInJar.resolve(sourceDir.relativize(sourceFile).toString());
                              Files.copy(sourceFile, destFile, StandardCopyOption.REPLACE_EXISTING);
                              System.out.println("  - Injected: " + sourceFile.getFileName());
                          } catch (IOException e) {
                              throw new RuntimeException("Failed to inject file: " + sourceFile, e);
                          }
                      });
            }
        }
    }

    private FileSystem createZipFileSystem(Path zipPath, boolean create) throws IOException {
        final URI uri = URI.create("jar:" + zipPath.toUri());
        final Map<String, String> env = new HashMap<>();
        if (create) {
            env.put("create", "true");
        }
        return FileSystems.newFileSystem(uri, env);
    }
}
