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


        model.addAttribute("revierTierList", revierTierList);
        model.addAttribute("reviere", revierRepository.findAll());
        model.addAttribute("tierart", tierartrepository.findAll());




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
