package com.example.titanium.cli;

import com.example.titanium.patcher.PatcherService;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
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

    @Option(names = {"--new-name"}, description = "Set a new server name in spigot.yml.")
    private String newServerName;

    private final PatcherService patcherService;

    public PatchCommand() {
        this.patcherService = new PatcherService();
    }

    @Override
    public Integer call() throws Exception {
        patcherService.processJarFile(paperJarFile, newServerName);
        return 0;
    }
}
