package net.ohnonick2.naturzooprojekt.frontend;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.HttpServletRequest;
import net.ohnonick2.naturzooprojekt.db.futter.FutterZeit;
import net.ohnonick2.naturzooprojekt.repository.FutterZeitRepository;
import net.ohnonick2.naturzooprojekt.repository.WochenTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class Index {

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

    @GetMapping("/")
    public String showFoodPlan(Model model) {
        List<FutterZeit> futterZeiten = futterZeitRepository.findAll();
        model.addAttribute("futterZeiten", futterZeiten);

        model.addAttribute("wochentage", wochenTagRepository.findAll());

        return "index";
    }


}
