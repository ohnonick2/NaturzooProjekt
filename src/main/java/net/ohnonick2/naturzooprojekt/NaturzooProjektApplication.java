package net.ohnonick2.naturzooprojekt;


import net.ohnonick2.naturzooprojekt.db.adresse.Adresse;
import net.ohnonick2.naturzooprojekt.db.futter.*;
import net.ohnonick2.naturzooprojekt.db.lieferant.Lieferant;
import net.ohnonick2.naturzooprojekt.db.ort.Ort;
import net.ohnonick2.naturzooprojekt.db.permission.Permission;
import net.ohnonick2.naturzooprojekt.db.permission.PermissionRolle;
import net.ohnonick2.naturzooprojekt.db.permission.Rolle;
import net.ohnonick2.naturzooprojekt.db.permission.RolleUser;
import net.ohnonick2.naturzooprojekt.db.revier.Revier;
import net.ohnonick2.naturzooprojekt.db.revier.RevierTier;
import net.ohnonick2.naturzooprojekt.db.tier.Tier;
import net.ohnonick2.naturzooprojekt.db.tier.TierArt;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.db.wochentag.Wochentag;
import net.ohnonick2.naturzooprojekt.repository.*;
import net.ohnonick2.naturzooprojekt.utils.TierGeschlecht;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

@SpringBootApplication
@Configuration
public class NaturzooProjektApplication {


    @Autowired
    private Tierrespository tierrespository;

    @Autowired
    private Tierartrepository tierartrepository;

    @Autowired
    private Ortrepository ortrepository;

    @Autowired
    private Pflegerrepository pflegerrepository;

    @Autowired
    private FutterPlanRepository futterplanRepository;

    @Autowired
    private FutterPlanTierRepositority futterPlanTierRepositority;

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private LieferantRepository lieferantRepository;

    @Autowired
    private FutterRepositority futterRepositority;

    @Autowired
    private FutterZeitRepository futterZeitRepository;

    @Autowired
    private FutterPlanFutterZeitRepository futterPlanFutterZeitRepository;

    @Autowired
    private FutterPlanWochentagRepository futterPlanWochentagRepository;

    @Autowired
    private WochenTagRepository wochentagRepository;

    @Autowired
    private RevierTierRespository revierTierRespository;

    @Autowired
    private RevierRepository revierRepository;

    @Autowired
    private RolleRepository rolleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionRolleRepository permissionRolleRepository;

    @Autowired
    private RolleUserRepository rolleUserRepository;




    public static void main(String[] args) {

        SpringApplication.run(NaturzooProjektApplication.class, args);
    }





}
