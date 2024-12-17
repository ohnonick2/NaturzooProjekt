package net.ohnonick2.naturzooprojekt.frontend;

import net.ohnonick2.naturzooprojekt.repository.Ortrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class temp {

    @Autowired
    private Ortrepository ortrepository;


    @GetMapping("/add")
    public String showAddPage(Model model) {
        // Lade alle Orte und füge sie dem Modell hinzu
        model.addAttribute("orte", ortrepository.findAll());
        return "temp";
    }

}
