package com.pedrassani.demo_estacionamento_api.repository;

import com.pedrassani.demo_estacionamento_api.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByUsuarioIdUsuario(Long id);
}
