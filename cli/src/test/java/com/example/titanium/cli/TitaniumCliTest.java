package com.example.titanium.cli;

import com.example.titanium.patcher.JarFileModifier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TitaniumCliTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private CommandLine cmd;

    @TempDir
    Path tempDir;
    private File dummyJar;
    private File dummyTxt;
    private JarFileModifier jarModifier = new JarFileModifier();

    @BeforeEach
    public void setUp() throws IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        cmd = new CommandLine(new TitaniumCli());

        // Create a dummy JAR with a spigot.yml file inside
        dummyJar = tempDir.resolve("test-paper.jar").toFile();
        String initialSpigotYml = "settings:\n  server-name: 'A Minecraft Server'\n";
        // Use a helper to create a zip/jar file for testing
        try (var fs = new JarFileModifier().createZipFileSystem(dummyJar.toPath(), true)) {
            Path spigotYmlPath = fs.getPath("spigot.yml");
            Files.writeString(spigotYmlPath, initialSpigotYml);
        }

        dummyTxt = Files.createFile(tempDir.resolve("not-a-jar.txt")).toFile();
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void whenPatchCommandIsCalledWithNewName_thenSpigotYmlIsUpdated() throws IOException {
        int exitCode = cmd.execute("patch", dummyJar.getAbsolutePath(), "--new-name", "My Custom Server");
        String output = outContent.toString();

        assertEquals(0, exitCode);
        assertTrue(output.contains("File validation successful."));
        assertTrue(output.contains("Attempting to change server name to: My Custom Server"));
        assertTrue(output.contains("Successfully updated spigot.yml"));

        // Verify the content of spigot.yml inside the jar
        String updatedSpigotYml = jarModifier.readEntry(dummyJar.toPath(), "spigot.yml");
        assertTrue(updatedSpigotYml.contains("server-name: My Custom Server"));
        assertFalse(updatedSpigotYml.contains("A Minecraft Server"));
    }

    @Test
    void whenPatchCommandIsCalledWithValidJar_thenSuccess() {
        int exitCode = cmd.execute("patch", dummyJar.getAbsolutePath());
        String output = outContent.toString();

        assertEquals(0, exitCode);
        assertTrue(output.contains("Processing JAR file: " + dummyJar.getAbsolutePath()));
        assertTrue(output.contains("File validation successful."));
    }

    @Test
    void whenPatchCommandIsCalledWithNonExistentFile_thenFails() {
        File nonExistentFile = new File(tempDir.toFile(), "non-existent.jar");
        int exitCode = cmd.execute("patch", nonExistentFile.getAbsolutePath());

        assertNotEquals(0, exitCode);
        assertTrue(errContent.toString().contains("Error: The specified file does not exist"));
    }

    @Test
    void whenPatchCommandIsCalledWithInvalidExtension_thenFails() {
        int exitCode = cmd.execute("patch", dummyTxt.getAbsolutePath());

        assertNotEquals(0, exitCode);
        assertTrue(errContent.toString().contains("Error: The specified file is not a .jar file"));
    }
}
