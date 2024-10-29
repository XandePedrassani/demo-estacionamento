package com.pedrassani.demo_estacionamento_api.repository;

import com.pedrassani.demo_estacionamento_api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUserName(String username);

    @Query("select u.role from Usuario u where u.userName like :username")
    Usuario.Role findRoleByUserName(String username);
}