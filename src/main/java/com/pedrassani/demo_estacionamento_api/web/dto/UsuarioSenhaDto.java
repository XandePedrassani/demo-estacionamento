package com.pedrassani.demo_estacionamento_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSenhaDto {
    @NotBlank
    @Size(min = 6, max = 12)
    private String senhaAtual;
    @NotBlank
    @Size(min = 6, max = 12)
    private String novaSenha;
    @NotBlank
    @Size(min = 6, max = 12)
    private String confirmaSenha;
}
