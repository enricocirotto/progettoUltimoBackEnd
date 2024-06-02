package it.nextdevs.gestioneEventi.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name="eventi")
public class Evento {
    @Id
    @GeneratedValue
    private Integer id;
    private String titolo;
    private String descrizione;
    private LocalDate data;
    private String luogo;
    private Integer postiDisponibili;
    @ManyToMany(mappedBy = "listaEventi")
    private List<Utente> listaUtenti;
}
