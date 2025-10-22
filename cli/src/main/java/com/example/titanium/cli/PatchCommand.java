package com.example.titanium.cli;

import com.example.titanium.patcher.PatcherService;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(
    name = "patch",
    description = "Applies patches to a specified PaperMC version."
)
public class PatchCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "The Minecraft version to patch (e.g., 1.20.1).")
    private String minecraftVersion;

    private final PatcherService patcherService;

    public PatchCommand() {
        this.patcherService = new PatcherService();
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("Patching version: " + minecraftVersion);
        patcherService.applyPatches(minecraftVersion);
        return 0;
    }
}
