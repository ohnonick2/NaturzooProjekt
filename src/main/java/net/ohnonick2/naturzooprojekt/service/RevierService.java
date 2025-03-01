package net.ohnonick2.naturzooprojekt.service;

import net.ohnonick2.naturzooprojekt.db.gebaeude.Gebaeude;
import net.ohnonick2.naturzooprojekt.db.revier.Revier;
import net.ohnonick2.naturzooprojekt.db.revier.RevierGebaeude;
import net.ohnonick2.naturzooprojekt.db.revier.RevierPfleger;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.Pflegerrepository;
import net.ohnonick2.naturzooprojekt.repository.RevierGebaeudeRepository;
import net.ohnonick2.naturzooprojekt.repository.RevierPflegerRepository;
import net.ohnonick2.naturzooprojekt.repository.RevierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RevierService {


    @Autowired
    private RevierRepository revierRepository;

    @Autowired
    private RevierPflegerRepository revierPflegerRepository;

    @Autowired
    private Pflegerrepository pflegerRepository;
    @Autowired
    private RevierGebaeudeRepository revierGebaeudeRepository;

    public RevierService() {
    }

    public boolean createRevier(String name) {
        if (revierRepository.findRevierByName(name) != null) {
            return false;
        }

        revierRepository.save(new Revier(name));
        return true;
    }

    public boolean updateRevier(long revierId, String name) {
        Revier revier = revierRepository.findById(revierId);
        if (revier == null) {
            return false;
        }

        revier.setName(name);
        revierRepository.save(revier);
        return true;
    }

    public boolean deleteRevier(long revierId) {
        Revier revier = revierRepository.findById(revierId);
        if (revier == null) {
            return false;
        }
        revierRepository.delete(revier);
        return true;
    }

    public boolean addPflegerToRevier(long revierId, long pflegerId) {
        Revier revier = revierRepository.findById(revierId);
        if (revier == null) {
            return false;
        }

        Pfleger pfleger = pflegerRepository.findPflegerById(pflegerId);
        if (pfleger == null) {
            return false;
        }
        RevierPfleger revierPfleger = new RevierPfleger(revier, pfleger);
        revierPflegerRepository.save(revierPfleger);
        return true;
    }

    public boolean removePflegerFromRevier(long revierId, long pflegerId) {
        Revier revier = revierRepository.findById(revierId);
        if (revier == null) {
            return false;
        }

        Pfleger pfleger = pflegerRepository.findPflegerById(pflegerId);
        if (pfleger == null) {
            return false;
        }

        RevierPfleger revierPfleger = revierPflegerRepository.findByRevierAndPfleger(revier, pfleger);
        if (revierPfleger == null) {
            return false;
        }

        revierPflegerRepository.delete(revierPfleger);
        return true;
    }

    public Revier findRevierById(long id) {
        return revierRepository.findById(id);
    }


    public Revier findRevierByName(String name) {
        return revierRepository.findRevierByName(name);
    }


    public List<Revier> getAllRevier() {
        return revierRepository.findAll();
    }

    public List<Pfleger> getPflegerForRevier(long revierId) {
        Revier revier = revierRepository.findById(revierId);
        if (revier == null) {
            return null;
        }

        List<RevierPfleger> revierPfleger = revierPflegerRepository.findAllByRevier(revier);

        List<Pfleger> pflegerList = new ArrayList<>();
        for (RevierPfleger revierPfleger1 : revierPfleger) {
            pflegerList.add(revierPfleger1.getPfleger());
        }

        return pflegerList;
    }



    public List<RevierDTO> getRevierDTO() {

        List<RevierDTO> revierDTOList = new ArrayList<>();
        List<Revier> reviere = revierRepository.findAll();
        List<Pfleger> pfleger = new ArrayList<>();
        List<Gebaeude> gebaeude = new ArrayList<>();

        for (Revier revier : reviere) {
            List<RevierPfleger> revierPfleger = revierPflegerRepository.findAllByRevier(revier);
            for (RevierPfleger revierPfleger1 : revierPfleger) {
                pfleger.add(revierPfleger1.getPfleger());
            }

            List<RevierGebaeude> revierGebaeude = revierGebaeudeRepository.findAllByRevier(revier);

            for (RevierGebaeude revierGebaeude1 : revierGebaeude) {
                gebaeude.add(revierGebaeude1.getGebaeude());
            }




        }


        for (Revier revier : reviere) {
            revierDTOList.add(new RevierDTO(revier.getId(), revier.getName(), pfleger, gebaeude));
        }

        return revierDTOList;

    }


    public class RevierDTO {

        private long id;
        private String name;
        private List<Pfleger> pfleger;
        private List<Gebaeude> gebaeude;

        public RevierDTO(long id, String name, List<Pfleger> pfleger, List<Gebaeude> gebaeude) {
            this.id = id;
            this.name = name;
            this.pfleger = pfleger;
            this.gebaeude = gebaeude;
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

        public List<Pfleger> getPfleger() {
            return pfleger;
        }

        public void setPfleger(List<Pfleger> pfleger) {
            this.pfleger = pfleger;
        }

        public List<Gebaeude> getGebaeude() {
            return gebaeude;
        }

        public void setGebaeude(List<Gebaeude> gebaeude) {
            this.gebaeude = gebaeude;
        }
    }
}
