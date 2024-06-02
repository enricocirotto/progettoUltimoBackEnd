package it.nextdevs.gestioneEventi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.nextdevs.gestioneEventi.enums.RuoloUtente;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table (name = "utenti")
@Data
@Entity
public class Utente implements UserDetails {
    @GeneratedValue
    @Id
    private Integer id;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private RuoloUtente ruoloUtente;
    @ManyToMany
    @JoinTable(
            name="prenotazioni",
            joinColumns = @JoinColumn(name="utente_id"),
            inverseJoinColumns = @JoinColumn(name="evento_id")
    )
    @JsonIgnore
    private List<Evento> listaEventi;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ruoloUtente.name()));
    }

    @Override
    public String getUsername() {
        return String.valueOf(id);
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
