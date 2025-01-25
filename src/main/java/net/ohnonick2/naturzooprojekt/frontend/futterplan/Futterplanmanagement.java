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
    public String editFutterplan(Model model, @PathVariable Long id) {
        // Futterplan aus der Datenbank laden
        FutterPlan futterPlan = futterPlanRepository.findById(id).orElse(null);
        if (futterPlan == null) {
            return "redirect:/futterplan";
        }

        // Wochentage sammeln
        List<FutterPlanWochentag> futterPlanWochentage = futterPlanWochentagRepository.findByFutterplan(futterPlan);
        List<Wochentag> wochentageList = futterPlanWochentage.stream()
                .map(FutterPlanWochentag::getWochentag)
                .collect(Collectors.toList());

        // Futtermittel sammeln
        List<FutterplanFutter> futterListForPlan = futterplanFutterRepository.findByFutterplanId(futterPlan.getId());
        List<Futter> aktuelleFutterList = futterListForPlan.stream()
                .map(FutterplanFutter::getFutter)
                .collect(Collectors.toList());

        // Mengen sammeln
        Map<Long, Integer> futterMengenMap = futterListForPlan.stream()
                .collect(Collectors.toMap(
                        futterplanFutter -> futterplanFutter.getFutter().getId(),
                        FutterplanFutter::getMenge
                ));

        // Futterzeiten sammeln
        List<FutterPlanFutterZeit> futterPlanFutterZeiten = futterPlanFutterZeitRepository.findByFutterplanId(futterPlan.getId());
        List<String> futterzeiten = futterPlanFutterZeiten.stream()
                .map(fz -> fz.getFutterZeit().getUhrzeit().toString())
                .collect(Collectors.toList());

        // DTO erstellen
        FutterplanDTO futterplanDTO = new FutterplanDTO(
                futterPlan.getId(),
                futterPlan.getName(),
                String.join(", ", aktuelleFutterList.stream().map(Futter::getName).collect(Collectors.toList())),
                String.join(", ", wochentageList.stream().map(Wochentag::getName).collect(Collectors.toList())),
                String.join(", ", futterMengenMap.values().stream().map(String::valueOf).collect(Collectors.toList())),
                String.join(", ", futterzeiten)
        );

        // Daten an das Model übergeben
        model.addAttribute("futterplan", futterplanDTO);
        model.addAttribute("aktuelleFutterList", aktuelleFutterList); // Für die Anzeige der aktuellen Futtermittel
        model.addAttribute("futterMengenMap", futterMengenMap); // Map für Mengeninformationen
        model.addAttribute("futterzeitenList", futterPlanFutterZeitRepository.findAll()); // Dropdown für Futterzeiten
        model.addAttribute("wochentagList", wochenTagRepository.findAll()); // Dropdown für Wochentage
        model.addAttribute("verfügbareFutterList", futterRepository.findAll()); // Dropdown für verfügbare Futtermittel

        return "autharea/futterplan/editfutterplanmanagement";
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
