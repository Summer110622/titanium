package com.example.titanium.cli;

import com.example.titanium.patcher.PatcherService;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.concurrent.Callable;

@Command(
    name = "patch",
    description = "Applies patches to a specified PaperMC server JAR file."
)
public class PatchCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "The path to the PaperMC server JAR file.", arity = "1")
    private File paperJarFile;

    private final PatcherService patcherService;

    public PatchCommand() {
        this.patcherService = new PatcherService();
    }

    @Override
    public Integer call() throws Exception {
        // The service will now handle the file logic
        patcherService.processJarFile(paperJarFile);
        return 0;
    }
}
