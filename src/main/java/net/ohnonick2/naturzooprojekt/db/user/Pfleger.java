package net.ohnonick2.naturzooprojekt.db.user;

import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.db.ort.Ort;
import net.ohnonick2.naturzooprojekt.db.permission.Rolle;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Entity
public class Pfleger {


    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO , generator = "pfleger_seq")
    private Long id;
    private String vorname;
    private String nachname;

    private String password;
    private boolean enabled;

    private String benutzername;

    private int failedLoginAttempts;
    private LocalDateTime lockedUntil; // Sperrdatum

    @JoinColumn(name = "ort")
    @OneToOne
    private Ort ort;

    public Pfleger() {
    }

    public Pfleger(String vorname, String nachname, String password, Ort ort) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.password = password;
        this.ort = ort;
        this.enabled= true;
        String initials = (vorname.substring(0, 1) + nachname.substring(0, 1)).toLowerCase();

        // Erstelle einen Zufallszahlengenerator
        Random random = new Random();
        StringBuilder randomNumbers = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            randomNumbers.append(random.nextInt(10)); // Zufallszahl zwischen 0 und 9
        }

        this.benutzername = initials + randomNumbers.toString();
        this.failedLoginAttempts = 0;
        this.lockedUntil = null;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Ort getOrt() {
        return ort;
    }

    public void setOrt(Ort ort) {
        this.ort = ort;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }


}
