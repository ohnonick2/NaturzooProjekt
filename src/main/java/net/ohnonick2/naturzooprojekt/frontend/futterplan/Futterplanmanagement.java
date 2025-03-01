package net.ohnonick2.naturzooprojekt.frontend.futterplan;

import net.ohnonick2.naturzooprojekt.db.futter.*;
import net.ohnonick2.naturzooprojekt.db.revier.Revier;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
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
    @Autowired
    private Tierrespository tierrespository;

    @Autowired
    private RevierRepository revierRepository;

    @Autowired
    private FutterPlanTierRepository futterPlanTierRepositority;
    @Autowired
    private FutterplanPflegerRepository futterplanPflegerRepository;


    /**
     * Zeigt die Liste aller Futterpläne
     */
    @GetMapping("/futterplan")
    public String futterplan(Model model) {
        List<FutterplanDTO> futterplanDTOs = new ArrayList<>();

        // Alle Futterpläne abrufen
        List<FutterPlan> futterPlaene = futterPlanRepository.findAll();

        for (FutterPlan futterPlan : futterPlaene) {
            // Wochentage abrufen (ggf. "Keine")
            String wochentage = futterPlanWochentagRepository.findByFutterplan(futterPlan).stream()
                    .map(wt -> wt.getWochentag().getName())
                    .collect(Collectors.joining(", "));
            if (wochentage.isEmpty()) wochentage = "-";

            // Futtermittel & Mengen sammeln (ggf. "Keine")
            List<FutterplanFutter> futterplanFutterListe = futterplanFutterRepository.findByFutterplanId(futterPlan.getId());
            String futter = futterplanFutterListe.stream()
                    .map(f -> f.getFutter().getName())
                    .collect(Collectors.joining(", "));
            String menge = futterplanFutterListe.stream()
                    .map(f -> String.valueOf(f.getMenge()))
                    .collect(Collectors.joining(", "));

            if (futter.isEmpty()) futter = "-";
            if (menge.isEmpty()) menge = "-";

            // Futterzeiten abrufen (ggf. "Keine")
            String futterzeiten = futterPlanFutterZeitRepository.findByFutterplanId(futterPlan.getId()).stream()
                    .map(fz -> fz.getFutterZeit().getUhrzeit().toString())
                    .collect(Collectors.joining(", "));
            if (futterzeiten.isEmpty()) futterzeiten = "-";

            // Pfleger abrufen
            List<FutterPlanTier> futterPlanTiers = futterPlanTierRepositority.findByFutterplan(futterPlan);
            List<Pfleger> pflegerList = futterplanPflegerRepository.findByFutterPlanId(futterPlan.getId()).stream()
                    .map(FutterplanPfleger::getPfleger)
                    .collect(Collectors.toList());



            // DTO zur Liste hinzufügen
            futterplanDTOs.add(new FutterplanDTO(futterPlan.getId(), futterPlan.getName(), futter, wochentage, menge, futterzeiten).setPflegerList(pflegerList));
        }
        model.addAttribute("futterplan", new FutterPlan());
        model.addAttribute("wochentagList", wochenTagRepository.findAll());
        model.addAttribute("verfügbareFutterList", futterRepository.findAll());

        model.addAttribute("futterplanList", futterplanDTOs);
        return "autharea/futterplan/futterplanmanagement";
    }


    /**
     * Zeigt das Bearbeitungsformular für einen Futterplan
     */
    @GetMapping("/editFutterplan/{id}")
    public String editFutterplan(@PathVariable Long id, Model model) {
        FutterPlan futterPlan = futterPlanRepository.findById(id).orElse(null);
        if (futterPlan == null) {
            return "redirect:/futterplan"; // Falls nicht gefunden, zurück zur Liste
        }

        List<Futter> verfügbareFutter = futterRepository.findAll();
        List<FutterplanFutter> ausgewählteFutter = futterplanFutterRepository.findByFutterplanId(id);

        List<Wochentag> verfügbareWochentage = wochenTagRepository.findAll();
        List<Long> ausgewählteWochentage = futterPlanWochentagRepository.findByFutterplan(futterPlan)
                .stream().map(wt -> wt.getWochentag().getId()).collect(Collectors.toList());

        List<FutterPlanFutterZeit> verfügbareUhrzeiten = futterPlanFutterZeitRepository.findAll();
        List<String> futterzeitenList = futterPlanFutterZeitRepository.findByFutterplanId(id)
                .stream().map(fz -> fz.getFutterZeit().getUhrzeit().toString()).collect(Collectors.toList());

        model.addAttribute("futterplan", futterPlan);
        model.addAttribute("verfügbareFutterList", verfügbareFutter);
        model.addAttribute("ausgewählteFutter", ausgewählteFutter);
        model.addAttribute("wochentagList", verfügbareWochentage);
        model.addAttribute("ausgewählteWochentage", ausgewählteWochentage);
        model.addAttribute("verfügbareUhrzeiten", verfügbareUhrzeiten);
        model.addAttribute("futterzeitenList", futterzeitenList);

        return "autharea/futterplan/editfutterplanmanagement";
    }




    @GetMapping("/getFutterplan/{id}")
    public String getFutterplan(@PathVariable Long id, Model model) {
        FutterPlan futterPlan = futterPlanRepository.findById(id).orElse(null);

        if (futterPlan == null) {
            return "redirect:/";
        }

        List<FutterplanFutter> futterplanFutters = futterplanFutterRepository.findByFutterplanId(id);
        List<FutterPlanWochentag> futterPlanWochentags = futterPlanWochentagRepository.findByFutterplan(futterPlan);
        List<FutterPlanFutterZeit> futterPlanFutterZeits = futterPlanFutterZeitRepository.findByFutterplanId(id);
        List<FutterPlanTier> futterPlanTiers = futterPlanTierRepositority.findByFutterplan(futterPlan);

        List<Tier> tierList = new ArrayList<>();
        List<Revier> revierList = new ArrayList<>();



        model.addAttribute("futterPlan", futterPlan);
        model.addAttribute("futterplanFutters", futterplanFutters);
        model.addAttribute("futterPlanWochentags", futterPlanWochentags);
        model.addAttribute("futterPlanFutterZeits", futterPlanFutterZeits);
        model.addAttribute("tierList", tierList);
        model.addAttribute("revierList", revierList);


        return "autharea/futterplan/getfutterplanmanagement";
    }


    @GetMapping("/addFutterplan")
    public String addFutterplan(Model model) {
        model.addAttribute("futterplan", new FutterPlan());
        model.addAttribute("wochentagList", wochenTagRepository.findAll());
        model.addAttribute("verfügbareFutterList", futterRepository.findAll());
        return "autharea/futterplan/addfutterplanmanagement";
    }











    /**
     * DTO-Klasse für Thymeleaf
     */
    public class FutterplanDTO {
        private Long id;
        private String name;
        private String futter;
        private String wochentage;
        private String futterZeiten;
        private String menge;
        private List<Pfleger> pflegerList;

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

        public List<Pfleger> getPflegerList() { return pflegerList; }
        public FutterplanDTO setPflegerList(List<Pfleger> pflegerList) {

            this.pflegerList = pflegerList;
            return this;
        }

    }
}
