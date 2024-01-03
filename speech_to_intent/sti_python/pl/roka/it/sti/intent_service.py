import subprocess
from enum import Enum, auto


class Intents(Enum):
    PROGRAM_OPEN = "openProgram"
    PROGRAM_CLOSE = "closeProgram"
    TRACK_NEXT = "nextTrack"
    EMPTY = "empty"

    @staticmethod
    def from_string(intent_text):
        for intent in Intents:
            if intent.value == intent_text:
                return intent
        return Intents.EMPTY


def process_intent(intent_text, entity):
    intent = Intents.from_string(intent_text)
    if intent is Intents.EMPTY:
        default_action()
    else:
        {
            Intents.PROGRAM_OPEN: open_program,
            Intents.PROGRAM_CLOSE: close_program,
            Intents.TRACK_NEXT: to_next_track
        }.get(intent, default_action)(entity.get('text'))


def open_program(app_name):
    application_name = "/Applications/" + capitalize_first_letter(app_name) + ".app"
    print(f"Opening application: {application_name}")
    try:
        exit_code = subprocess.call(["open", application_name])
        if exit_code == 0:
            print("Application opened successfully!")
        else:
            print(f"Error opening application. Exit code: {exit_code}")
    except Exception as e:
        print(f"open_program error: {e}")
        raise


def close_program(app_name):
    application_name = capitalize_first_letter(app_name)
    print(f"Closing application: {application_name}")
    script = f"tell application \"{application_name}\" to quit"
    try:
        subprocess.call(["osascript", "-e", script])
    except Exception as e:
        print(f"close_program error: {e}")
        raise


def to_next_track(app_name):
    application_name = capitalize_first_letter(app_name)
    print(f"Switching to next track in application: {application_name}")
    script = f"tell application \"{application_name}\" to next track"
    try:
        exit_code = subprocess.call(["osascript", "-e", script])
        if exit_code == 0:
            print("Successfully sent next track command to Spotify.")
        else:
            print("Error occurred while sending next track command to Spotify.")
    except Exception as e:
        print(f"toNextTrack error: {e}")
        raise


def default_action():
    print("Sorry, I didn't understand that. Please try again.")


def capitalize_first_letter(input_str):
    if not input_str:
        return input_str
    return input_str[0].upper() + input_str[1:]

# Example usage
# Replace 'entity' with the actual entity object or dictionary
# process_intent('PROGRAM_OPEN', {'text': 'notes'})
