package net.ohnonick2.naturzooprojekt.frontend.aktivität;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ActivityController {

    @GetMapping("/aktivität")
    public String showActivity() {
        return "aktivität";
    }


}
