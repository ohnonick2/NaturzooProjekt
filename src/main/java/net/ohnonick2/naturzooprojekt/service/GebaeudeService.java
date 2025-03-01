package net.ohnonick2.naturzooprojekt.service;


import jakarta.persistence.EntityNotFoundException;
import net.ohnonick2.naturzooprojekt.db.gebaeude.Gebaeude;
import net.ohnonick2.naturzooprojekt.db.gebaeude.GebaeudeTier;
import net.ohnonick2.naturzooprojekt.db.revier.Revier;
import net.ohnonick2.naturzooprojekt.db.revier.RevierGebaeude;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class GebaeudeService {

    private static Logger logger = Logger.getLogger(GebaeudeService.class.getName());



    @Autowired
    private GebaeudeRepository gebaeudeRepository;
    @Autowired
    private GebaeudeTierRepository gebaeudeTierRepository;
    @Autowired
    private Pflegerrepository pflegerrepository;

    @Value("${debug.mode:true}")
    private boolean debugMode;
    @Autowired
    private Tierrespository tierrespository;
    @Autowired
    private RevierGebaeudeRepository revierGebaeudeRepository;


    /**
     * Erstellt ein neues Gebäude
     * @param name
     * @param beschreibung
     * @param maximaleKapazitaet
     * @return
     */
    public boolean createGebaeude(String name , String beschreibung , int maximaleKapazitaet) {
        if (gebaeudeRepository.findByName(name) != null || name == null || name.isEmpty() || beschreibung == null || beschreibung.isEmpty() || maximaleKapazitaet < 0) {
            if (debugMode) {
                logger.warning("Gebäude konnte nicht erstellt werden" + name + " " + beschreibung + " " + maximaleKapazitaet);
            }
            return false;
        } else {
            gebaeudeRepository.save(new Gebaeude(name , beschreibung , maximaleKapazitaet));
            if (debugMode) {
                logger.info("Gebäude wurde erstellt" + name + " " + beschreibung + " " + maximaleKapazitaet);
            }
            return true;
        }
    }

    /**
     * Löscht ein Gebäude
     * @param gebaeudeId
     * @return
     */
    public boolean deleteGebaeude(long gebaeudeId) {
        try {
            gebaeudeRepository.deleteById(gebaeudeId);
            if (debugMode) {
                logger.info("Gebäude wurde gelöscht" + gebaeudeId);
            }
            return true;
        } catch (DataIntegrityViolationException | EntityNotFoundException e) {

            if (debugMode) {
                logger.warning("Gebäude konnte nicht gelöscht werden" + gebaeudeId);
            }

            return false;
        }
    }

    /**
     * Updated ein Gebäude
     * @param gebaeudeId
     * @param name
     * @param beschreibung
     * @param maximaleKapazitaet
     * @return
     */
    public boolean updateGebaeude(long gebaeudeId , String name , String beschreibung , int maximaleKapazitaet) {
        Gebaeude gebaeude = gebaeudeRepository.findById(gebaeudeId);
        if (gebaeude == null || name == null || name.isEmpty() || beschreibung == null || beschreibung.isEmpty() || maximaleKapazitaet < 0) {
            if (debugMode) logger.warning("Gebäude konnte nicht geupdatet werden" + gebaeudeId + " " + name + " " + beschreibung + " " + maximaleKapazitaet);
            return false;
        } else {
            gebaeude.setName(name);
            gebaeude.setBeschreibung(beschreibung);
            gebaeude.setMaximaleKapazitaet(maximaleKapazitaet);
            gebaeudeRepository.save(gebaeude);
            if (debugMode) {
                logger.info("Gebäude wurde geupdatet" + gebaeudeId + " " + name + " " + beschreibung + " " + maximaleKapazitaet);
            }
            return true;
        }
    }

    /**
     * Fügt ein Tier zu einem Gebäude hinzu
     * @param gebaeudeId
     * @param tierId
     * @return
     */
    public boolean addTierToGebaeude(long gebaeudeId , long tierId) {
        Gebaeude gebaeude = gebaeudeRepository.findById(gebaeudeId);
        Tier tier = tierrespository.findById(tierId);
        if (gebaeude == null || tier == null) {
            if (debugMode) logger.warning("Tier konnte nicht hinzugefügt werden" + gebaeudeId + " " + tierId);
            return false;
        } else {

            List<Gebaeude> gebaeudeList = gebaeudeTierRepository.findByGebaeude(gebaeude).stream().map(GebaeudeTier::getGebaeude).toList();
            if (gebaeudeList.size() >= gebaeude.getMaximaleKapazitaet()) {
                if (debugMode) logger.warning("Tier konnte nicht hinzugefügt werden" + gebaeudeId + " " + tierId);
                return false;
            }

            gebaeudeTierRepository.save(new GebaeudeTier(gebaeude , tier));
            if (debugMode) logger.info("Tier wurde hinzugefügt" + gebaeudeId + " " + tierId);
            return true;
        }
    }

    /**
     * Entfernt ein Tier von einem Gebäude
     * @param gebaeudeId
     * @param tierId
     * @return
     */
    public boolean removeTierFromGebaeude(long gebaeudeId , long tierId) {
        Gebaeude gebaeude = gebaeudeRepository.findById(gebaeudeId);
        Tier tier = tierrespository.findById(tierId);
        if (gebaeude == null || tier == null) {
            if (debugMode) logger.warning("Tier konnte nicht entfernt werden" + gebaeudeId + " " + tierId);
            return false;
        } else {
            gebaeudeTierRepository.delete(new GebaeudeTier(gebaeude , tier));
            if (debugMode) logger.info("Tier wurde entfernt" + gebaeudeId + " " + tierId);
            return true;
        }
    }

    /**
     * Gibt alle Tiere in einem Gebäude zurück
     * @param gebaeudeId
     * @return
     */
    public List<Tier> getTiereInGebaeude(long gebaeudeId) {
        Gebaeude gebaeude = gebaeudeRepository.findById(gebaeudeId);
        if (gebaeude == null) {
            if (debugMode) logger.warning("Tiere konnten nicht geladen werden" + gebaeudeId);
            return null;
        } else {
            List<Tier> tierList = new ArrayList<>();

            if (gebaeudeTierRepository.findByGebaeude(gebaeude).isEmpty()) {
                return new ArrayList<>();
            }

            for (GebaeudeTier gebaeudeTier : gebaeudeTierRepository.findByGebaeude(gebaeude)) {

                tierList.add(gebaeudeTier.getTier());
            }
            if (debugMode) logger.info("Tiere wurden geladen" + gebaeudeId);

            return tierList;
        }
    }

    /**
     * Gibt ein Gebäude anhand der ID zurück
     * @param gebaeudeId
     * @return
     */
    public Gebaeude getGebaeudeById(long gebaeudeId) {
        return gebaeudeRepository.findById(gebaeudeId);
    }

    /**
     * Gibt alle Gebäude zurück
     * @return
     */
    public List<Gebaeude> getAllGebaeude() {
        return gebaeudeRepository.findAll();
    }

    public List<GebaeudeDTO> getAllGebaeudeDTO() {
        List<GebaeudeDTO> gebaeudeDTOList = new ArrayList<>();
        List<Gebaeude> gebaeudeList = gebaeudeRepository.findAll();
        List<RevierGebaeude> revierList = revierGebaeudeRepository.findAll();

        for (Gebaeude gebaeude : gebaeudeList) {
            List<Tier> tierList = new ArrayList<>();
            for (GebaeudeTier gebaeudeTier : gebaeudeTierRepository.findByGebaeude(gebaeude)) {
                tierList.add(gebaeudeTier.getTier());
            }
            Revier revier = null;
            for (RevierGebaeude revierGebaeude : revierList) {
                if (revierGebaeude.getGebaeude().getId() == gebaeude.getId()) {
                    revier = revierGebaeude.getRevier();
                }
            }



            gebaeudeDTOList.add(new GebaeudeDTO(gebaeude.getName(), gebaeude.getBeschreibung(), gebaeude.getMaximaleKapazitaet(), revier, tierList));

        }

        return gebaeudeDTOList;
    }



    /**
     * Gibt alle Gebäude als DTO zurück
     * @return
     */
    public class GebaeudeDTO {

        private long id;
        private String name;
        private String beschreibung;
        private int maximaleKapazitaet;
        private Revier revier;
        private List<Tier> tierList;
        private List<Pfleger> pflegerList;

        public GebaeudeDTO(String name, String beschreibung, int maximaleKapazitaet , Revier revier , List<Tier> tierList) {
            this.name = name;
            this.beschreibung = beschreibung;
            this.maximaleKapazitaet = maximaleKapazitaet;
            this.revier = revier;
            this.tierList = tierList;
            this.pflegerList = pflegerList;
        }

        public GebaeudeDTO(String name, String beschreibung, int maximaleKapazitaet) {
            this.name = name;
            this.beschreibung = beschreibung;
            this.maximaleKapazitaet = maximaleKapazitaet;
            revier = null;
            tierList = null;
            pflegerList = null;
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

        public String getBeschreibung() {
            return beschreibung;
        }

        public void setBeschreibung(String beschreibung) {
            this.beschreibung = beschreibung;
        }

        public int getMaximaleKapazitaet() {
            return maximaleKapazitaet;
        }

        public void setMaximaleKapazitaet(int maximaleKapazitaet) {
            this.maximaleKapazitaet = maximaleKapazitaet;
        }

        public Revier getRevier() {
            return revier;
        }

        public void setRevier(Revier revier) {
            this.revier = revier;
        }

        public List<Tier> getTierList() {
            return tierList;
        }

        public void setTierList(List<Tier> tierList) {
            this.tierList = tierList;
        }





    }




}
