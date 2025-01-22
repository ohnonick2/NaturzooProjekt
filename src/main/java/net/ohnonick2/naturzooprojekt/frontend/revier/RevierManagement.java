package net.ohnonick2.naturzooprojekt.frontend.revier;

import net.ohnonick2.naturzooprojekt.repository.RevierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RevierManagement {

    @Autowired
    private RevierRepository revierRepository;


    @GetMapping("/revier")
    public String revier(Model model){
        model.addAttribute("revierList", revierRepository.findAll());

        return "autharea/revier/revierverwaltung";
    }

    @GetMapping("editRevier/{id}")
    public String editRevier(){
        return "autharea/revier/editrevierverwaltung";
    }

    @GetMapping("addRevier")
    public String addRevier(){
        return "autharea/revier/addrevierverwaltung";
    }



}
