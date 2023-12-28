package pl.roka.it;

import com.microsoft.cognitiveservices.speech.CancellationDetails;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        SpeechConfig speechConfig = SpeechConfig.fromSubscription(
                System.getenv("SPEECH_KEY"),
                System.getenv("SPEECH_REGION")
        );
        speechConfig.setSpeechRecognitionLanguage("en-US");
        boolean keepRunning = true;
        while (keepRunning) {
            keepRunning = recognizeFromMicrophone(speechConfig);
        }
    }

    public static boolean recognizeFromMicrophone(SpeechConfig speechConfig) {
        boolean result = true;

        try (AudioConfig audioConfig = AudioConfig.fromDefaultMicrophoneInput();
             SpeechRecognizer speechRecognizer = new SpeechRecognizer(speechConfig, audioConfig)) {
            System.out.println("Speak into your microphone.");
            Future<SpeechRecognitionResult> task = speechRecognizer.recognizeOnceAsync();
            SpeechRecognitionResult speechRecognitionResult = task.get();
            switch (speechRecognitionResult.getReason()) {
                case RecognizedSpeech -> {
                    String text = speechRecognitionResult.getText();
                    System.out.println("That's what I've heard: " + text);
                    result = !text.toLowerCase().contains("stop recognition".toLowerCase());
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
            result = false;
        }

        return result;
    }
}