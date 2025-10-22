package com.example.titanium.patcher;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Map;

public class PatcherService {

    private final JarFileModifier jarFileModifier;

    public PatcherService() {
        this.jarFileModifier = new JarFileModifier();
    }

    public void processJarFile(File jarFile, String newServerName) throws IOException {
        System.out.println("Processing JAR file: " + jarFile.getAbsolutePath());
        validateJarFile(jarFile);
        System.out.println("File validation successful.");

        if (newServerName != null && !newServerName.isEmpty()) {
            changeServerName(jarFile, newServerName);
        }

        // Other processing logic can be added here
    }

    private void validateJarFile(File jarFile) {
        if (!jarFile.exists()) {
            throw new IllegalArgumentException("Error: The specified file does not exist: " + jarFile.getPath());
        }
        if (!jarFile.isFile()) {
            throw new IllegalArgumentException("Error: The specified path is not a file: " + jarFile.getPath());
        }
        if (!jarFile.getName().toLowerCase().endsWith(".jar")) {
            throw new IllegalArgumentException("Error: The specified file is not a .jar file: " + jarFile.getPath());
        }
    }

    private void changeServerName(File jarFile, String newServerName) throws IOException {
        final String spigotYmlPath = "spigot.yml";
        System.out.println("Attempting to change server name to: " + newServerName);

        try {
            String originalContent = jarFileModifier.readEntry(jarFile.toPath(), spigotYmlPath);

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);

            Map<String, Object> yamlData = yaml.load(originalContent);

            // Assuming the structure is settings -> server-name
            if (yamlData.containsKey("settings")) {
                Object settingsObj = yamlData.get("settings");
                if (settingsObj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> settingsMap = (Map<String, Object>) settingsObj;
                    settingsMap.put("server-name", newServerName);
                    System.out.println("Found 'settings' map, setting 'server-name'.");
                }
            } else {
                 System.out.println("Warning: 'settings' section not found in " + spigotYmlPath);
            }

            String newContent = yaml.dump(yamlData);
            jarFileModifier.writeEntry(jarFile.toPath(), spigotYmlPath, newContent);
            System.out.println("Successfully updated " + spigotYmlPath);

        } catch (NoSuchFileException e) {
            System.out.println("Info: " + spigotYmlPath + " not found in the JAR. Skipping server name change.");
        }
    }
}
