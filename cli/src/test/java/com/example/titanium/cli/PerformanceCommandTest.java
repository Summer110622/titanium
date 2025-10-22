package com.example.titanium.cli;

import com.example.titanium.performance.PerformanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import picocli.CommandLine;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PerformanceCommandTest {

    @Mock
    private PerformanceService performanceService;

    private PerformanceCommand performanceCommand;

    @org.junit.jupiter.api.BeforeEach
    public void setUp() {
        performanceCommand = new PerformanceCommand(performanceService);
    }

    @Test
    public void testPerformanceCommand() throws Exception {
        File fakeJar = new File("fake.jar");
        try {
            fakeJar.createNewFile();
            new CommandLine(performanceCommand).execute(fakeJar.getAbsolutePath());

            ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
            verify(performanceService).measureStartupTime(fileCaptor.capture());

            assertEquals(fakeJar.getName(), fileCaptor.getValue().getName());
        } finally {
            if (fakeJar.exists()) {
                fakeJar.delete();
            }
        }
    }
}
