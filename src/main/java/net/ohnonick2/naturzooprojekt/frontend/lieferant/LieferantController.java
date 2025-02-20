package net.ohnonick2.naturzooprojekt.frontend.lieferant;

import net.ohnonick2.naturzooprojekt.db.lieferant.Lieferant;
import net.ohnonick2.naturzooprojekt.repository.AdresseRepository;
import net.ohnonick2.naturzooprojekt.repository.LieferantRepository;
import net.ohnonick2.naturzooprojekt.repository.Ortrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LieferantController {

    @Autowired
    private LieferantRepository lieferantRepository;

    @Autowired
    private Ortrepository ortrepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @GetMapping("/lieferanten")
    public String lieferant(Model model) {

        model.addAttribute("lieferantList", lieferantRepository.findAll());

        return "autharea/lieferant/lieferantmanagement";
    }

    @GetMapping("/addLieferant")
    public String addLieferant(Model model) {

        model.addAttribute("orte", ortrepository.findAll());
        model.addAttribute("adressen", adresseRepository.findAll());
        return "autharea/lieferant/addlieferantmanagement";
    }

    @GetMapping("/editLieferant/{id}")
    public String editLieferant(@PathVariable Long id ,  Model model) {

        Lieferant lieferant = lieferantRepository.findById(id).orElse(null);
        if (lieferant == null) {
            return "redirect:/lieferanten"; // Weiterleitung, falls ID nicht existiert
        }
        model.addAttribute("lieferant", lieferant);
        model.addAttribute("orte", ortrepository.findAll());
        model.addAttribute("adressen", adresseRepository.findAll());
        return "autharea/lieferant/editlieferantmanagement";
    }



}
