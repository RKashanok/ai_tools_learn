import os

no_match = "NoMatch"
category_program = "program"
endpoint = "https://ailanguagelearn.cognitiveservices.azure.com"
api_version = "2023-04-01"
project_name = "conversationlangunderstanding"
deployment_name = "jarvis-assistant-deployment"
string_index_type = "TextElement_V8"

env_vars = {}
env_vars.setdefault('AZURE_REGION', os.environ.get('AZURE_REGION'))
env_vars.setdefault('SPEECH_RESOURCE_KEY', os.environ.get('SPEECH_RESOURCE_KEY'))
env_vars.setdefault('LANGUAGE_RESOURCE_KEY', os.environ.get('LANGUAGE_RESOURCE_KEY'))


def get_speech_resource_key():
    return env_vars["SPEECH_RESOURCE_KEY"]


def get_language_resource_key():
    return env_vars["LANGUAGE_RESOURCE_KEY"]


def get_azure_region():
    return env_vars["AZURE_REGION"]


def get_deployment_name():
    return env_vars["AZURE_DEPLOYMENT_NAME"]
