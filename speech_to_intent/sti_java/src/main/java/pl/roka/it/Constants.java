package pl.roka.it;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    private static final Map<String, String> envVars = new HashMap<>();
    public static final String noMatch = "NoMatch";
    public static final String categoryProgram = "program";
    public static final String endpoint = "https://ailanguagelearn.cognitiveservices.azure.com";
    public static final String apiVersion = "2023-04-01";
    public static final String projectName = "conversationlangunderstanding";
    public static final String deploymentName = "jarvis-assistant-deployment";
    public static final String stringIndexType = "TextElement_V8";

    public static String getAzureRegion() {
        String AZURE_REGION = "AZURE_REGION";
        return envVars.computeIfAbsent(AZURE_REGION, k -> System.getenv(AZURE_REGION));
    }

    public static String getLanguageResourceKey() {
        String AZURE_LANG_KEY = "LANGUAGE_RESOURCE_KEY";
        return envVars.computeIfAbsent(AZURE_LANG_KEY, k -> System.getenv(AZURE_LANG_KEY));
    }

    public static String getSpeechResourceKey() {
        String AZURE_SPEECH_KEY = "SPEECH_RESOURCE_KEY";
        return envVars.computeIfAbsent(AZURE_SPEECH_KEY, k -> System.getenv(AZURE_SPEECH_KEY));
    }

    public static String getDeploymentName() {
        String INTENT_ASSISTANT_DEPLOYMENT_NAME = "AZURE_DEPLOYMENT_NAME";
        return envVars.computeIfAbsent(INTENT_ASSISTANT_DEPLOYMENT_NAME, k -> System.getenv(INTENT_ASSISTANT_DEPLOYMENT_NAME));
    }
}
