package net.ohnonick2.naturzooprojekt.frontend.tiermanagement;

import net.ohnonick2.naturzooprojekt.db.revier.RevierTier;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import net.ohnonick2.naturzooprojekt.repository.RevierRepository;
import net.ohnonick2.naturzooprojekt.repository.RevierTierRepository;

import net.ohnonick2.naturzooprojekt.repository.Tierartrepository;
import net.ohnonick2.naturzooprojekt.repository.Tierrespository;
import net.ohnonick2.naturzooprojekt.utils.TierGeschlecht;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class Tiermanagement {

    @Autowired
    private Tierrespository tierrespository;
    @Autowired
    private Tierartrepository tierartrepository;
    @Autowired
    private RevierTierRepository revierTierRespository;
    @Autowired
    private RevierRepository revierRepository;


    @GetMapping("/tier")
    public String tierManagement(Model model) {
        List<RevierTier> revierTierList = revierTierRespository.findAll();
        List<Tier> tierList = tierrespository.findAll();

        if (revierTierList.isEmpty()) {
            System.out.println("Keine Tiere mit zugewiesenem Revier gefunden!");
        } else {
            System.out.println("Tiere mit Revier gefunden: " + revierTierList.size());
        }

        // Liste für die angereicherten Tier-Daten
        List<Map<String, Object>> enrichedList = new ArrayList<>();

        // Alle Tiere mit zugewiesenem Revier hinzufügen
        revierTierList.forEach(revierTier -> {
            Map<String, Object> map = new HashMap<>();
            map.put("tierId", revierTier.getTierId().getId());
            map.put("tierName", revierTier.getTierId().getName());
            map.put("geburtsdatum", revierTier.getTierId().getGeburtsdatum());
            map.put("sterbedatum", revierTier.getTierId().getSterbedatum() != null ? revierTier.getTierId().getSterbedatum() : "Noch am Leben");
            map.put("geschlecht", revierTier.getTierId().getGeschlecht());
            map.put("tierArtName", revierTier.getTierId().getTierArt().getName());

            // Falls das Revier NULL ist, setze "Kein Revier zugewiesen"
            String revierName = (revierTier.getRevierId() != null) ? revierTier.getRevierId().getName() : "Kein Revier zugewiesen";
            map.put("revierName", revierName);

            map.put("abgegeben", revierTier.getTierId().isAbgegeben() ? "Ja" : "Nein");

            enrichedList.add(map);
        });

        // Alle Tiere durchgehen und prüfen, ob sie in `revierTierList` enthalten sind
        for (Tier tier : tierList) {
            boolean hatRevier = revierTierList.stream()
                    .anyMatch(revierTier -> revierTier.getTierId().getId() == tier.getId());

            // Falls das Tier kein Revier hat, füge es zur Liste hinzu
            if (!hatRevier) {
                Map<String, Object> map = new HashMap<>();
                map.put("tierId", tier.getId());
                map.put("tierName", tier.getName());
                map.put("geburtsdatum", tier.getGeburtsdatum());
                map.put("sterbedatum", tier.getSterbedatum() != null ? tier.getSterbedatum() : "Noch am Leben");
                map.put("geschlecht", tier.getGeschlecht());
                map.put("tierArtName", tier.getTierArt().getName());
                map.put("revierName", "Kein Revier zugewiesen"); // Kein Revier gefunden
                map.put("abgegeben", tier.isAbgegeben() ? "Ja" : "Nein");

                enrichedList.add(map);
            }
        }

        model.addAttribute("tierList", enrichedList);
        return "autharea/tiermanagement/tiermanagement";
    }





    @GetMapping("/addTier/")
    public String addTier(Model model) {

        List<TierGeschlecht> geschlechts = List.of(TierGeschlecht.values());

        model.addAttribute("genders", geschlechts);
        model.addAttribute("tierArten", tierartrepository.findAll());
        model.addAttribute("reviere", revierRepository.findAll());





        return "autharea/tiermanagement/addtiermanagement";
    }

    @GetMapping("/editTier/{id}")
    public String editTier(@PathVariable Long id, Model model) {

        if (tierrespository.findById(id).isEmpty()) {
            return "redirect:/tier";
        }

        Tier tier = tierrespository.findById(id).get();
        if (tier == null) {
            return "redirect:/tier";
        }

        var geschlechts = List.of(TierGeschlecht.values());

        model.addAttribute("tier", tier);
        model.addAttribute("genders", geschlechts);
        model.addAttribute("tierArten", tierartrepository.findAll());
        model.addAttribute("reviere", revierRepository.findAll());

        return "autharea/tiermanagement/edittiermanagement";
    }







}
