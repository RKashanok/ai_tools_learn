import os
from azure.cognitiveservices.speech import SpeechConfig, AudioConfig, SpeechRecognizer, CancellationReason, ResultReason


def recognize_from_microphone(speech_config):
    result_flag = True
    audio_config = AudioConfig(use_default_microphone=True)
    speech_recognizer = SpeechRecognizer(speech_config=speech_config, audio_config=audio_config)
    print("Speak into your microphone.")
    speech_recognition_result = speech_recognizer.recognize_once()
    if speech_recognition_result.reason == ResultReason.RecognizedSpeech:
        text = speech_recognition_result.text
        print("That's what I've heard: " + text)
        result_flag = "stop recognition" not in text.lower()
    elif speech_recognition_result.reason == ResultReason.NoMatch:
        print("NOMATCH: Speech could not be recognized.")
    elif speech_recognition_result.reason == ResultReason.Canceled:
        cancellation = CancellationReason.from_result(speech_recognition_result)
        print(f"CANCELED: Reason={cancellation}")
    else:
        print(f"Recognition failed with reason: {speech_recognition_result.reason}")
    return result_flag


def main():
    speech_key = os.getenv('SPEECH_KEY')
    speech_region = os.getenv('SPEECH_REGION')
    speech_config = SpeechConfig(subscription=speech_key, region=speech_region)
    speech_config.speech_recognition_language = "en-US"

    keep_running = True
    while keep_running:
        keep_running = recognize_from_microphone(speech_config)


if __name__ == "__main__":
    main()
