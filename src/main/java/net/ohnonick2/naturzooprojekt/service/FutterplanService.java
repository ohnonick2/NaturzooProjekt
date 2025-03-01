package net.ohnonick2.naturzooprojekt.service;

import net.ohnonick2.naturzooprojekt.db.futter.*;
import net.ohnonick2.naturzooprojekt.db.gebaeude.GebaeudeTier;
import net.ohnonick2.naturzooprojekt.db.revier.Revier;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.db.wochentag.Wochentag;
import net.ohnonick2.naturzooprojekt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FutterplanService {

    @Autowired
    private FutterPlanRepository futterPlanRepository;

    @Autowired
    private FutterplanPflegerRepository futterplanPflegerRepository;

    @Autowired
    private FutterPlanWochentagRepository futterPlanWochentagRepository;

    @Autowired
    private FutterplanFutterRepository futterplanFutterRepository;

    @Autowired
    private FutterZeitRepository futterZeitRepository;

    @Autowired
    private WochenTagRepository wochenTagRepository;

    @Autowired
    private Pflegerrepository pflegerrepository;

    @Autowired
    private RevierPflegerRepository revierPflegerRepository;

    @Autowired
    private GebaeudeTierRepository gebaeudeTierRepository;

    @Autowired
    private RevierGebaeudeRepository revierGebaeudeRepository;

    @Autowired
    private GebaeudeRepository gebaeudeRepository;
    @Autowired
    private FutterPlanFutterZeitRepository futterPlanFutterZeitRepository;
    @Autowired
    private FutterPlanGebaeudeRepository futterPlanGebaeudeRepository;


    public FutterplanService() {
    }



    public FutterplanDTO getFutterplan(long id) {
        FutterPlan futterPlan = futterPlanRepository.findFutterPlanById(id);
        List<FutterplanFutter> futterplanFutters = futterplanFutterRepository.findByFutterplanId(id);

        List<FutterPlanWochentag> futterPlanWochentags = futterPlanWochentagRepository.findByFutterplan(futterPlan);

        List<Wochentag> wochentage = new ArrayList<>();
        futterPlanWochentags.forEach(futterPlanWochentag -> {
            wochentage.add(futterPlanWochentag.getWochentag());
        });

        List<FutterPlanFutterZeit> futterPlanFutterZeits = futterPlanFutterZeitRepository.findByFutterplanId(id);
        List<FutterZeit> futterzeiten = new ArrayList<>();
        futterPlanFutterZeits.forEach(futterPlanFutterZeit -> {
            futterzeiten.add(futterPlanFutterZeit.getFutterZeit());
        });

        List<Futter> futter = futterplanFutters.stream().map(FutterplanFutter::getFutter).toList();
        List<FutterPlanGebaeude> futterPlanGebaeudes = futterPlanGebaeudeRepository.findByFutterPlanId(futterPlan);
        List<Tier> tiere = new ArrayList<>();

        for (FutterPlanGebaeude futterPlanGebaeude : futterPlanGebaeudes) {
            List<GebaeudeTier> gebaeudeTiers = gebaeudeTierRepository.findByGebaeude(futterPlanGebaeude.getGebaeudeId());
            for (GebaeudeTier gebaeudeTier : gebaeudeTiers) {
                tiere.add(gebaeudeTier.getTier());
            }

        }

        List<Revier> revier = new ArrayList<>();

        revierGebaeudeRepository.findAll().forEach(revierGebaeude -> {
            if (futterPlanGebaeudes.stream().anyMatch(futterPlanGebaeude -> futterPlanGebaeude.getGebaeudeId().equals(revierGebaeude.getGebaeude()))) {
                revier.add(revierGebaeude.getRevier());
            }
        });




        int futterMenge = futterplanFutters.stream().map(FutterplanFutter::getMenge).reduce(0, Integer::sum);




        return new FutterplanDTO(futterPlan.getId(), futterPlan.getName(),revier , wochentage, futterzeiten, futter, futterMenge, tiere);
    }

    public List<FutterplanDTO> getFutterplaene(Pfleger pfleger) {

        List<FutterplanDTO> futterplanDTOs = new ArrayList<>();
        List<FutterplanPfleger> futterplanPfleger = futterplanPflegerRepository.findByPflegerId(pfleger.getId());

        futterplanPfleger.forEach(futterplanPfleger1 -> {
            futterplanDTOs.add(getFutterplan(futterplanPfleger1.getFutterPlan().getId()));
        });

        return futterplanDTOs;



    }


    public class FutterplanDTO {


        private long id;
        private String name;
        private List<Revier> revier;

        private List<Wochentag> wochentage;
        private List<FutterZeit> futterzeiten;

        private List<Futter> futter;
        private int futterMenge;

        private List<Tier> tiere;


        public FutterplanDTO(long id, String name, List<Revier> revier, List<Wochentag> wochentage, List<FutterZeit> futterzeiten, List<Futter> futter, int futterMenge, List<Tier> tiere) {
            this.id = id;
            this.name = name;
            this.revier = revier;
            this.wochentage = wochentage;
            this.futterzeiten = futterzeiten;
            this.futter = futter;
            this.futterMenge = futterMenge;
            this.tiere = tiere;
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

        public List<Revier> getRevier() {
            return revier;
        }

        public void setRevier(List<Revier> revier) {
            this.revier = revier;
        }

        public List<Wochentag> getWochentage() {
            return wochentage;
        }

        public void setWochentage(List<Wochentag> wochentage) {
            this.wochentage = wochentage;
        }

        public List<FutterZeit> getFutterzeiten() {
            return futterzeiten;
        }

        public void setFutterzeiten(List<FutterZeit> futterzeiten) {
            this.futterzeiten = futterzeiten;
        }

        public List<Futter> getFutter() {
            return futter;
        }

        public void setFutter(List<Futter> futter) {
            this.futter = futter;
        }

        public int getFutterMenge() {
            return futterMenge;
        }

        public void setFutterMenge(int futterMenge) {
            this.futterMenge = futterMenge;
        }

        public List<Tier> getTiere() {
            return tiere;
        }

        public void setTiere(List<Tier> tiere) {
            this.tiere = tiere;
        }
    }

}
