package net.ohnonick2.naturzooprojekt.frontend.dashboard;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.ohnonick2.naturzooprojekt.repository.Pflegerrepository;
import net.ohnonick2.naturzooprojekt.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Dashboard {



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
        model.addAttribute("hasBirthday" , jsonObject.get("hasBirthday").getAsBoolean());

        return "autharea/dashboard/dashboard";
    }

}
