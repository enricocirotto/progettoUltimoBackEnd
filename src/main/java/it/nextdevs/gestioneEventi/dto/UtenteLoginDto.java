package it.nextdevs.gestioneEventi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UtenteLoginDto {
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;

    public UtenteLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
