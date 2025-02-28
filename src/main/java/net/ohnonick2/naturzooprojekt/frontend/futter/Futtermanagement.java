package net.ohnonick2.naturzooprojekt.frontend.futter;

import net.ohnonick2.naturzooprojekt.repository.AdresseRepository;
import net.ohnonick2.naturzooprojekt.repository.FutterRepositority;
import net.ohnonick2.naturzooprojekt.repository.LieferantRepository;
import net.ohnonick2.naturzooprojekt.repository.Ortrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class Futtermanagement {

    @Autowired
    private FutterRepositority futterRepositority;
    @Autowired
    private LieferantRepository lieferantRepository;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private Ortrepository ortrepository;

    @GetMapping("/futter")
    public String futter(Model model){

        model.addAttribute("futterList", futterRepositority.findAll());
        model.addAttribute("lieferanten", lieferantRepository.findAll());
        model.addAttribute("orte", ortrepository.findAll());
        model.addAttribute("adressen", adresseRepository.findAll());
        return "autharea/futter/futterverwaltung";
    }

    @GetMapping("/editFutter/{id}")
    public String editFutter(Model model, @PathVariable Long id){

        model.addAttribute("futter", futterRepositority.findById(id).get());
        model.addAttribute("lieferanten", lieferantRepository.findAll());

        return "autharea/futter/editfutterverwaltung";
    }

    @GetMapping("/addFutter")
    public String addFutter(Model model){

        model.addAttribute("lieferanten", lieferantRepository.findAll());

        return "autharea/futter/addfutterverwaltung";
    }


}
