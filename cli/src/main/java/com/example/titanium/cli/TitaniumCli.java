package com.example.titanium.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = "titanium",
    mixinStandardHelpOptions = true,
    version = "Titanium 0.1",
    description = "A CUI tool to patch and customize PaperMC.",
    subcommands = {
        PatchCommand.class,
        PerformanceCommand.class
    }
)
public class TitaniumCli implements Runnable {

    @Override
    public void run() {
        // Root command logic goes here.
        // For now, it just shows help if no subcommand is given.
        CommandLine.usage(this, System.out);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new TitaniumCli()).execute(args);
        System.exit(exitCode);
    }
}
