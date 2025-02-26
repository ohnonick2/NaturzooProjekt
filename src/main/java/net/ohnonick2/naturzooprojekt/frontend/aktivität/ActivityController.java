package net.ohnonick2.naturzooprojekt.frontend.aktivität;

import net.ohnonick2.naturzooprojekt.db.aktivitaet.Aktivitaet;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.AktivitaetRepository;
import net.ohnonick2.naturzooprojekt.repository.Pflegerrepository;
import net.ohnonick2.naturzooprojekt.utils.ActivityAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ActivityController {

    @Autowired
    private Pflegerrepository pflegerrepository;

    @Autowired
    private AktivitaetRepository aktivitaetRepository;

    @GetMapping("/logs")
    public String logs(Model model) {

        // Alle Aktivitäten abrufen
        List<Aktivitaet> aktivitaeten = aktivitaetRepository.findAll();

        // Alle Pfleger abrufen und als Map speichern (ID -> Benutzername)
        Map<Long, String> pflegerMap = pflegerrepository.findAll().stream()
                .collect(Collectors.toMap(Pfleger::getId, Pfleger::getBenutzername));

        // Benutzernamen ersetzen und Datum formatieren
        aktivitaeten.forEach(a -> {
            if (pflegerMap.containsKey(a.getUserId())) {
                a.setUsername(pflegerMap.get(a.getUserId()));
            }
        });

        model.addAttribute("actions", ActivityAction.values());
        model.addAttribute("aktivitaeten", aktivitaeten);
        return "autharea/logs/logs";
    }
}
