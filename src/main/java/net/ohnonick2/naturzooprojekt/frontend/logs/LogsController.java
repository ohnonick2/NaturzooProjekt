package net.ohnonick2.naturzooprojekt.frontend.logs;

import net.ohnonick2.naturzooprojekt.db.aktivitaet.Aktivitaet;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.AktivitaetRepository;
import net.ohnonick2.naturzooprojekt.utils.ActivityAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LogsController {

    @Autowired
    private AktivitaetRepository aktivitaetRepository;

    @GetMapping("/logs")
    public String logs(Model model) {

        model.addAttribute("logs", aktivitaetRepository.findAll());


        List<String> actions = Arrays.stream(ActivityAction.values())
                .map(ActivityAction::getActionname)
                .collect(Collectors.toList());

        model.addAttribute("type" , actions);



        return "autharea/logs/logs";
    }

}
