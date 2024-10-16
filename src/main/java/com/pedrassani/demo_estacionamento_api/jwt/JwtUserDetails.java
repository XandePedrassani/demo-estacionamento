package com.pedrassani.demo_estacionamento_api.jwt;

import com.pedrassani.demo_estacionamento_api.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class JwtUserDetails extends User {
    private Usuario usuario;
    public JwtUserDetails(Usuario usuario) {
        super(usuario.getUserName(), usuario.getPassword(), AuthorityUtils.createAuthorityList(usuario.getRole().name()));
        this.usuario = usuario;
    }

    public Long getId(){
        return  usuario.getIdUsuario();
    }

    public String getRole(){
        return usuario.getRole().name();
    }
}
