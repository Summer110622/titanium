package com.example.titanium.patcher;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class JarFileModifier {

    /**
     * Reads the content of a specific entry (file) within a JAR file.
     *
     * @param jarPath   The path to the JAR file.
     * @param entryPath The path to the entry within the JAR (e.g., "version.json").
     * @return The content of the entry as a String.
     * @throws IOException if an I/O error occurs.
     */
    public String readEntry(Path jarPath, String entryPath) throws IOException {
        try (FileSystem fs = createZipFileSystem(jarPath, false)) {
            Path pathInJar = fs.getPath(entryPath);
            if (!Files.exists(pathInJar)) {
                throw new NoSuchFileException("Entry not found in JAR: " + entryPath);
            }
            return new String(Files.readAllBytes(pathInJar));
        }
    }

    /**
     * Writes or overwrites the content of a specific entry (file) within a JAR file.
     *
     * @param jarPath   The path to the JAR file.
     * @param entryPath The path to the entry within the JAR (e.g., "version.json").
     * @param content   The new content to write to the entry.
     * @throws IOException if an I/O error occurs.
     */
    public void writeEntry(Path jarPath, String entryPath, String content) throws IOException {
        try (FileSystem fs = createZipFileSystem(jarPath, false)) {
            Path pathInJar = fs.getPath(entryPath);
            Files.write(pathInJar, content.getBytes());
        }
    }

    /**
     * Creates a Zip File System for the given JAR path.
     *
     * @param jarPath The path to the JAR file.
     * @param create  Whether to create the file if it doesn't exist.
     * @return A new FileSystem instance.
     * @throws IOException if an I/O error occurs.
     */
    public FileSystem createZipFileSystem(Path jarPath, boolean create) throws IOException {
        final URI uri = URI.create("jar:" + jarPath.toUri());
        final Map<String, String> env = new HashMap<>();
        if (create) {
            env.put("create", "true");
        }
        return FileSystems.newFileSystem(uri, env);
    }
}
