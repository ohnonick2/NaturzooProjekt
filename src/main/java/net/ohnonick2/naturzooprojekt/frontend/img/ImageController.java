package net.ohnonick2.naturzooprojekt.frontend.img;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ImageController {

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            // Der Pfad zum 'static' Ordner relativ zur Ressourcenstruktur
            Path filePath = Paths.get("src/main/resources/static/images").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                // Bestimme den MediaType basierend auf der Dateiendung
                String fileExtension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
                MediaType mediaType = switch (fileExtension) {
                    case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
                    case "png" -> MediaType.IMAGE_PNG;
                    case "gif" -> MediaType.IMAGE_GIF;
                    case "bmp" -> MediaType.valueOf("image/bmp");
                    default -> MediaType.IMAGE_JPEG; // Standardwert
                };

                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build(); // Datei nicht gefunden
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Fehler bei URL-Erstellung
        }
    }
}
