package net.ohnonick2.naturzooprojekt;


import net.ohnonick2.naturzooprojekt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configuration
@EnableScheduling
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
    private FutterPlanTierRepository futterPlanTierRepositority;

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
