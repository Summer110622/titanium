package com.example.titanium.cli;

import com.example.titanium.customizer.CustomizerService;
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

    @Option(names = {"--custom-api-dir"}, description = "Directory containing custom .java files to add to the JAR.")
    private File customApiDir;

    private final PatcherService patcherService;
    private final CustomizerService customizerService;

    public PatchCommand() {
        this.patcherService = new PatcherService();
        this.customizerService = new CustomizerService();
    }

    @Override
    public Integer call() throws Exception {
        // PatcherService handles initial validation and server name change
        patcherService.processJarFile(paperJarFile, newServerName);

        // CustomizerService handles adding custom API files
        customizerService.addCustomApi(paperJarFile, customApiDir);

        return 0;
    }
}
