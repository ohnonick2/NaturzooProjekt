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

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {

            Ort ort = new Ort(123457, "Testort");
            ortrepository.save(ort);

            LocalDate geburtsdatum = LocalDate.of(2021, 1, 1);

            TierArt tierArt = new TierArt("Elefant");
            tierartrepository.save(tierArt);

            Tier tier = new Tier("Hans",   geburtsdatum  , null, TierGeschlecht.MAENNLICH, tierArt);

            tierrespository.save(tier);



            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = bCryptPasswordEncoder.encode("test");
            Pfleger pfleger = new Pfleger("Hans", "Müller", encodedPassword, ort);
            pflegerrepository.save(pfleger);



            FutterPlan futterplan = new FutterPlan(new Date(), 100);
            futterplanRepository.save(futterplan);

            FutterPlanTier futterPlanTier = new FutterPlanTier(futterplan, tier);

            futterPlanTierRepositority.save(futterPlanTier);

            Adresse adresse = new Adresse("Teststraße", "12345", ort);
            adresseRepository.save(adresse);


            Lieferant lieferant = new Lieferant("Hans GmbH" , adresse , "12345" , "Hans");
            lieferantRepository.save(lieferant);

            Futter futter = new Futter("Heu", 10, lieferant);
            futterRepositority.save(futter);

            FutterZeit futterZeit = new FutterZeit("12:00");
            futterZeitRepository.save(futterZeit);

            FutterPlanFutterZeit futterPlanFutterZeit = new FutterPlanFutterZeit(futterZeit, futterplan);

            futterPlanFutterZeitRepository.save(futterPlanFutterZeit);


            Wochentag wochentag = new Wochentag("Montag");
            wochentagRepository.save(wochentag);

            FutterPlanWochentag futterPlanWochentag = new FutterPlanWochentag(futterplan , wochentag);
            futterPlanWochentagRepository.save(futterPlanWochentag);

            Revier revier = new Revier("Testrevier");
            revierRepository.save(revier);

            RevierTier revierTier = new RevierTier(revier, tier);
            revierTierRespository.save(revierTier);


            Rolle superAdmin = new Rolle("SuperAdmin");
            Rolle rolle = new Rolle("Admin");
            Rolle rolle2 = new Rolle("Pfleger");


            Permission all = new Permission("*" , "Alle Rechte");
            Permission permission = new Permission("READ" , "Lesen");
            Permission permission2 = new Permission("WRITE" , "Schreiben");

            PermissionRolle permissionRolle = new PermissionRolle(permission, rolle);
            PermissionRolle permissionRolle2 = new PermissionRolle(permission2, rolle2);
            PermissionRolle permissionRolle1 = new PermissionRolle(permission, superAdmin);
            PermissionRolle permissionRolle3 = new PermissionRolle(all, superAdmin);

            rolleRepository.save(superAdmin);
            rolleRepository.save(rolle);
            rolleRepository.save(rolle2);

            permissionRepository.save(all);
            permissionRepository.save(permission);
            permissionRepository.save(permission2);

            permissionRolleRepository.save(permissionRolle3);
            permissionRolleRepository.save(permissionRolle);
            permissionRolleRepository.save(permissionRolle2);
            permissionRolleRepository.save(permissionRolle1);

            RolleUser rolleUser = new RolleUser(rolle2 , pfleger);


            rolleUserRepository.save(rolleUser);









        };

    }




}
