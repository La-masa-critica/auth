package LMC.auth.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class SecurityUser implements UserDetails {
    private final AuthData authData;

    public SecurityUser(AuthData authData) {
        this.authData = authData;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        // Agregamos el rol
        Role role = authData.getRole();
        authorities.add(role);
        // Agregamos los permisos del rol
        authorities.addAll(role.getPermissions());
        return authorities;
    }

    @Override
    public String getPassword() {
        return authData.getPassword();
    }

    @Override
    public String getUsername() {
        return authData.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return authData.isEnabled();
    }

    public Profile getProfile() {
        return authData.getProfile();
    }
}