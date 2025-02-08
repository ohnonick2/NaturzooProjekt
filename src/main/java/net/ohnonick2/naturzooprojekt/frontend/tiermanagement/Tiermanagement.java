package net.ohnonick2.naturzooprojekt.frontend.tiermanagement;

import net.ohnonick2.naturzooprojekt.db.revier.RevierTier;
import net.ohnonick2.naturzooprojekt.repository.RevierRepository;
import net.ohnonick2.naturzooprojekt.repository.RevierTierRespository;
import net.ohnonick2.naturzooprojekt.repository.Tierartrepository;
import net.ohnonick2.naturzooprojekt.repository.Tierrespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    private RevierTierRespository revierTierRespository;
    @Autowired
    private RevierRepository revierRepository;


    @GetMapping("/tier")
    public String tierManagement(Model model) {
        List<RevierTier> revierTierList = revierTierRespository.findAll();

        // Anreicherung der Daten für das Frontend
        List<Map<String, Object>> enrichedList = revierTierList.stream().map(revierTier -> {
            Map<String, Object> map = new HashMap<>();
            map.put("tierId", revierTier.getTierId().getId());
            map.put("tierName", revierTier.getTierId().getName());
            map.put("geburtsdatum", revierTier.getTierId().getGeburtsdatum());
            map.put("sterbedatum", revierTier.getTierId().getSterbedatum() != null ? revierTier.getTierId().getSterbedatum() : "Noch am Leben");
            map.put("geschlecht", revierTier.getTierId().getGeschlecht());
            map.put("tierArtName", revierTier.getTierId().getTierArt().getName());
            map.put("revierName", revierTier.getRevierId().getName());
            return map;
        }).toList();

        // Übergabe der angereicherten Daten an das Model
        model.addAttribute("tierList", enrichedList);

        return "autharea/tiermanagement/tiermanagement";
    }


    @GetMapping("/addTier/")
    public String addTier(Model model) {

        return "autharea/tiermanagement/add";
    }





}
