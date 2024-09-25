package com.pedrassani.demo_estacionamento_api.service;

import com.pedrassani.demo_estacionamento_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
}
