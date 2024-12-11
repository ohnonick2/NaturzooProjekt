package net.ohnonick2.naturzooprojekt.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ohnonick2.naturzooprojekt.db.permission.Permission;
import net.ohnonick2.naturzooprojekt.db.permission.PermissionRolle;
import net.ohnonick2.naturzooprojekt.db.permission.Rolle;
import net.ohnonick2.naturzooprojekt.db.permission.RolleUser;
import net.ohnonick2.naturzooprojekt.db.user.Pfleger;
import net.ohnonick2.naturzooprojekt.repository.PermissionRolleRepository;
import net.ohnonick2.naturzooprojekt.repository.RolleUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class CustomUserDetails implements UserDetails {

    private Pfleger user;
    private boolean timeout;

    private RolleUserRepository rolleUserRepository;

    private PermissionRolleRepository permissionRolleRepository;


    public CustomUserDetails(Pfleger user , RolleUserRepository rolleUserRepository, PermissionRolleRepository permissionRolleRepository) {
        this.user = user;
        this.timeout = false;
        this.rolleUserRepository = rolleUserRepository;
        this.permissionRolleRepository = permissionRolleRepository;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        try {
            Long userId = Long.valueOf(user.getId());
            RolleUser rolleUser = rolleUserRepository.findByUserId(userId);

            if (rolleUser == null) {
                return Collections.emptyList();
            }

            Rolle rolle = rolleUser.getRolle();
            List<PermissionRolle> permissionRolles = permissionRolleRepository.findByRolle(rolle);

            List<GrantedAuthority> authorities = new ArrayList<>();
            if (permissionRolles != null && !permissionRolles.isEmpty()) {
                for (PermissionRolle permissionRolle : permissionRolles) {
                    Permission permission = permissionRolle.getPermission();
                    if (permission != null) {
                        authorities.add(new SimpleGrantedAuthority(permission.getPermission()));
                    }
                }
            }

            return authorities;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }



    @Override
    public String getUsername() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("benutzername", user.getBenutzername());
        jsonObject.addProperty("id", user.getId());
        return jsonObject.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        return user.getLockedUntil() == null ||
                user.getLockedUntil().isBefore(LocalDateTime.now(ZoneId.systemDefault()));
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public boolean isTimeout() {
        return timeout;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }
}