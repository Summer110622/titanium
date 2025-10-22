package com.example.titanium.cli;

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

    @BeforeEach
    public void setUp() throws IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        cmd = new CommandLine(new TitaniumCli());

        // Create dummy files for testing
        dummyJar = Files.createFile(tempDir.resolve("test-paper.jar")).toFile();
        dummyTxt = Files.createFile(tempDir.resolve("not-a-jar.txt")).toFile();
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
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
