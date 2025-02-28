package net.ohnonick2.naturzooprojekt.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ohnonick2.naturzooprojekt.db.futter.FutterPlan;
import net.ohnonick2.naturzooprojekt.db.futter.FutterPlanTier;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import net.ohnonick2.naturzooprojekt.repository.*;
import net.ohnonick2.naturzooprojekt.utils.FoodReponse;
import org.graalvm.nativeimage.c.struct.AllowNarrowingCast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FoodService {

    @Autowired
    private FutterRepositority futterRepositority;

    @Autowired
    private FutterPlanRepository futterplanRepository;

    @Autowired
    private Tierartrepository tierartrepository;

    @Autowired
    private FutterPlanTierRepository futterPlanTierRepositority;

    @Autowired
    private FutterZeitRepository futterZeitRepository;

    @Autowired
    private FutterPlanFutterZeitRepository futterPlanFutterZeitRepository;

    @Autowired
    private FutterPlanWochentagRepository futterPlanWochentagRepository;

    @Autowired
    private WochenTagRepository wochentagRepository;

    @Autowired
    private LieferantRepository lieferantRepository;


    public String getFutterPlaene() {
        JsonObject response = new JsonObject();
        JsonArray futterPlaeneArray = new JsonArray();

        List<FutterPlan> futterPlaene = futterplanRepository.findAll();
        if (futterPlaene.isEmpty()) {
            return new FoodReponse(false, "Keine Futterplaene vorhanden").toJson();
        }

        for (FutterPlan futterPlan : futterPlaene) {
            JsonObject futterPlanJson = new JsonObject();
            futterPlanJson.addProperty("name", futterPlan.getName());

            List<FutterPlanTier> tiere = futterPlanTierRepositority.findAll().stream().sorted().toList();

            JsonArray tiereArray = new JsonArray();
            for (FutterPlanTier tier : tiere) {
               JsonObject tierJson = new JsonObject();
                tierJson.addProperty("tier", tier.getTier().getId());
                tiereArray.add(tierJson);
            }
            futterPlanJson.add("tiere", tiereArray);
            futterPlaeneArray.add(futterPlanJson);
        }

        response.add("futterPlaene", futterPlaeneArray);
        return response.toString();
    }



}
