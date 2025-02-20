package net.ohnonick2.naturzooprojekt.frontend.revier;

import net.ohnonick2.naturzooprojekt.db.revier.Revier;
import net.ohnonick2.naturzooprojekt.db.revier.RevierPfleger;
import net.ohnonick2.naturzooprojekt.db.revier.RevierTier;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class RevierManagement {



    @Autowired
    private RevierRepository revierRepository;

    @Autowired
    private RevierTierRepository revierTierRepository;

    @Autowired
    private RevierPflegerRepository revierPflegerRepository;

    @Autowired
    private Pflegerrepository pflegerrepository;
    @Autowired
    private Tierrespository tierrespository;


    @GetMapping("/revier")
    public String revier(Model model){
        model.addAttribute("revierList", revierRepository.findAll());

        return "autharea/revier/revierverwaltung";
    }

    @GetMapping("/editRevier/{id}")
    public String editRevier(@PathVariable Long id, Model model) {

        Optional<Revier> revierOptional = revierRepository.findById(id);
        if (revierOptional.isPresent()) {
            Revier revier = revierOptional.get();

            // Pfleger im Revier laden
            List<RevierPfleger> revierPflegerList = revierPflegerRepository.findAllByRevier(revier);
            model.addAttribute("revierPflegerList", revierPflegerList);

            // Alle verfügbaren Pfleger laden
            List<Pfleger> allPfleger = pflegerrepository.findAll();
            allPfleger.removeIf(pfleger -> revierPflegerList.stream()
                    .anyMatch(rp -> rp.getPfleger().getId().equals(pfleger.getId())));

            model.addAttribute("verfuegbarePfleger", allPfleger);

            // Tiere im Revier laden
            List<RevierTier> revierTiere = revierTierRepository.findAll();
            model.addAttribute("revierTiere", revierTiere);

            // Alle verfügbaren Tiere laden (die noch nicht im Revier sind)
            List<Tier> alleTiere = tierrespository.findAll();
            alleTiere.removeIf(tier -> revierTiere.stream()
                    .anyMatch(rt -> rt.getTierId().getId() == tier.getId()));

            model.addAttribute("verfuegbareTiere", alleTiere);

            model.addAttribute("revier", revier);
        } else {
            return "redirect:/revier";
        }

        return "autharea/revier/editrevierverwaltung";
    }


    @GetMapping("addRevier")
    public String addRevier(){
        return "autharea/revier/addrevierverwaltung";
    }



}
