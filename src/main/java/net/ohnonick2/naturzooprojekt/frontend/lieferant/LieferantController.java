package net.ohnonick2.naturzooprojekt.frontend.lieferant;

import net.ohnonick2.naturzooprojekt.repository.LieferantRepository;
import net.ohnonick2.naturzooprojekt.repository.Ortrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LieferantController {

    @Autowired
    private LieferantRepository lieferantRepository;

    @Autowired
    private Ortrepository ortrepository;

    @GetMapping("/lieferanten")
    public String lieferant(Model model) {

        model.addAttribute("lieferantList", lieferantRepository.findAll());

        return "autharea/lieferant/lieferantmanagement";
    }

    @GetMapping("/addLieferant")
    public String addLieferant(Model model) {

        model.addAttribute("orte", ortrepository.findAll());

        return "autharea/lieferant/addlieferantmanagement";
    }



}
