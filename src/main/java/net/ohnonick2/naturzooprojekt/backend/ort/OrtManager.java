package net.ohnonick2.naturzooprojekt.backend.ort;

import net.ohnonick2.naturzooprojekt.db.ort.Ort;
import net.ohnonick2.naturzooprojekt.repository.Ortrepository;
import net.ohnonick2.naturzooprojekt.utils.FoodReponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/ort")
public class OrtManager {  // Corrected the class name spelling

    @Autowired
    private Ortrepository ortrepository;

    @GetMapping("/add/{plz}/{name}")
    public ResponseEntity<String> addOrt(@PathVariable String plz, @PathVariable String name) {

        // Check if Ort with the same PLZ already exists
        if (ortrepository.findByPlz(Integer.valueOf(plz)) != null) {
            return new ResponseEntity<>("Ort already exists", HttpStatus.BAD_REQUEST);
        }

        // Parse the plz into an integer
        Integer plzi = Integer.parseInt(plz);

        // Create a new Ort object
        Ort ort1 = new Ort(plzi, name);

        // Save the Ort to the repository
        ortrepository.save(ort1);

        // Return a success message with HTTP 200 OK status
        return new ResponseEntity<>("Ort added", HttpStatus.OK);
    }




}
