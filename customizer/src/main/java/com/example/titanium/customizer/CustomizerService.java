package com.example.titanium.customizer;

import java.io.File;
import java.io.IOException;

public class CustomizerService {

    private final JavaFileInjector javaFileInjector;

    public CustomizerService() {
        this.javaFileInjector = new JavaFileInjector();
    }

    public void addCustomApi(File jarFile, File customApiDir) {
        if (customApiDir == null || !customApiDir.isDirectory()) {
            // Do nothing if the directory is not specified or doesn't exist.
            return;
        }

        try {
            javaFileInjector.injectJavaFiles(jarFile.toPath(), customApiDir.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to add custom API files to JAR", e);
        }
    }
}
