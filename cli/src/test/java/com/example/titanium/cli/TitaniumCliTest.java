package com.example.titanium.cli;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TitaniumCliTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private CommandLine cmd;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        cmd = new CommandLine(new TitaniumCli());
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void whenPatchCommandIsCalled_thenSuccess() {
        int exitCode = cmd.execute("patch", "1.20.1");
        assertEquals(0, exitCode);
        assertTrue(outContent.toString().contains("Patching version: 1.20.1"));
    }
}
