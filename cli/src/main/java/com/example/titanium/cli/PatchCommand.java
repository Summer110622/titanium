package com.example.titanium.cli;

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

    @Override
    public Integer call() throws Exception {
        System.out.println("Patching version: " + minecraftVersion);
        // Actual patching logic will be implemented later.
        return 0;
    }
}
