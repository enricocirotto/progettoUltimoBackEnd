package it.nextdevs.gestioneEventi.controllers;

import it.nextdevs.gestioneEventi.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtenteController {
    @Autowired
    private UtenteService utenteService;

    @PostMapping("/utenti/{id}/partecipa/{evento}")
    public String partecipaEvento(@PathVariable Integer id, @PathVariable Integer evento) {
        return utenteService.partecipaEvento(evento, id);
    }
}
