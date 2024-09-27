package com.pedrassani.demo_estacionamento_api.web.dto.mapper;

import com.pedrassani.demo_estacionamento_api.entity.Usuario;
import com.pedrassani.demo_estacionamento_api.web.dto.UsuarioCreateDto;
import com.pedrassani.demo_estacionamento_api.web.dto.UsuarioResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {
    public static Usuario toUsuario(UsuarioCreateDto usuarioCreateDto){
        return new ModelMapper().map(usuarioCreateDto, Usuario.class);//Converte o usuarioDto para usuario

    }

    public static UsuarioResponseDto toDto(Usuario usuario){
        String role = usuario.getRole().name().substring("ROLE_".length());
        PropertyMap<Usuario, UsuarioResponseDto> props = new PropertyMap<Usuario, UsuarioResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);//Seto o role que eu criei acima, dessa forma o mapper não trocara o valor
        return mapper.map(usuario, UsuarioResponseDto.class);
    }
    public static List<UsuarioResponseDto> toDto(List<Usuario> usuarios){
        return usuarios.stream().map(UsuarioMapper::toDto).collect(Collectors.toList());
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
