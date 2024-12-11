package net.ohnonick2.naturzooprojekt.service;

import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.PermissionRolleRepository;
import net.ohnonick2.naturzooprojekt.repository.Pflegerrepository;

import net.ohnonick2.naturzooprojekt.repository.RolleUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private Pflegerrepository userRespository;


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RolleUserRepository rolleUserRepository;

    @Autowired
    private PermissionRolleRepository permissionRolleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Benutzer anhand der E-Mail-Adresse finden
        Pfleger user = findUserByUsername(username);

        // Überprüfen, ob der Benutzer gültig ist
        if (validateUser(user)) {
            return new net.ohnonick2.naturzooprojekt.utils.CustomUserDetails(user , rolleUserRepository, permissionRolleRepository);
        }

        throw new UsernameNotFoundException("Ungültige Benutzeranmeldeinformationen.");


    }

    private Pfleger findUserByUsername(String username) {
        return userRespository.findByBenutzername(username);
    }

    private boolean validateUser(Pfleger user) {
        return user != null && user.isEnabled();
    }




}