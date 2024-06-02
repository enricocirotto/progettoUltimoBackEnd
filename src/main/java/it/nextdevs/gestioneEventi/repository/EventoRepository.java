package it.nextdevs.gestioneEventi.repository;

import it.nextdevs.gestioneEventi.models.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento,Integer> {
}
