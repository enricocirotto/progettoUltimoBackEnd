package it.nextdevs.gestioneEventi.controllers;

import it.nextdevs.gestioneEventi.dto.EventoDto;
import it.nextdevs.gestioneEventi.exceptions.BadRequestException;
import it.nextdevs.gestioneEventi.exceptions.EventoNonTrovatoException;
import it.nextdevs.gestioneEventi.models.Evento;
import it.nextdevs.gestioneEventi.service.EventoService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class EventoController {
    @Autowired
    private EventoService eventoService;
    @GetMapping("/eventi")
    public Page<Evento> getAllEventi(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "15") int size,
                                     @RequestParam(defaultValue = "id") String sort)
    {
        return eventoService.getAllEventi(page, size, sort);


    }
    @GetMapping("/eventi/{id}")
    public Evento getEventoById(
            @PathVariable Integer id
    ) {
        Optional<Evento> eventoOptional = eventoService.getEventoById(id);
        if (eventoOptional.isPresent()) {
            return eventoOptional.get();
        } else {
            throw new EventoNonTrovatoException("evento con id "+id +" non trovato");

        }

    }
    @PostMapping("/eventi")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE_DI_EVENTI')")
    public String salvaEvento(@RequestBody @Validated EventoDto eventoDto, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                map(DefaultMessageSourceResolvable::getDefaultMessage).
                reduce("", (s,s2)->s+s2));
        }

        return eventoService.salvaEvento(eventoDto);

    }

    @PutMapping("/eventi/{id}")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE_DI_EVENTI')")
    public Evento aggiornaEvento(@PathVariable Integer id, @RequestBody @Validated EventoDto eventoDto, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(DefaultMessageSourceResolvable::getDefaultMessage).
                    reduce("", (s,s2)->s+s2));
        }

        return eventoService.aggiornaEvento(id, eventoDto);

    }

    @DeleteMapping("/eventi/{id}" )
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE_DI_EVENTI')")
    public String eliminaEvento(@PathVariable Integer id){
        return eventoService.eliminaEvento(id);

    }
}

