package pl.roka.it;

import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import pl.roka.it.service.IntentService;
import pl.roka.it.service.RecognitionService;

import static pl.roka.it.Constants.getAzureRegion;
import static pl.roka.it.Constants.getSpeechResourceKey;


public class Main {

    private static boolean keepRunning = true;

    public static void main(String[] args) {
        SpeechConfig speechConfig = SpeechConfig.fromSubscription(getSpeechResourceKey(), getAzureRegion());
        speechConfig.setSpeechRecognitionLanguage("en-US");
        AudioConfig audioConfig = AudioConfig.fromDefaultMicrophoneInput();
        IntentService intentService = new IntentService();
        RecognitionService recognitionService = new RecognitionService(speechConfig, audioConfig, intentService);

        //TODO transform this into speech (TTS)
        System.out.println("Just tell me what do you want me to do for you.");
        while (keepRunning) {
            keepRunning = recognitionService.recognizeFromMicrophone();
        }
    }
}

