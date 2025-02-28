package net.ohnonick2.naturzooprojekt.frontend.dashboard;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.db.futter.*;
import net.ohnonick2.naturzooprojekt.db.notification.Notification;
import net.ohnonick2.naturzooprojekt.db.revier.RevierPfleger;
import net.ohnonick2.naturzooprojekt.db.revier.RevierTier;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.db.wochentag.Wochentag;
import net.ohnonick2.naturzooprojekt.repository.*;
import net.ohnonick2.naturzooprojekt.service.CustomUserDetailsService;
import net.ohnonick2.naturzooprojekt.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class Dashboard {

    @Autowired
    private RevierPflegerRepository revierPflegerRepository;

    @Autowired
    private Pflegerrepository pflegerrepository;

    @Autowired
    private RevierTierRepository revierTierRepository;

    @Autowired
    private FutterPlanRepository futterPlanRepository;
    @Autowired
    private FutterplanPflegerRepository futterplanPflegerRepository;
    @Autowired
    private FutterPlanWochentagRepository futterPlanWochentagRepository;
    @Autowired
    private WochenTagRepository wochenTagRepository;
    @Autowired
    private FutterZeitRepository futterZeitRepository;
    @Autowired
    private FutterPlanFutterZeitRepository futterPlanFutterZeitRepository;
    @Autowired
    private FutterplanFutterRepository futterplanFutterRepository;

    @Autowired
    private NotificationService notificationService;



    @GetMapping("/dashboard")
    public String showDashboard(Model model) {

        if (SecurityContextHolder.getContext().getAuthentication() == null) return "redirect:/login";
        CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService();
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        JsonObject jsonObject = JsonParser.parseString(username).getAsJsonObject();
        String benutzername = jsonObject.get("benutzername").getAsString();
        //ersten Buchstaben groß machen
        benutzername = benutzername.substring(0, 1).toUpperCase() + benutzername.substring(1);
        model.addAttribute("username", benutzername);
        model.addAttribute("hasBirthday", jsonObject.get("hasBirthday").getAsBoolean());

        Pfleger pfleger = pflegerrepository.findPflegerById(jsonObject.get("id").getAsLong());
        List<RevierPfleger> revierPfleger = revierPflegerRepository.findByPfleger(pfleger);
        model.addAttribute("revierText", formatRevierList(revierPfleger));

        int anzahlTiere = 0;
        for (RevierPfleger rp : revierPfleger) {
            List<RevierTier> revierTiere = revierTierRepository.findAllByRevierId(rp.getRevier());
            anzahlTiere += revierTiere.size();
        }

        List<FutterplanPfleger> futterplanPfleger = futterplanPflegerRepository.findByPflegerId(pfleger.getId());
        List<FutterPlan> zugewieseneFutterplaene = futterplanPfleger.stream()
                .map(FutterplanPfleger::getFutterPlan)
                .collect(Collectors.toList());

        HashMap<Long, HashMap<Futter, Integer>> futterMitMengeMap = new HashMap<>();

        for (FutterPlan futterPlan : zugewieseneFutterplaene) {
            HashMap<Futter, Integer> futterMitMenge = new HashMap<>();
            List<FutterplanFutter> futterplanFutters = futterplanFutterRepository.findByFutterplanId(futterPlan.getId());

            for (FutterplanFutter futterplanFutter : futterplanFutters) {
                futterMitMenge.put(futterplanFutter.getFutter(), futterplanFutter.getMenge());
            }
            futterMitMengeMap.put(futterPlan.getId(), futterMitMenge);
        }

        HashMap<Long, List<String>> futterPlanFutterZeit = new HashMap<>();
        for (FutterPlan futterPlan : zugewieseneFutterplaene) {
            List<String> futterZeitList = futterPlanFutterZeitRepository.findByFutterplanId(futterPlan.getId())
                    .stream()
                    .map(futterPlanFutterZeit1 -> futterPlanFutterZeit1.getFutterZeit().getUhrzeit())
                    .collect(Collectors.toList());

            futterPlanFutterZeit.put(futterPlan.getId(), futterZeitList);
        }

        HashMap<Long, List<Wochentag>> futterPlanWochentag = new HashMap<>();
        for (FutterPlan futterPlan : zugewieseneFutterplaene) {
            List<Wochentag> wochentagList = futterPlanWochentagRepository.findByFutterplan(futterPlan).stream()
                    .map(FutterPlanWochentag::getWochentag)
                    .collect(Collectors.toList());
            futterPlanWochentag.put(futterPlan.getId(), wochentagList);
        }
        List<FutterplanDTO> futterplanDTOs = zugewieseneFutterplaene.stream().map(futterPlan ->
                new FutterplanDTO(
                        futterPlan.getId(),
                        futterPlan.getName(),
                        futterMitMengeMap.getOrDefault(futterPlan.getId(), new HashMap<>()),
                        futterPlanWochentag.getOrDefault(futterPlan.getId(), new ArrayList<>()).stream().map(Wochentag::getName).collect(Collectors.toList()),
                        futterPlanFutterZeit.getOrDefault(futterPlan.getId(), new ArrayList<>())
                )
        ).collect(Collectors.toList());

        model.addAttribute("futterplaene", futterplanDTOs);

        List<Notification> notifications = notificationService.getNotificationsForPfleger(pfleger.getId());

        model.addAttribute("notifications", notifications);

        model.addAttribute("id" , jsonObject.get("id").getAsLong());








        model.addAttribute("anzahlTiere", anzahlTiere);
        model.addAttribute("revierPfleger", revierPflegerRepository);

        return "autharea/dashboard/dashboard";
    }

    public String formatRevierList(List<RevierPfleger> revierPfleger) {
        if (revierPfleger.isEmpty()) {
            return "🚨 Dir wurde noch kein Revier zugewiesen.";
        }

        List<String> revierNamen = revierPfleger.stream()
                .map(rp -> rp.getRevier().getName())
                .collect(Collectors.toList());

        if (revierNamen.size() == 1) {
            return revierNamen.get(0);
        } else if (revierNamen.size() == 2) {
            return String.join(" und ", revierNamen);
        } else {
            return String.join(", ", revierNamen.subList(0, revierNamen.size() - 1))
                    + " und " + revierNamen.get(revierNamen.size() - 1);
        }
    }

    public class FutterplanDTO {
        private Long id;
        private String name;

        private HashMap<Futter, Integer> futtermitMenge;
        private List<String> wochentage;
        private List<String> uhrzeiten;

        public FutterplanDTO(Long id, String name, HashMap<Futter, Integer> futtermitMenge, List<String> wochentage, List<String> uhrzeiten) {
            this.id = id;
            this.name = name;
            this.futtermitMenge = futtermitMenge;
            this.wochentage = wochentage;
            this.uhrzeiten = uhrzeiten;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public HashMap<Futter, Integer> getFuttermitMenge() {
            return futtermitMenge;
        }

        public List<String> getWochentage() {
            return wochentage;
        }

        public List<String> getUhrzeiten() {
            return uhrzeiten;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setFuttermitMenge(HashMap<Futter, Integer> futtermitMenge) {
            this.futtermitMenge = futtermitMenge;
        }

        public void setWochentage(List<String> wochentage) {
            this.wochentage = wochentage;
        }

        public void setUhrzeiten(List<String> uhrzeiten) {
            this.uhrzeiten = uhrzeiten;
        }


    }



}



