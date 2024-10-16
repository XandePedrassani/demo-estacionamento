package com.pedrassani.demo_estacionamento_api.jwt;

import com.pedrassani.demo_estacionamento_api.entity.Usuario;
import com.pedrassani.demo_estacionamento_api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    final UsuarioService usuarioService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorUsername(username);
        return new JwtUserDetails(usuario);
    }

    public JwtToken getTokenAuthentication(String userName){
        Usuario.Role role = usuarioService.buscarRolePorUsername(userName);
        return JwtUtils.createToken(userName, role.name().substring("ROLE_".length()));
    }
}
