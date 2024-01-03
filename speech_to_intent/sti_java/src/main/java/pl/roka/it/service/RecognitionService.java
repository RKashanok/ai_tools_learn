package pl.roka.it.service;

import com.google.gson.Gson;
import com.microsoft.cognitiveservices.speech.CancellationDetails;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import pl.roka.it.model.IntentResponse;
import pl.roka.it.model.UtteranceRequest;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static pl.roka.it.Constants.*;

public class RecognitionService {

    private final SpeechConfig speechConfig;
    private final AudioConfig audioConfig;
    private final IntentService intentService;

    private final Gson gson = new Gson();
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public RecognitionService(SpeechConfig speechConfig, AudioConfig audioConfig, IntentService intentService) {
        this.speechConfig = speechConfig;
        this.audioConfig = audioConfig;
        this.intentService = intentService;
    }

    public boolean recognizeFromMicrophone() {
        boolean result = true;
        try (SpeechRecognizer recognizer = new SpeechRecognizer(speechConfig, audioConfig)) {
            Future<SpeechRecognitionResult> task = recognizer.recognizeOnceAsync();
            System.out.println("I'm listening...");
            SpeechRecognitionResult speechRecognitionResult = task.get();
            switch (speechRecognitionResult.getReason()) {
                case RecognizedSpeech -> {
                    String text = speechRecognitionResult.getText();
                    System.out.println("That's what I've heard: " + text);
                    result = !text.toLowerCase().contains("stop recognition".toLowerCase());
                    if (result) {
                        new Thread(() -> intentRecognition(text)).start();
                    }
                }
                case NoMatch -> System.out.println("NOMATCH: Speech could not be recognized.");
                case Canceled -> {
                    CancellationDetails cancellation = CancellationDetails.fromResult(speechRecognitionResult);
                    System.out.println("CANCELED: Reason=" + cancellation.getReason());
                }
                default -> System.out.println("Recognition failed with reason: " + speechRecognitionResult.getReason());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void intentRecognition(String intentText) {
        try {
            HttpResponse response = getIntentRecognitionResult(intentText);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                System.out.println("Request was successful.");
                String responseBody = EntityUtils.toString(response.getEntity());
                IntentResponse.ConversationResult result = gson.fromJson(responseBody, IntentResponse.ConversationResult.class);
                result.result().prediction().entities().forEach(entity -> {
                    String category = entity.category();
                    if (category.equalsIgnoreCase(categoryProgram)) {
                        intentService.processIntent(result.result().prediction().topIntent(), entity);
                    } else {
                        // TODO implement some default behaviour
                    }
                });
            } else {
                System.out.println("Request failed with status code " + statusCode);
                String responseBody = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HttpResponse getIntentRecognitionResult(String intentText) throws IOException {
        HttpPost request = new HttpPost(endpoint + "/language/:analyze-conversations?api-version=" + apiVersion);
        String subsKeyHeader = "Ocp-Apim-Subscription-Key";
        request.setHeader(subsKeyHeader, getLanguageResourceKey());
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        UtteranceRequest.IntentRequest payload = createUttRequest(intentText);
        request.setEntity(new StringEntity(gson.toJson(payload)));

        return httpClient.execute(request);
    }

    private static UtteranceRequest.IntentRequest createUttRequest(String intentText) {
        String uuid = UUID.randomUUID().toString();
        return UtteranceRequest.getIntentRequest("Conversation", uuid, uuid, intentText, "text",
                projectName, getDeploymentName(), stringIndexType);
    }
}
