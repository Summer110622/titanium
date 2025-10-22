package com.example.titanium.performance;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class PerformanceService {
    public void measureStartupTime(File paperJarFile) throws IOException, InterruptedException {
        System.out.println("Measuring startup time for: " + paperJarFile.getAbsolutePath());

        // Ensure eula.txt exists and is set to true
        File eula = new File(paperJarFile.getParentFile(), "eula.txt");
        if (!eula.exists()) {
            try {
                eula.createNewFile();
                java.nio.file.Files.write(eula.toPath(), "eula=true".getBytes());
            } catch (IOException e) {
                System.err.println("Failed to create or write to eula.txt. Server startup may fail.");
                e.printStackTrace();
            }
        }


        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", paperJarFile.getAbsolutePath(), "nogui");
        processBuilder.directory(paperJarFile.getParentFile());

        long startTime = System.currentTimeMillis();
        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Print server output for debugging
                if (line.contains("Done")) {
                    break;
                }
            }
        }

        long endTime = System.currentTimeMillis();
        long startupTime = endTime - startTime;

        System.out.println("Server startup time: " + startupTime + " ms");

        process.destroy();
        process.waitFor();
    }
}
