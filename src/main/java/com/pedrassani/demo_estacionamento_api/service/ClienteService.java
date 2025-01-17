package com.pedrassani.demo_estacionamento_api.service;

import com.pedrassani.demo_estacionamento_api.entity.Cliente;
import com.pedrassani.demo_estacionamento_api.entity.Usuario;
import com.pedrassani.demo_estacionamento_api.exception.CpfUniqueViolationException;
import com.pedrassani.demo_estacionamento_api.exception.EntityNotFoundException;
import com.pedrassani.demo_estacionamento_api.exception.UsernameUniqueViolationException;
import com.pedrassani.demo_estacionamento_api.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ClienteService {
    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente salvar(Cliente cLiente){
        try {
            return clienteRepository.save(cLiente);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            // Capturando o erro específico de unicidade no banco de dados
            throw new CpfUniqueViolationException(
                    String.format("O CPF '%s' já está cadastrado.", cLiente.getCpf())
            );
        }
    }

    @Transactional
    public Cliente buscarPorId(Long idCliente){
        return clienteRepository.findById(idCliente).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cliente id='%s' nao encontrado.", idCliente))
        );
    }

    @Transactional
    public List<Cliente> buscarTodos(){
        return clienteRepository.findAll();
    }

    @Transactional
    public Cliente buscarPorUsuarioId(Long id) {
        return clienteRepository.findByUsuarioIdUsuario(id);
    }
}
