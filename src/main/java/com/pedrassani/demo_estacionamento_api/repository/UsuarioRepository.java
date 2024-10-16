package com.pedrassani.demo_estacionamento_api.repository;

import com.pedrassani.demo_estacionamento_api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    @Query("SELECT U.ROLE FROM USUARIOS AS U WHERE U.USER_NAME LIKE :username")
    Usuario.Role findRoleByUsername(String username);
}