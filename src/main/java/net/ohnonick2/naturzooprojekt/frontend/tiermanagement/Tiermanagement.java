package net.ohnonick2.naturzooprojekt.frontend.tiermanagement;

import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import net.ohnonick2.naturzooprojekt.repository.RevierRepository;

import net.ohnonick2.naturzooprojekt.repository.Tierartrepository;
import net.ohnonick2.naturzooprojekt.repository.Tierrespository;
import net.ohnonick2.naturzooprojekt.utils.TierGeschlecht;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class Tiermanagement {

    @Autowired
    private Tierrespository tierrespository;
    @Autowired
    private Tierartrepository tierartrepository;

    @Autowired
    private RevierRepository revierRepository;


    @GetMapping("/tier")
    public String tierManagement(Model model) {

        List<Tier> tierList = tierrespository.findAll();

        model.addAttribute("tiere", tierList);

        model.addAttribute("genders", List.of(TierGeschlecht.values()));
        model.addAttribute("tierArten", tierartrepository.findAll());



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
