package com.pedrassani.demo_estacionamento_api.repository;

import com.pedrassani.demo_estacionamento_api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}