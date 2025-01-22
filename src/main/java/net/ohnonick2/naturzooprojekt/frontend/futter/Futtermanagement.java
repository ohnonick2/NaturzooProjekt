package net.ohnonick2.naturzooprojekt.frontend.futter;

import net.ohnonick2.naturzooprojekt.repository.FutterRepositority;
import net.ohnonick2.naturzooprojekt.repository.LieferantRepository;
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

    @GetMapping("/futter")
    public String futter(Model model){

        model.addAttribute("futterList", futterRepositority.findAll());

        return "autharea/futter/futterverwaltung";
    }

    @GetMapping("editFutter/{id}")
    public String editFutter(Model model, @PathVariable Long id){

        model.addAttribute("futter", futterRepositority.findById(id).get());
        model.addAttribute("lieferanten", lieferantRepository.findAll());

        return "autharea/futter/editfutterverwaltung";
    }

    @GetMapping("addFutter")
    public String addFutter(Model model){

        model.addAttribute("lieferanten", lieferantRepository.findAll());

        return "autharea/futter/addfutterverwaltung";
    }


}
