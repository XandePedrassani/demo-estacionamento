package com.pedrassani.demo_estacionamento_api.service;

import com.pedrassani.demo_estacionamento_api.entity.Usuario;
import com.pedrassani.demo_estacionamento_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvar(Usuario usuario){
        return usuarioRepository.save(usuario);

    }
    @Transactional
    public Usuario buscarPorId(Long idUser){
        return usuarioRepository.findById(idUser).orElseThrow(
                () -> new RuntimeException("Usuario não encontrado.")
        );
    }
    @Transactional
    public Usuario editarSenha(Long idUser, String password){
        //Não precisa do update pois o hibernate está controlando
        Usuario user = buscarPorId(idUser);
        user.setPassword(password);
        return user;
    }

    @Transactional
    public List<Usuario> buscarTodos(){
        return usuarioRepository.findAll();
    }
}
