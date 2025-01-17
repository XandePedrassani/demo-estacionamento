package com.pedrassani.demo_estacionamento_api.web.controller;

import com.pedrassani.demo_estacionamento_api.entity.Cliente;
import com.pedrassani.demo_estacionamento_api.entity.Usuario;
import com.pedrassani.demo_estacionamento_api.jwt.JwtUserDetails;
import com.pedrassani.demo_estacionamento_api.service.ClienteService;
import com.pedrassani.demo_estacionamento_api.service.UsuarioService;
import com.pedrassani.demo_estacionamento_api.web.dto.ClienteCreateDto;
import com.pedrassani.demo_estacionamento_api.web.dto.ClienteResponseDto;
import com.pedrassani.demo_estacionamento_api.web.dto.UsuarioCreateDto;
import com.pedrassani.demo_estacionamento_api.web.dto.UsuarioResponseDto;
import com.pedrassani.demo_estacionamento_api.web.dto.mapper.ClienteMapper;
import com.pedrassani.demo_estacionamento_api.web.dto.mapper.UsuarioMapper;
import com.pedrassani.demo_estacionamento_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/clientes")
public class ClienteController {
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @Operation(
            summary = "Criar um novo cliente", description = "Recurso para criar um novo cliente",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "409", description = "Cliente já cadastrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(responseCode = "422", description = "Dados invalidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> create(@Valid @RequestBody ClienteCreateDto clienteCreateDto,
                                                     @AuthenticationPrincipal JwtUserDetails userDetails){

        Cliente cliente = ClienteMapper.toCliente(clienteCreateDto);
        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
        clienteService.salvar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toDto(cliente));
    }


    @Operation(
            summary = "Recuperar um cliente pelo id", description = "Recuperar um cliente pelo id",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente recuperado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Cliente sem moral",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDto> getCliente(@PathVariable Long id){
        Cliente cliente = clienteService.buscarPorId(id);
        return  ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }

    @Operation(
            summary = "Recuperar todos os clientes", description = "Recuperar todos os clientes",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente recuperado com sucesso",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDto.class)))
                    ),
                    @ApiResponse(responseCode = "403", description = "Cliente sem moral",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClienteResponseDto>> getAll(){
        List<Cliente> clientes = clienteService.buscarTodos();
        return  ResponseEntity.ok(ClienteMapper.toDto(clientes));
    }


    @GetMapping("/detalhes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> getDetalhes(@AuthenticationPrincipal JwtUserDetails userDetails){
        Cliente cliente = clienteService.buscarPorUsuarioId(userDetails.getId());

        return  ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }
}
