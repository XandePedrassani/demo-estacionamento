package com.pedrassani.demo_estacionamento_api.web.controller;

import com.pedrassani.demo_estacionamento_api.entity.Usuario;
import com.pedrassani.demo_estacionamento_api.service.UsuarioService;
import com.pedrassani.demo_estacionamento_api.web.dto.UsuarioCreateDto;
import com.pedrassani.demo_estacionamento_api.web.dto.UsuarioResponseDto;
import com.pedrassani.demo_estacionamento_api.web.dto.UsuarioSenhaDto;
import com.pedrassani.demo_estacionamento_api.web.dto.mapper.UsuarioMapper;
import com.pedrassani.demo_estacionamento_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios", description = "Contem todas as operações referentes ao usuario, como cadastro, consulta e edição")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Operation(
            summary = "Criar um novo usuario", description = "Recurso para criar um novo usuario",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuario criado com sucesso",
                                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "409", description = "Usuario já cadastrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(responseCode = "422", description = "Dados invalidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto usuarioCreateDto){//@Valid faz o jakarta fazer as validações criadas no DTO

        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(usuarioCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @Operation(
            summary = "Recuperar um usuario pelo id", description = "Recuperar um usuario pelo id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario recuperado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuario não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getUser(@PathVariable Long id){
        Usuario user = usuarioService.buscarPorId(id);
        return  ResponseEntity.ok(UsuarioMapper.toDto(user));
    }

    @Operation(
            summary = "Alterar senha", description = "Recurso para alterar senha",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuario não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Senha invalida",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @PatchMapping("/{id}")//PatchMapping é para alterar parciamente objeto(boa pratica)
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UsuarioSenhaDto usuarioSenhaDto){
        Usuario user = usuarioService.editarSenha(id, usuarioSenhaDto.getSenhaAtual(), usuarioSenhaDto.getNovaSenha(), usuarioSenhaDto.getConfirmaSenha());
        return  ResponseEntity.noContent().build();//Não retorna nada, porem com o status 204 de sucesso
    }


    @Operation(
            summary = "Recuperar todos os usuarios", description = "Recuperar todos os usuarios",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario recuperado com sucesso",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDto.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> getAll(){
        List<Usuario> users = usuarioService.buscarTodos();
        return  ResponseEntity.ok(UsuarioMapper.toDto(users));
    }
}
