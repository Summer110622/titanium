package com.example.titanium.gui;

import com.example.titanium.patcher.PatcherService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class MainController {

    @FXML
    private Button patchButton;

    @FXML
    private TextArea outputArea;

    private final PatcherService patcherService = new PatcherService();

    @FXML
    public void initialize() {
        patchButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open JAR File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JAR Files", "*.jar"));
            File selectedFile = fileChooser.showOpenDialog(patchButton.getScene().getWindow());

            if (selectedFile != null) {
                TextInputDialog dialog = new TextInputDialog("MyCustomServer");
                dialog.setTitle("Server Name");
                dialog.setHeaderText("Enter the new server name.");
                dialog.setContentText("Server name:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(serverName -> {
                    try {
                        outputArea.appendText("Processing JAR file: " + selectedFile.getAbsolutePath() + "\\n");
                        patcherService.processJarFile(selectedFile, serverName);
                        outputArea.appendText("Successfully applied patch!\\n");
                    } catch (IOException e) {
                        outputArea.appendText("Error processing JAR file: " + e.getMessage() + "\\n");
                    }
                });
            }
        });
    }
}
