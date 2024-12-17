package net.ohnonick2.naturzooprojekt.utils;

import com.fasterxml.jackson.core.io.SegmentedStringWriter;
import com.google.gson.JsonObject;

public class FoodReponse {

    public boolean success;
    public String message;

    public FoodReponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", success);
        jsonObject.addProperty("message", message);
        return jsonObject.getAsString();
    }



}
