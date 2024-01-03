package pl.roka.it.model;

public enum Intents {
    PROGRAM_OPEN("openProgram"),
    PROGRAM_CLOSE("closeProgram"),
    TRACK_NEXT("nextTrack"),
    EMPTY("empty");

    private final String intent;

    Intents(String intent) {
        this.intent = intent;
    }

    public static Intents fromString(String intentString) {
        for (Intents intent : Intents.values()) {
            if (intent.intent.equals(intentString)) {
                return intent;
            }
        }
        return EMPTY;
    }
}
