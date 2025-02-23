package net.ohnonick2.naturzooprojekt.frontend;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import net.ohnonick2.naturzooprojekt.db.futter.*;
import net.ohnonick2.naturzooprojekt.db.wochentag.Wochentag;
import net.ohnonick2.naturzooprojekt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import javax.net.ssl.SSLEngineResult;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class Index {

    @Autowired
    private FutterplanFutterRepository futterplanFutterRepository;
    @Autowired
    private FutterPlanFutterZeitRepository futterPlanFutterZeitRepository;

    @GetMapping("/index")
    public String showIndexPage(Model model , HttpServletRequest request) {

        if (request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) != null) {
            return "redirect:/login?error";
        }
        if (request.getParameter("session") != null && request.getParameter("session").equals("invalid")) {
            return "redirect:/login?invalidession";
        }
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            JsonObject jsonObject = JsonParser.parseString(username).getAsJsonObject();
            String benutzername = jsonObject.get("benutzername").getAsString();
            model.addAttribute("username", benutzername);
            Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            String authoritiesList = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));
            model.addAttribute("authorities", authoritiesList);
            return "auth/userindex";
        }
        return "redirect:/login";
    }

    @Autowired
    private FutterZeitRepository futterZeitRepository;
    @Autowired
    private WochenTagRepository wochenTagRepository;

    @Autowired
    private FutterPlanRepository futterPlanRepository;

    @Autowired
    private FutterPlanWochentagRepository futterPlanWochentagRepository;

    @GetMapping("/")
    public String showFoodPlan(Model model) {
        List<FutterPlan> futterplaene = futterPlanRepository.findAll();
        List<Wochentag> wochentage = wochenTagRepository.findAll();
        List<Integer> stunden = IntStream.rangeClosed(6, 19).boxed().toList();
        String kalenderWoche = getCalendarWeek();

        // Map für Futterpläne und deren zugehörige Futtermengen
        HashMap<Long, HashMap<Futter, Integer>> futterMitMengeMap = new HashMap<>();

        for (FutterPlan futterPlan : futterplaene) {
            HashMap<Futter, Integer> futterMitMenge = new HashMap<>();
            List<FutterplanFutter> futterplanFutters = futterplanFutterRepository.findByFutterplanId(futterPlan.getId());

            for (FutterplanFutter futterplanFutter : futterplanFutters) {
                futterMitMenge.put(futterplanFutter.getFutter(), futterplanFutter.getMenge());
            }
            futterMitMengeMap.put(futterPlan.getId(), futterMitMenge);
        }

        // Map für Futterpläne und deren Fütterungszeiten
        HashMap<Long, List<String>> futterPlanFutterZeit = new HashMap<>();
        for (FutterPlan futterPlan : futterplaene) {
            List<String> futterZeitList = futterPlanFutterZeitRepository.findByFutterplanId(futterPlan.getId())
                    .stream()
                    .map(futterPlanFutterZeit1 -> futterPlanFutterZeit1.getFutterZeit().getUhrzeit())
                    .collect(Collectors.toList());

            futterPlanFutterZeit.put(futterPlan.getId(), futterZeitList);
        }


        //mache eine List an Wochentagen die in futterplanwochentag sind
        HashMap<Long, List<Wochentag>> futterPlanWochentag = new HashMap<>();
        for (FutterPlan futterPlan : futterplaene) {
            List<Wochentag> wochentagList = futterPlanWochentagRepository.findByFutterplan(futterPlan).stream()
                    .map(futterPlanWochentag1 -> futterPlanWochentag1.getWochentag())
                    .collect(Collectors.toList());
            futterPlanWochentag.put(futterPlan.getId(), wochentagList);
        }







        // Erstellen der DTOs mit Korrektur der Liste
        List<FutterplanDTO> futterplanDTOs = futterplaene.stream().map(futterPlan ->
                new FutterplanDTO(
                        futterPlan.getId(),
                        futterPlan.getName(),
                        futterMitMengeMap.getOrDefault(futterPlan.getId(), new HashMap<>()),
                        futterPlanWochentag.getOrDefault(futterPlan.getId(), new ArrayList<>()).stream().map(Wochentag::getName).collect(Collectors.toList()),
                        futterPlanFutterZeit.getOrDefault(futterPlan.getId(), new ArrayList<>())
                )
        ).collect(Collectors.toList());

        // Debugging: Ausgabe in der Konsole
        futterplanDTOs.forEach(futterplanDTO -> {
            System.out.println("Futterplan Name: " + futterplanDTO.getName());
            System.out.println("ID: " + futterplanDTO.getId());
            System.out.println("Wochentage: " + futterplanDTO.getWochentage());
            System.out.println("Fütterungszeiten: " + futterplanDTO.getUhrzeiten());
            System.out.println("Futter mit Mengen: " + futterplanDTO.getFuttermitMenge());
        });

        model.addAttribute("futterplanList", futterplanDTOs);
        model.addAttribute("stunden", stunden);
        model.addAttribute("kalenderWoche", kalenderWoche);
        model.addAttribute("wochentage", wochentage);



        return "index";
    }



    // Hilfsmethode zur Berechnung der aktuellen Kalenderwoche
    private String getCalendarWeek() {
        LocalDate today = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.GERMANY);
        int weekNumber = today.get(weekFields.weekOfWeekBasedYear());
        return "KW " + weekNumber + " (" + today.with(DayOfWeek.MONDAY) + " - " + today.with(DayOfWeek.SUNDAY) + ")";
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
