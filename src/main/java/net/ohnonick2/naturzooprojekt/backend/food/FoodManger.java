package net.ohnonick2.naturzooprojekt.backend.food;

import com.google.gson.JsonObject;
import net.ohnonick2.naturzooprojekt.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/food")
public class FoodManger {


    public FoodService foodService;

    public FoodManger(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping(value = "/getFutterplan")
    public String getFood() {

        return foodService.getFutterPlaene();
    }



}

