package net.ohnonick2.naturzooprojekt.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.aktivitaet.Aktivitaet;
import net.ohnonick2.naturzooprojekt.db.notification.NotificationReadId;
import net.ohnonick2.naturzooprojekt.db.notification.NotificationReadPfleger;
import net.ohnonick2.naturzooprojekt.db.permission.PermissionRolle;
import net.ohnonick2.naturzooprojekt.db.permission.RolleUser;
import net.ohnonick2.naturzooprojekt.repository.AktivitaetRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.util.Optional;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    private AktivitaetRepository auditLogRepository;
    @Qualifier("userDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    private long getUserID() {
        if (SecurityContextHolder.getContext().getAuthentication() == null ||
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser") {
            return 0; // Kein Benutzer eingeloggt
        }

        String user = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        if (user == null) {
            return 0;
        }

        JsonObject userloggedIn = JsonParser.parseString(user).getAsJsonObject();
        return userloggedIn.get("id").getAsLong();
    }

    private String getUserName() {
        String user  = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject userloggedIn = JsonParser.parseString(user).getAsJsonObject();
        return userloggedIn.get("benutzername").getAsString();
    }


    private void logAction(ActivityAction action, JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        if (args.length == 0 || args[0] == null) {
            return; // Falls keine Argumente vorhanden sind, abbrechen
        }

        Object entity = args[0]; // Das neue Objekt (nach der Änderung)
        String entityName = entity.getClass().getSimpleName();
        String entityId = getEntityId(entity);

        // Falls die Entität vom Typ 'Aktivitaet' ist, keine Log-Einträge erstellen
        if (entity instanceof Aktivitaet) {
            return;
        }

        if (entity instanceof NotificationReadId || entity instanceof NotificationReadPfleger || entity instanceof PermissionRolle || entity instanceof RolleUser) {
            return;
        }
        if (getUserID() == 0) {
            return;
        }

        String actionMessage;
        String changedFields = "";

        if (action == ActivityAction.SAVE) {
            Optional<?> oldEntityOpt = auditLogRepository.findById(Long.parseLong(entityId));


            if (oldEntityOpt.isPresent()) {
                Object oldEntity = oldEntityOpt.get();
                changedFields = compareEntities(oldEntity, entity); // Unterschiede ermitteln
                actionMessage = "bearbeitet";
            } else {
                actionMessage = "erstellt";
            }
        } else if (action == ActivityAction.DELETE) {
            actionMessage = "gelöscht";
        } else {
            actionMessage = "modifiziert";
        }

        // Detaillierte Log-Nachricht
        String logMessage = String.format(
                "%s wurde %s. | ID: %s | Änderungen: %s",
                entityName, actionMessage, entityId, changedFields.isEmpty() ? "Keine Änderungen" : changedFields
        );

        // Speichern des Log-Eintrags
        Aktivitaet log = new Aktivitaet(action, getUserID(), getUserName(), logMessage);
        auditLogRepository.save(log);
    }



    private String compareEntities(Object oldEntity, Object newEntity) {
        StringBuilder changes = new StringBuilder();

        try {
            for (Field field : oldEntity.getClass().getDeclaredFields()) {
                field.setAccessible(true); // Zugriff auf private Felder ermöglichen

                Object oldValue = field.get(oldEntity);
                Object newValue = field.get(newEntity);

                // Falls Werte unterschiedlich sind, zur Änderungs-Liste hinzufügen
                if ((oldValue == null && newValue != null) || (oldValue != null && !oldValue.equals(newValue))) {
                    changes.append(String.format("%s: %s → %s, ", field.getName(), oldValue, newValue));
                }
            }
        } catch (Exception e) {
            return "Fehler beim Vergleich: " + e.getMessage();
        }

        // Entferne letztes Komma und Leerzeichen, falls Änderungen existieren
        return changes.length() > 0 ? changes.substring(0, changes.length() - 2) : "Keine Änderungen";
    }



    private String getEntityId(Object entity) {
        try {
            return entity.getClass().getMethod("getId").invoke(entity).toString();
        } catch (Exception e) {
            return "Unbekannt";
        }
    }
    @AfterReturning("execution(* org.springframework.data.jpa.repository.JpaRepository.save(..))")
    public void logSaveOrEdit(JoinPoint joinPoint) {
        logAction(ActivityAction.SAVE, joinPoint);
    }

    @Before("execution(* org.springframework.data.jpa.repository.JpaRepository.delete(..))")
    public void logDelete(JoinPoint joinPoint) {
        logAction(ActivityAction.DELETE, joinPoint);
    }






}
