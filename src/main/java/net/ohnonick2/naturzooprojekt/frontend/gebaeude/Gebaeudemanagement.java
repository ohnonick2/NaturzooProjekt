package net.ohnonick2.naturzooprojekt.frontend.gebaeude;

import groovy.transform.AutoImplement;
import net.ohnonick2.naturzooprojekt.repository.GebaeudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class Gebaeudemanagement {

    @Autowired
    private GebaeudeRepository gebaeudeRepository;

    @GetMapping("gebaeudemanagement")
    public String gebaeudeManagement(Model model) {

        model.addAttribute("gebaeudeList", gebaeudeRepository.findAll());

        return "autharea/gebaeude/gebaeudemanagement";
    }

    @GetMapping("/addGebaeude")
    public String addGebaeude() {
        return "autharea/gebaeude/addgebaeudemanagement";
    }

    @GetMapping("/editGebaeude/{id}")
    public String editGebaeude(@PathVariable Long id, Model model) {

        model.addAttribute("gebaeude", gebaeudeRepository.findById(id).get());
        return "autharea/gebaeude/editgebaeudemanagement";
    }




}
