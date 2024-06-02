package it.nextdevs.gestioneEventi.service;

import it.nextdevs.gestioneEventi.dto.EventoDto;
import it.nextdevs.gestioneEventi.exceptions.EventoNonTrovatoException;
import it.nextdevs.gestioneEventi.models.Evento;
import it.nextdevs.gestioneEventi.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;

    public Page<Evento> getAllEventi(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return eventoRepository.findAll(pageable);
    }

    public Optional<Evento> getEventoById(Integer id) {
        return eventoRepository.findById(id);
    }

    public String salvaEvento(EventoDto eventoDto) {
        Evento evento = new Evento();
        evento.setTitolo(eventoDto.getTitolo());
        evento.setDescrizione(eventoDto.getDescrizione());
        evento.setLuogo(eventoDto.getLuogo());
        evento.setData(eventoDto.getData());
        evento.setPostiDisponibili(eventoDto.getPostiDisponibili());
        eventoRepository.save(evento);
        return "Evento con id "+evento.getId()+" creato";
    }

    public Evento aggiornaEvento(Integer id, EventoDto eventoDto) {
        Optional<Evento> eventoOptional = getEventoById(id);
        if (eventoOptional.isPresent()) {
            Evento evento = eventoOptional.get();
            evento.setTitolo(eventoDto.getTitolo());
            evento.setDescrizione(eventoDto.getDescrizione());
            evento.setLuogo(eventoDto.getLuogo());
            evento.setData(eventoDto.getData());
            evento.setPostiDisponibili(eventoDto.getPostiDisponibili());
            return eventoRepository.save(evento);
        } else {
            throw new EventoNonTrovatoException("Evento con id "+id+" non trovato");
        }
    }

    public String eliminaEvento(Integer id) {
        Optional<Evento> eventoOptional = getEventoById(id);
        if (eventoOptional.isPresent()) {
            eventoRepository.delete(eventoOptional.get());
            return "Evento con id "+id+" eliminato";
        } else {
            throw new EventoNonTrovatoException("Evento con id "+id+" non trovato");
        }
    }
}
