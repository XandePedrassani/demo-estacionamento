package com.pedrassani.demo_estacionamento_api.service;

import com.pedrassani.demo_estacionamento_api.entity.Usuario;
import com.pedrassani.demo_estacionamento_api.exception.UsernameUniqueViolationException;
import com.pedrassani.demo_estacionamento_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvar(Usuario usuario){
        try {
            Usuario savedUsuario = usuarioRepository.save(usuario);
            usuarioRepository.flush();  // Sem isso ele nunca caia no meu catch
            return savedUsuario;
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            // Capturando o erro específico de unicidade no banco de dados
            throw new UsernameUniqueViolationException(
                    String.format("O username '%s' já está cadastrado.", usuario.getUserName())
            );
        }


    }
    @Transactional
    public Usuario buscarPorId(Long idUser){
        return usuarioRepository.findById(idUser).orElseThrow(
                () -> new RuntimeException("Usuario não encontrado.")
        );
    }
    @Transactional
    public Usuario editarSenha(Long idUser, String senhaAtual, String novaSenha, String confirmaSenha){
        //Não precisa do update pois o hibernate está controlando
        Usuario user = buscarPorId(idUser);
        if(user.getPassword().equals(senhaAtual)){
            if(novaSenha.equals(confirmaSenha)){
                user.setPassword(novaSenha);
            }else{
                throw new RuntimeException("Nova senha não confere com a confirmação senha");
            }
        }else{
            throw new RuntimeException("Senha atual incorreta");
        }

        return user;
    }

    @Transactional
    public List<Usuario> buscarTodos(){
        return usuarioRepository.findAll();
    }
}
