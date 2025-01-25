package net.ohnonick2.naturzooprojekt.frontend.revier;

import net.ohnonick2.naturzooprojekt.db.revier.RevierPfleger;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.Pflegerrepository;
import net.ohnonick2.naturzooprojekt.repository.RevierPflegerRepository;
import net.ohnonick2.naturzooprojekt.repository.RevierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class RevierManagement {



    @Autowired
    private RevierRepository revierRepository;

    @Autowired
    private RevierPflegerRepository revierPflegerRepository;

    @Autowired
    private Pflegerrepository pflegerrepository;


    @GetMapping("/revier")
    public String revier(Model model){
        model.addAttribute("revierList", revierRepository.findAll());

        return "autharea/revier/revierverwaltung";
    }

    @GetMapping("/editRevier/{id}")
    public String editRevier(@PathVariable Long id , Model model){

        if (revierRepository.findById(id).isPresent()){

            List<RevierPfleger> revierPflegerList = revierPflegerRepository.findAllByRevier(revierRepository.findById(id).get());

            if (revierPflegerList.size() > 0){
                model.addAttribute("revierPflegerList", revierPflegerList);
            }

            //remove die revierPfleger die schon in der Liste sind aus der Liste
           List<Pfleger> allRevierPfleger = pflegerrepository.findAll();
            for (RevierPfleger revierPfleger : revierPflegerList){
                allRevierPfleger.remove(revierPfleger.getPfleger());
            }

            model.addAttribute("verfuegbarePfleger", allRevierPfleger);


            model.addAttribute("revier", revierRepository.findById(id).get());
        } else {
            return "redirect:/revier";
        }



        return "autharea/revier/editrevierverwaltung";
    }

    @GetMapping("addRevier")
    public String addRevier(){
        return "autharea/revier/addrevierverwaltung";
    }



}
