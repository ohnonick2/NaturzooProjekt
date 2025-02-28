package net.ohnonick2.naturzooprojekt.db.tier;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import net.ohnonick2.naturzooprojekt.utils.TierGeschlecht;

import java.time.LocalDate;

@Entity
public class Tier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private LocalDate geburtsdatum;

    @Column(name = "Todsdatum", nullable = true)
    private LocalDate sterbedatum;

    @Column(name = "AbgabeDatum", nullable = true)
    private LocalDate abgabeDatum;

    private TierGeschlecht geschlecht;

    @Column(name = "isAbgegeben")
    private boolean isAbgegeben;


    @OneToOne
    private TierArt tierArt;

    public Tier() {
    }

    public Tier(String name, LocalDate geburtsdatum, @Nullable LocalDate sterbedatum, @Nullable LocalDate abgabeDatum, TierGeschlecht geschlecht, TierArt tierArt) {
        this.name = name;
        this.geburtsdatum = geburtsdatum;
        this.sterbedatum = sterbedatum;
        this.geschlecht = geschlecht;
        this.tierArt = tierArt;
        this.isAbgegeben = false;
        this.abgabeDatum = null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(LocalDate geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public LocalDate getSterbedatum() {
        return sterbedatum;
    }

    public void setSterbedatum(LocalDate sterbedatum) {
        this.sterbedatum = sterbedatum;
    }

    public LocalDate getAbgabeDatum() {
        return abgabeDatum;
    }

    public void setAbgabeDatum(LocalDate abgabeDatum) {
        this.abgabeDatum = abgabeDatum;
    }

    public TierGeschlecht getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(TierGeschlecht geschlecht) {
        this.geschlecht = geschlecht;
    }

    public TierArt getTierArt() {
        return tierArt;
    }

    public void setTierArt(TierArt tierArt) {
        this.tierArt = tierArt;
    }

    public boolean isAbgegeben() {
        return isAbgegeben;
    }

    public void setAbgegeben(boolean abgegeben) {
        isAbgegeben = abgegeben;
    }
}

