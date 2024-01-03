package pl.roka.it.service;

import pl.roka.it.model.IntentResponse;
import pl.roka.it.model.Intents;

import java.io.IOException;

public class IntentService {

    public void processIntent(String intentText, IntentResponse.Entity entity) {
        switch (Intents.fromString(intentText)) {
            case PROGRAM_OPEN -> openProgram(entity.text());
            case PROGRAM_CLOSE -> closeProgram(entity.text());
            case TRACK_NEXT -> toNextTrack(entity.text());
            default -> defaultAction();
        }
    }

    private void openProgram(String appName) {
        String applicationName = "/Applications/" + capitalizeFirstLetter(appName) +".app";
        System.out.println("Opening application: " + applicationName);
        ProcessBuilder processBuilder = new ProcessBuilder("open", applicationName);
        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Application opened successfully!");
            } else {
                System.out.println("Error opening application. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("openProgram error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void closeProgram(String appName) {
        String applicationName = capitalizeFirstLetter(appName);
        System.out.println("Closing application: " + applicationName);
        String[] script = {
                "osascript",
                "-e",
                "tell application \"" + applicationName + "\" to quit"
        };

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(script);
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println("closeProgram error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void toNextTrack(String appName) {
        String applicationName = capitalizeFirstLetter(appName);
        System.out.println("Switching to next track in application: " + applicationName);
        String[] script = {
                "osascript",
                "-e",
                "tell application \"" + applicationName + "\" to next track"
        };

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(script);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Successfully sent next track command to Spotify.");
            } else {
                System.err.println("Error occurred while sending next track command to Spotify.");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("toNextTrack error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void defaultAction() {
        System.out.println("Sorry, I didn't understand that. Please try again.");
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
