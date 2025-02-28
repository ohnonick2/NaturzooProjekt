package net.ohnonick2.naturzooprojekt.utils;

public enum ActivityAction {

    SAVE("hat {target} gespeichert."),
    EDIT("hat {target} bearbeitet."),
    DELETE("hat {target} gelöscht.");

    private final String messageTemplate;

    ActivityAction(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getFormattedMessage(String user, String target) {
        return user + " " + messageTemplate.replace("{target}", target);
    }

}
