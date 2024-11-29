package LMC.auth.models;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class SecurityUser implements UserDetails {
    private final AuthData authData;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = authData.getRole();
        Set<GrantedAuthority> authorities = new HashSet<>(role.getPermissions());
        authorities.add(role);
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
    public boolean isEnabled() {
        return authData.isEnabled();
    }

    public Profile getProfile() {
        return authData.getProfile();
    }

    public Long getId() {
        return authData.getId();
    }
}
