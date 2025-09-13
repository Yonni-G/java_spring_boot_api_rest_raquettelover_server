package com.yonni.raquettelover.security;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private String firstName;

    // Constructeur
    public CustomUserDetails(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities, String firstName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.firstName = firstName;
    }

    // Getter spécifique
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }   


    // Implémentation des méthodes UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }
}
