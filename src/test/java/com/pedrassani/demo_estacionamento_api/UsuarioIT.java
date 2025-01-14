package com.pedrassani.demo_estacionamento_api;

import com.pedrassani.demo_estacionamento_api.web.dto.UsuarioCreateDto;
import com.pedrassani.demo_estacionamento_api.web.dto.UsuarioResponseDto;
import com.pedrassani.demo_estacionamento_api.web.dto.UsuarioSenhaDto;
import com.pedrassani.demo_estacionamento_api.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class UsuarioIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createUsuario_ComUserNameEPasswordValidos_RetornarUsuarioCriadoStatus201(){
        UsuarioResponseDto usuarioCreateDto = testClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("teste_Alexandre@gmail.com", "123456"))
                .exchange()//resposta
                .expectStatus().isCreated()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(usuarioCreateDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioCreateDto.getIdUsuario()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioCreateDto.getUserName()).isEqualTo("");
        org.assertj.core.api.Assertions.assertThat(usuarioCreateDto.getRole()).isEqualTo("CLIENTE");
    }

    @Test
    public void createUsuario_ComUserNameinvalido_RetornarUsuarioCriadoStatus422(){
        ErrorMessage errorMessage = testClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("", "123456"))
                .exchange()//resposta
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(errorMessage.getStatus()).isNotNull();

        errorMessage = testClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("teste_@", "123456"))
                .exchange()//resposta
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(errorMessage.getStatus()).isNotNull();

        errorMessage = testClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto(".com", "123456"))
                .exchange()//resposta
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(errorMessage.getStatus()).isNotNull();
    }

    @Test
    public void createUsuario_ComPasswordinvalid_RetornarUsuarioCriadoStatus422(){
        ErrorMessage errorMessage = testClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("teste_Alexandre@gmail.com", ""))
                .exchange()//resposta
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(errorMessage.getStatus()).isNotNull();

        errorMessage = testClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("teste_Alexandre@gmail.com", "1234"))
                .exchange()//resposta
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(errorMessage.getStatus()).isNotNull();

    }

    @Test
    public void createUsuario_ComUserNameRepetido(){
        ErrorMessage errorMessage = testClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("alexandre@teste.com", "123456"))
                .exchange()//resposta
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(errorMessage.getStatus()).isNotNull();
    }

    @Test
    public void buscarUsuario_comIdExistente_RetornoUsuario200(){
        UsuarioResponseDto usuarioResponseDto = testClient.get()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "alexandre@teste.com", "1234567"))
                .exchange()//resposta
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getUserName()).isEqualTo("alexandre@teste.com");
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getIdUsuario()).isEqualTo(100L);

        usuarioResponseDto = testClient.get()
                .uri("/api/v1/usuarios/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "alexandre@teste.com", "1234567"))
                .exchange()//resposta
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getUserName()).isEqualTo("teste2@teste.com");
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getIdUsuario()).isEqualTo(101L);

        usuarioResponseDto = testClient.get()
                .uri("/api/v1/usuarios/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "teste2@teste.com", "1234567"))
                .exchange()//resposta
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getUserName()).isEqualTo("teste2@teste.com");
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getIdUsuario()).isEqualTo(101L);
    }

    @Test
    public void buscarUsuario_comIdInexistenteExistente_RetornoUsuario404(){
        ErrorMessage errorMessage  = testClient.get()
                .uri("/api/v1/usuarios/0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "alexandre@teste.com", "1234567"))
                .exchange()//resposta
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(errorMessage.getStatus()).isNotNull();
    }

    @Test
    public void buscarUsuario_comIdOutroUsario_RetornoUsuario403(){
        ErrorMessage responseBody  = testClient.get()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "teste2@teste.com", "1234567"))
                .exchange()//resposta
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }
    @Test
    public void updatePassword_ComSenhasInvalidas_RetornarStatus400() {
        // Testando senhas inválidas (senha atual vazia)
        ErrorMessage errorMessage = testClient.patch()
                .uri("/api/v1/usuarios/100") // ID do usuário
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("", "novaSenha123", "novaSenha123")) // Senha atual vazia
                .exchange()
                .expectStatus().isEqualTo(422) // Espera-se um status 400 para senha inválida
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(errorMessage.getStatus()).isNotNull();

        // Testando senhas inválidas (nova senha muito curta)
        errorMessage = testClient.patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "123", "123")) // Nova senha muito curta
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(errorMessage.getStatus()).isNotNull();

        // Testando senhas inválidas (confirmação de senha não coincide)
        errorMessage = testClient.patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "novaSenha123", "outraSenha123")) // Senhas não coincidem
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(errorMessage.getStatus()).isNotNull();
    }

}
