package net.ohnonick2.naturzooprojekt.frontend.revier;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.revier.Revier;
import net.ohnonick2.naturzooprojekt.db.revier.RevierPfleger;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.*;
import net.ohnonick2.naturzooprojekt.service.RevierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class RevierManagement {



    @Autowired
    private RevierRepository revierRepository;



    @Autowired
    private RevierPflegerRepository revierPflegerRepository;

    @Autowired
    private Pflegerrepository pflegerrepository;
    @Autowired
    private Tierrespository tierrespository;


    @Autowired
    private RevierService revierService;

    @GetMapping("/revier")
    public String revier(Model model) {



        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject jsonObject = JsonParser.parseString(username).getAsJsonObject();
        long id = jsonObject.get("id").getAsLong();


        List<Pfleger> verfuegbarePfleger = pflegerrepository.findAll();




        model.addAttribute("revierList", revierService.getRevierDTO() );
        return "autharea/revier/revierverwaltung";
    }


    @GetMapping("/editRevier/{id}")
    public String editRevier(@PathVariable Long id, Model model) {

        Optional<Revier> revierOptional = revierRepository.findById(id);
        if (revierOptional.isPresent()) {
            Revier revier = revierOptional.get();

            // Pfleger im Revier laden
            List<RevierPfleger> revierPflegerList = revierPflegerRepository.findAllByRevier(revier);
            model.addAttribute("revierPflegerList", revierPflegerList);

            // Alle verfügbaren Pfleger laden
            List<Pfleger> allPfleger = pflegerrepository.findAll();
            allPfleger.removeIf(pfleger -> revierPflegerList.stream()
                    .anyMatch(rp -> rp.getPfleger().getId().equals(pfleger.getId())));

            model.addAttribute("verfuegbarePfleger", allPfleger);

            // Tiere im Revier laden

            model.addAttribute("revier", revier);
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
