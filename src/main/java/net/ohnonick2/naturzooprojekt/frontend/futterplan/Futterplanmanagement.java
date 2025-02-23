package net.ohnonick2.naturzooprojekt.frontend.futterplan;

import net.ohnonick2.naturzooprojekt.db.futter.*;
import net.ohnonick2.naturzooprojekt.db.wochentag.Wochentag;
import net.ohnonick2.naturzooprojekt.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class Futterplanmanagement {

    private static final Logger logger = LoggerFactory.getLogger(Futterplanmanagement.class);

    @Autowired
    private FutterPlanRepository futterPlanRepository;

    @Autowired
    private WochenTagRepository wochenTagRepository;

    @Autowired
    private FutterRepositority futterRepository;

    @Autowired
    private FutterPlanWochentagRepository futterPlanWochentagRepository;

    @Autowired
    private FutterplanFutterRepository futterplanFutterRepository;

    @Autowired
    private FutterPlanFutterZeitRepository futterPlanFutterZeitRepository;

    /**
     * Zeigt die Liste aller Futterpläne
     */
    @GetMapping("/futterplan")
    public String futterplan(Model model) {
        List<FutterplanDTO> futterplanDTOs = new ArrayList<>();

        // Futterpläne abrufen und gruppieren
        futterplanFutterRepository.findAll().stream()
                .collect(Collectors.groupingBy(f -> f.getFutterplan().getId()))
                .forEach((futterplanId, futterplanFutterListe) -> {

                    // Hole den Futterplan
                    FutterPlan futterPlan = futterPlanRepository.findById(futterplanId).orElse(null);
                    if (futterPlan == null) return;

                    // Wochentage abrufen
                    String wochentage = futterPlanWochentagRepository.findByFutterplan(futterPlan).stream()
                            .map(wt -> wt.getWochentag().getName())
                            .collect(Collectors.joining(", "));

                    // Futtermittel & Mengen sammeln
                    String futter = futterplanFutterListe.stream()
                            .map(f -> f.getFutter().getName())
                            .collect(Collectors.joining(", "));
                    String menge = futterplanFutterListe.stream()
                            .map(f -> String.valueOf(f.getMenge()))
                            .collect(Collectors.joining(", "));

                    // Futterzeiten abrufen
                    String futterzeiten = futterPlanFutterZeitRepository.findByFutterplanId(futterPlan.getId()).stream()
                            .map(fz -> fz.getFutterZeit().getUhrzeit().toString())
                            .collect(Collectors.joining(", "));

                    // DTO zur Liste hinzufügen
                    futterplanDTOs.add(new FutterplanDTO(futterPlan.getId(), futterPlan.getName(), futter, wochentage, menge, futterzeiten));
                });

        model.addAttribute("futterplanList", futterplanDTOs);
        return "autharea/futterplan/futterplanmanagement";
    }

    /**
     * Zeigt das Bearbeitungsformular für einen Futterplan
     */
    @GetMapping("/editFutterplan/{id}")
    public String editFutterplan(@PathVariable Long id, Model model) {
        return futterPlanRepository.findById(id).map(futterPlan -> {


            List<FutterplanFutter> futterListForPlan = futterplanFutterRepository.findByFutterplanId(id);
            Map<Long, Integer> futterMengenMap = futterListForPlan.stream()
                    .collect(Collectors.toMap(f -> f.getFutter().getId(), FutterplanFutter::getMenge));


            Map<Long, String> futterNamenMap = futterRepository.findAll().stream()
                    .collect(Collectors.toMap(Futter::getId, Futter::getName));


            List<String> futterzeitenList = futterPlanFutterZeitRepository.findByFutterplanId(id).stream()
                    .map(fz -> fz.getFutterZeit().getUhrzeit())
                    .collect(Collectors.toList());


            List<Long> wochentageList = futterPlanWochentagRepository.findByFutterplan(futterPlan).stream()
                    .map(wt -> wt.getWochentag().getId())
                    .collect(Collectors.toList());


            model.addAttribute("futterplan", futterPlan);
            model.addAttribute("futterMengenMap", futterMengenMap);
            model.addAttribute("futterNamenMap", futterNamenMap); // NEU
            model.addAttribute("wochentagList", wochenTagRepository.findAll());
            model.addAttribute("futterzeitenList", futterzeitenList);
            model.addAttribute("verfügbareFutterList", futterRepository.findAll());
            model.addAttribute("ausgewählteWochentage", wochentageList);

            return "autharea/futterplan/editfutterplanmanagement";
        }).orElse("redirect:/futterplan");
    }




    /**
     * Zeigt das Formular für einen neuen Futterplan
     */
    @GetMapping("/addFutterplan")
    public String addFutterplan(Model model) {
        model.addAttribute("futterplan", new FutterPlan());  // 🛠️ Futterplan-Objekt hinzufügen
        model.addAttribute("wochentagList", wochenTagRepository.findAll());
        model.addAttribute("verfügbareFutterList", futterRepository.findAll());
        return "autharea/futterplan/addfutterplanmanagement";
    }


    /**
     * DTO-Klasse für Thymeleaf
     */
    public static class FutterplanDTO {
        private Long id;
        private String name;
        private String futter;
        private String wochentage;
        private String futterZeiten;
        private String menge;

        public FutterplanDTO(Long id, String name, String futter, String wochentage, String menge, String futterZeiten) {
            this.id = id;
            this.name = name;
            this.futter = futter;
            this.wochentage = wochentage;
            this.menge = menge;
            this.futterZeiten = futterZeiten;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getFutter() { return futter; }
        public void setFutter(String futter) { this.futter = futter; }

        public String getWochentage() { return wochentage; }
        public void setWochentage(String wochentage) { this.wochentage = wochentage; }

        public String getMenge() { return menge; }
        public void setMenge(String menge) { this.menge = menge; }

        public String getFutterZeiten() { return futterZeiten; }
        public void setFutterZeiten(String futterZeiten) { this.futterZeiten = futterZeiten; }
    }
}
