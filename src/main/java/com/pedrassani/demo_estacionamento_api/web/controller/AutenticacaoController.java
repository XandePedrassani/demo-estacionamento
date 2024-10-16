package com.pedrassani.demo_estacionamento_api.web.controller;


import com.pedrassani.demo_estacionamento_api.jwt.JwtToken;
import com.pedrassani.demo_estacionamento_api.jwt.JwtUserDetailsService;
import com.pedrassani.demo_estacionamento_api.web.dto.UsuarioLoginDto;
import com.pedrassani.demo_estacionamento_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "Login", description = "Contem todas as operações referentes ao login de usuario")
@RestController
@RequestMapping("api/v1")
public class AutenticacaoController {
    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> autenticar(@RequestBody @Valid UsuarioLoginDto usuarioLoginDto, HttpServletRequest request){
        log.info("Processo de autenticação pelo login {}", usuarioLoginDto.getUsername());

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuarioLoginDto.getUsername(), usuarioLoginDto.getPassword());
            authenticationManager.authenticate(authenticationToken);
            JwtToken token = detailsService.getTokenAuthentication(usuarioLoginDto.getUsername());
            return ResponseEntity.ok(token);
        }catch (AuthenticationException exception){
            log.warn("Bad Credentials from username '{}'", usuarioLoginDto.getUsername());
        }

        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credenciais Invalidas"));
    }
}
