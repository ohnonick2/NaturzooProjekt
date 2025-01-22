package net.ohnonick2.naturzooprojekt.utils;

public enum ActivityAction {

    LOGIN("User Login", "Benutzer Login"),
    USER_EDIT("Edit User", "Benutzer bearbeiten"),
    USER_DELETE("Delete User", "Benutzer löschen"),
    TIER_ADD("Add Animal", "Tier hinzufügen"),
    TIER_DELETE("Delete Animal", "Tier löschen"),
    TIER_EDIT("Edit Animal", "Tier bearbeiten"),
    ROLE_ASSIGN("Assign Role", "Rolle zuweisen"),
    ROLE_REVOKE("Revoke Role", "Rolle entziehen"),
    SETTINGS_UPDATE("Update Settings", "Einstellungen aktualisieren"),
    GEBÄUDE_ADD("Add Building", "Gebäude hinzufügen"),
    GEBÄUDE_EDIT("Edit Building", "Gebäude bearbeiten"),
    GEBÄUDE_DELETE("Delete Building", "Gebäude löschen");

    private final String action;      // Englische Beschreibung
    private final String actionname;  // Deutsche Beschreibung

    // Konstruktor, der beide Werte setzt
    ActivityAction(String action, String actionname) {
        this.action = action;
        this.actionname = actionname;
    }

    // Getter für den englischen Namen
    public String getAction() {
        return action;
    }

    // Getter für den deutschen Namen
    public String getActionname() {
        return actionname;
    }
}
