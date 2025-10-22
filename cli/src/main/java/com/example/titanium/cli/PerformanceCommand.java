package com.example.titanium.cli;

import com.example.titanium.performance.PerformanceService;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.concurrent.Callable;

@Command(
    name = "performance",
    description = "Measures the performance of a specified PaperMC server JAR file."
)
public class PerformanceCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "The path to the PaperMC server JAR file.", arity = "1")
    private File paperJarFile;

    private final PerformanceService performanceService;

    public PerformanceCommand() {
        this.performanceService = new PerformanceService();
    }

    // Constructor for testing
    public PerformanceCommand(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @Override
    public Integer call() throws Exception {
        performanceService.measureStartupTime(paperJarFile);
        return 0;
    }
}
