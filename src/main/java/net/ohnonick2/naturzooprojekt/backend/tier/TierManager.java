package net.ohnonick2.naturzooprojekt.backend.tier;

import net.ohnonick2.naturzooprojekt.db.futter.FutterPlanTier;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import net.ohnonick2.naturzooprojekt.db.tier.TierArt;
import net.ohnonick2.naturzooprojekt.repository.FutterPlanRepository;
import net.ohnonick2.naturzooprojekt.repository.FutterPlanTierRepositority;
import net.ohnonick2.naturzooprojekt.repository.Tierartrepository;
import net.ohnonick2.naturzooprojekt.repository.Tierrespository;
import net.ohnonick2.naturzooprojekt.utils.TierGeschlecht;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Random;

@RestController
@RequestMapping(value = "/api/tier")
public class TierManager {

    @Autowired
    private Tierrespository tierrespository;

    @Autowired
    private Tierartrepository tierartrepository;

    @Autowired
    private FutterPlanTierRepositority futterPlanTierRepositority;

    @Autowired
    private FutterPlanRepository futterPlanRepository;

    @GetMapping(value = "/add")
    public String addTier() {

        int i = new Random().nextInt(1,6);

        if (tierrespository.findByName("Elefant" + i) != null) {
            return "Tier already exists";
        }

        Tier tier = new Tier("Elefant" + new Random().nextInt(1,6), LocalDate.now() , null , TierGeschlecht.MAENNLICH , tierartrepository.findByName("Elefant"));
        tierrespository.save(tier);
        FutterPlanTier futterPlanTier = new FutterPlanTier(futterPlanRepository.findFutterplanById(1L) , tier );
        futterPlanTierRepositority.save(futterPlanTier);


        return "Tier added";
    }

}
