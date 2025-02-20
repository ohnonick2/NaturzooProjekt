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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    @GetMapping("/futterplan")
    public String futterplan(Model model) {
        List<FutterplanDTO> futterplanDTOs = new ArrayList<>();

        // Gruppierung der Futterpläne nach futterplan_id
        Map<Long, List<FutterplanFutter>> gruppierteFutterplaene = new HashMap<>();
        futterplanFutterRepository.findAll().forEach(futterplanFutter -> {
            gruppierteFutterplaene
                    .computeIfAbsent(futterplanFutter.getFutterplan().getId(), k -> new ArrayList<>())
                    .add(futterplanFutter);
        });

        // Erstellung der FutterplanDTOs
        gruppierteFutterplaene.forEach((futterplanId, futterplanFutterListe) -> {
            // Futterplan aus der Datenbank abrufen
            FutterPlan futterPlan = futterPlanRepository.findById(futterplanId).orElse(null);
            if (futterPlan == null) {
                return;
            }

            // Wochentage sammeln
            List<FutterPlanWochentag> futterPlanWochentage = futterPlanWochentagRepository.findByFutterplan(futterPlan);
            String wochentage = futterPlanWochentage.isEmpty() ? "-" :
                    futterPlanWochentage.stream()
                            .map(FutterPlanWochentag::getWochentag)
                            .map(Wochentag::getName)
                            .collect(Collectors.joining(", "));

            // Futternamen, Mengen und Futterzeiten korrekt zusammenfassen
            List<String> futterList = new ArrayList<>();
            List<String> mengeList = new ArrayList<>();
            List<String> futterzeitenList = new ArrayList<>();

            futterplanFutterListe.forEach(futterplanFutter -> {
                futterList.add(futterplanFutter.getFutter().getName());
                mengeList.add(String.valueOf(futterplanFutter.getMenge()));

                List<FutterPlanFutterZeit> futterPlanFutterZeiten = futterPlanFutterZeitRepository.findByFutterplanId(futterplanFutter.getFutterplan().getId());

                futterPlanFutterZeiten.forEach(futterPlanFutterZeit -> {
                    futterzeitenList.add(futterPlanFutterZeit.getFutterZeit().getUhrzeit().toString());
                });
            });

            // Wenn keine Futtermittel, Mengen oder Futterzeiten vorhanden sind, setze "-" als Platzhalter
            String futter = futterList.isEmpty() ? "-" : String.join(", ", futterList);
            String menge = mengeList.isEmpty() ? "-" : String.join(", ", mengeList);
            String futterzeiten = futterzeitenList.isEmpty() ? "-" : String.join(", ", futterzeitenList);

            // Erstellen des DTOs
            futterplanDTOs.add(new FutterplanDTO(futterPlan.getId(), futterPlan.getName(),
                    futter, wochentage, menge, futterzeiten));
        });

        model.addAttribute("futterplanList", futterplanDTOs);

        return "autharea/futterplan/futterplanmanagement";
    }



    @GetMapping("/editFutterplan/{id}")
    public String editFutterplan(@PathVariable Long id, Model model) {
        return futterPlanRepository.findById(id)
                .map(futterPlan -> {
                    // Futter und Mengen abrufen
                    List<FutterplanFutter> futterListForPlan = futterplanFutterRepository.findByFutterplanId(futterPlan.getId());

                    Map<Long, Integer> futterMengenMap = futterListForPlan.stream()
                            .collect(Collectors.toMap(f -> f.getFutter().getId(), FutterplanFutter::getMenge));

                    List<String> futterList = futterListForPlan.stream()
                            .map(f -> f.getFutter().getName())
                            .collect(Collectors.toList());

                    List<String> mengeList = futterListForPlan.stream()
                            .map(f -> String.valueOf(f.getMenge()))
                            .collect(Collectors.toList());

                    // Futterzeiten abrufen
                    List<String> futterzeitenList = futterPlanFutterZeitRepository.findByFutterplanId(futterPlan.getId()).stream()
                            .map(futterPlanFutterZeit -> futterPlanFutterZeit.getFutterZeit().getUhrzeit().toString())
                            .collect(Collectors.toList());

                    // Wochentage abrufen
                    String wochentage = futterPlanWochentagRepository.findByFutterplan(futterPlan).stream()
                            .map(FutterPlanWochentag::getWochentag)
                            .map(Wochentag::getName)
                            .collect(Collectors.joining(", "));

                    // DTO für den editierten Futterplan erstellen
                    FutterplanDTO futterplanDTO = new FutterplanDTO(
                            futterPlan.getId(),
                            futterPlan.getName(),
                            futterList.isEmpty() ? "-" : String.join(", ", futterList),
                            "-",
                            mengeList.isEmpty() ? "-" : String.join(", ", mengeList),
                            futterzeitenList.isEmpty() ? "-" : String.join(", ", futterzeitenList)
                    );
                    futterplanDTO.setWochentage(wochentage.isEmpty() ? "-" : wochentage);

                    // Attribute für das Model setzen
                    model.addAttribute("futterplanDTO", futterplanDTO);
                    model.addAttribute("futterplan", futterPlan);
                    model.addAttribute("aktuelleFutterList", futterListForPlan);
                    model.addAttribute("futterMengenMap", futterMengenMap);
                    model.addAttribute("wochentagList", wochenTagRepository.findAll());
                    model.addAttribute("futterzeitenList", futterPlanFutterZeitRepository.findByFutterplanId(futterPlan.getId()));
                    model.addAttribute("verfügbareFutterList", futterRepository.findAll());

                    return "autharea/futterplan/editfutterplanmanagement";
                })
                .orElse("redirect:/futterplan"); // Falls nicht gefunden, zur Übersicht weiterleiten
    }





    @GetMapping("/addFutterplan")
    public String addFutterplan(Model model) {


        return "autharea/futterplan/addfutterplanmanagement";
    }








    public static class FutterplanDTO {
        private Long id;
        private String name;
        private String futter;
        private String wochentage;
        private String futterZeiten;
        private String menge;

        // Konstruktor
        public FutterplanDTO(Long id, String name, String futter, String wochentage, String menge, String futterZeiten) {
            this.id = id;
            this.name = name;
            this.futter = futter;
            this.wochentage = wochentage;
            this.menge = menge;
            this.futterZeiten = futterZeiten;
        }

        // Getter und Setter
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFutter() {
            return futter;
        }

        public void setFutter(String futter) {
            this.futter = futter;
        }

        public String getWochentage() {
            return wochentage;
        }

        public void setWochentage(String wochentage) {
            this.wochentage = wochentage;
        }

        public String getMenge() {
            return menge;
        }

        public void setMenge(String menge) {
            this.menge = menge;
        }

        public String getFutterZeiten() {
            return futterZeiten;
        }

        public void setFutterZeiten(String futterZeiten) {
            this.futterZeiten = futterZeiten;
        }
    }
}
