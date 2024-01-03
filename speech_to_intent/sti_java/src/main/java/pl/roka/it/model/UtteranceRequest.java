package pl.roka.it.model;

public class UtteranceRequest {

    public static IntentRequest getIntentRequest(String kind, String id,
                                          String participantId,
                                          String text,
                                          String modality,
                                          String projectName,
                                          String deploymentName,
                                          String stringIndexType) {
        return new IntentRequest(kind,
                new AnalysisInput(new ConversationItem(id, participantId, text, modality)),
                new Parameters(projectName, deploymentName, stringIndexType));
    }

    public record IntentRequest(String kind, AnalysisInput analysisInput, Parameters parameters) {

    }

    record AnalysisInput(ConversationItem conversationItem) {
    }

    record ConversationItem(String id, String participantId, String text, String modality) {
    }

    record Parameters(String projectName, String deploymentName, String stringIndexType) {
    }
}
