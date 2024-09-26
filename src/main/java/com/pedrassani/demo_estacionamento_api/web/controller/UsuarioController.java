package com.pedrassani.demo_estacionamento_api.web.controller;

import com.pedrassani.demo_estacionamento_api.entity.Usuario;
import com.pedrassani.demo_estacionamento_api.service.UsuarioService;
import com.pedrassani.demo_estacionamento_api.web.dto.UsuarioCreateDto;
import com.pedrassani.demo_estacionamento_api.web.dto.UsuarioResponseDto;
import com.pedrassani.demo_estacionamento_api.web.dto.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@RequestBody UsuarioCreateDto usuarioCreateDto){

        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(usuarioCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getUser(@PathVariable Long id){
        Usuario user = usuarioService.buscarPorId(id);
        return  ResponseEntity.ok(UsuarioMapper.toDto(user));
    }

    @PatchMapping("/{id}")//PatchMapping é para alterar parciamente objeto(boa pratica)
    public ResponseEntity<Usuario> updatePassword(@PathVariable Long id, @RequestBody Usuario usuario){
        Usuario user = usuarioService.editarSenha(id, usuario.getPassword());
        return  ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAll(){
        List<Usuario> users = usuarioService.buscarTodos();
        return  ResponseEntity.ok(users);
    }
}
