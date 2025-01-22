package net.ohnonick2.naturzooprojekt.service;

import net.ohnonick2.naturzooprojekt.db.aktivitaet.Aktivitaet;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.AktivitaetRepository;
import net.ohnonick2.naturzooprojekt.utils.ActivityAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AktivitaetService {

    @Autowired
    private AktivitaetRepository aktivitaetRepository;

    public void logActivity(ActivityAction action, Pfleger pfleger, String details) {
        Aktivitaet aktivitaet = new Aktivitaet();
        aktivitaet.setAction(action);
       // aktivitaet.setPfleger(pfleger);
        aktivitaet.setTimestamp(LocalDateTime.now());
        aktivitaet.setDetails(details);

        aktivitaetRepository.save(aktivitaet);
    }
}