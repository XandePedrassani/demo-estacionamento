package com.pedrassani.demo_estacionamento_api.web.dto.mapper;

import com.pedrassani.demo_estacionamento_api.entity.Cliente;
import com.pedrassani.demo_estacionamento_api.entity.Usuario;
import com.pedrassani.demo_estacionamento_api.web.dto.ClienteCreateDto;
import com.pedrassani.demo_estacionamento_api.web.dto.ClienteResponseDto;
import com.pedrassani.demo_estacionamento_api.web.dto.UsuarioResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {
    public static Cliente toCliente(ClienteCreateDto dto){
        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteResponseDto toDto(Cliente cliente){
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }

    public static List<ClienteResponseDto> toDto(List<Cliente> clientes){
        return clientes.stream().map(ClienteMapper::toDto).collect(Collectors.toList());
        /*
            usuarios.stream():
                Converte a lista usuarios em um fluxo (stream).
                Um stream é uma sequência de elementos que suporta operações agregadas sequenciais e paralelas.
            map(UsuarioMapper::toDto):
                Aplica a função toDto da classe UsuarioMapper a cada elemento do stream.
            collect(Collectors.toList()):
                Coleta os elementos do stream em uma lista. O resultado é uma lista de objetos UsuarioDto.
        * */
    }
}
