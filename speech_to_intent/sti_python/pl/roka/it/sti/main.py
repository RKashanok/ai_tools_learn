import json
import threading
import uuid

import requests
from azure.cognitiveservices.speech import SpeechConfig, AudioConfig, SpeechRecognizer, CancellationReason, ResultReason

from pl.roka.it.sti.config.config import get_speech_resource_key, get_azure_region, get_language_resource_key, endpoint, \
    api_version, category_program, project_name, deployment_name, string_index_type
from pl.roka.it.sti.intent_service import process_intent


def get_intent_recognition_result(intent_text):
    url = f"{endpoint}/language/:analyze-conversations?api-version={api_version}"
    headers = {
        "Ocp-Apim-Subscription-Key": get_language_resource_key(),
        "Content-Type": "application/json"
    }
    payload = create_utt_request(intent_text)
    return requests.post(url, headers=headers, data=json.dumps(payload))


def intent_recognition(intent_text):
    try:
        response = get_intent_recognition_result(intent_text)
        status_code = response.status_code
        if status_code == 200:
            print("Request was successful.")
            response_body = response.text
            result = json.loads(response_body)['result']
            for entity in result['prediction']['entities']:
                category = entity['category']
                if category.lower() == category_program.lower():
                    process_intent(result['prediction']['topIntent'], entity)
                else:  # TODO: implement some default behavior
                    print("Unknown category: " + category)
        else:
            print("Request failed with status code " + str(status_code))
    except Exception as e:
        print(e)


def recognize_from_microphone(speech_config, audio_config):
    result_flag = True
    speech_recognizer = SpeechRecognizer(speech_config=speech_config, audio_config=audio_config)
    print("I'm listening...")
    speech_recognition_result = speech_recognizer.recognize_once()
    if speech_recognition_result.reason == ResultReason.RecognizedSpeech:
        text = speech_recognition_result.text
        print("That's what I've heard: " + text)
        result_flag = "stop recognition" not in text.lower()
        if result_flag:
            threading.Thread(target=intent_recognition, args=(text,)).start()
    elif speech_recognition_result.reason == ResultReason.NoMatch:
        print("NOMATCH: Speech could not be recognized.")
    elif speech_recognition_result.reason == ResultReason.Canceled:
        cancellation = CancellationReason.from_result(speech_recognition_result)
        print(f"CANCELED: Reason={cancellation}")
    else:
        print(f"Recognition failed with reason: {speech_recognition_result.reason}")
    return result_flag


def create_utt_request(intent_text):
    return {
        "kind": "Conversation",
        "analysisInput": {
            "conversationItem": {
                "id": str(uuid.uuid4()),
                "participantId": str(uuid.uuid4()),
                "text": intent_text,
                "modality": "text"
            }
        },
        "parameters": {
            "projectName": project_name,
            "deploymentName": deployment_name,
            "stringIndexType": string_index_type
        }
    }


def main():
    speech_config = SpeechConfig(subscription=get_speech_resource_key(),
                                 region=get_azure_region(),
                                 speech_recognition_language="en-US")
    audio_config = AudioConfig(use_default_microphone=True)
    keep_running = True
    while keep_running:
        keep_running = recognize_from_microphone(speech_config, audio_config)


if __name__ == "__main__":
    main()
