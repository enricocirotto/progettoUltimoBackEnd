package it.nextdevs.gestioneEventi.service;

import it.nextdevs.gestioneEventi.dto.UtenteDto;
import it.nextdevs.gestioneEventi.exceptions.BadRequestException;
import it.nextdevs.gestioneEventi.exceptions.EventoNonTrovatoException;
import it.nextdevs.gestioneEventi.exceptions.UtenteNonTrovatoException;
import it.nextdevs.gestioneEventi.models.Evento;
import it.nextdevs.gestioneEventi.models.Utente;
import it.nextdevs.gestioneEventi.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private EventoService eventoService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Utente> getUtenteById(Integer id) {
        return utenteRepository.findById(id);
    }

    public Optional<Utente> getUtenteByEmail(String email) {
        return utenteRepository.findByEmail(email);
    }

    public Page<Utente> getAllUtenti(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return utenteRepository.findAll(pageable);
    }

    public String salvaUtente(UtenteDto utenteDto) {
        if (getUtenteByEmail(utenteDto.getEmail()).isEmpty()) {
            Utente utente = new Utente();
            utente.setNome(utenteDto.getNome());
            utente.setCognome(utenteDto.getCognome());
            utente.setEmail(utenteDto.getEmail());
            utente.setPassword(passwordEncoder.encode(utenteDto.getPassword()));
            utente.setRuoloUtente(utenteDto.getRuoloUtente());
            utenteRepository.save(utente);

            return "Utente con id "+utente.getId()+" creato con successo";
        } else {
            throw new BadRequestException("L'email dell'utente "+utenteDto.getEmail()+" è già presente");
        }
    }

    public String partecipaEvento(Integer idEvento, Integer idUtente) {
        Optional<Utente> utenteOptional = getUtenteById(idUtente);
        if (utenteOptional.isPresent()) {
            Optional<Evento> eventoOptional = eventoService.getEventoById(idEvento);
            if (eventoOptional.isPresent()) {
                Evento evento = eventoOptional.get();
                if (evento.getListaUtenti().size() < evento.getPostiDisponibili()) {
                    if (evento.getListaUtenti().stream().filter(u -> u.getId().equals(idUtente)).toList().isEmpty()) {
                        Utente utente = utenteOptional.get();
                        List<Evento> eventi = utente.getListaEventi();
                        eventi.add(evento);
                        utente.setListaEventi(eventi);
                        utenteRepository.save(utente);
                        return "Partecipazione segnata all'evento "+idEvento;
                    } else {
                        throw new BadRequestException("Stai già partecipando a questo evento");
                    }
                } else {
                    throw new BadRequestException("Non ci sono posti disponibili a questo evento");
                }
            } else {
                throw new EventoNonTrovatoException("Evento con id "+idEvento+" non trovato");
            }
        } else {
            throw new UtenteNonTrovatoException("Utente con id "+idUtente+" non trovato");
        }
    }
}
