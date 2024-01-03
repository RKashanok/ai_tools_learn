package pl.roka.it.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class IntentResponse {

    public record ConversationResult(Result result) {
    }

    public record Result(String query, Prediction prediction) {
    }

    public record Prediction(@SerializedName("topIntent") String topIntent,
                             @SerializedName("projectKind") String projectKind, List<Intent> intents,
                             List<Entity> entities) {
    }

    public record Intent(@SerializedName("category") String category,
                         @SerializedName("confidenceScore") double confidenceScore) {
    }

    public record Entity(@SerializedName("category") String category, String text, int offset, int length,
                         @SerializedName("confidenceScore") double confidenceScore) {
    }
}