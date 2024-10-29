package com.pedrassani.demo_estacionamento_api.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService detailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader(JwtUtils.JWT_AUTHORIZATION);
        if(token == null || !token.startsWith(JwtUtils.JWT_BEARER)){
            log.info("JWT token está nullo ou vazio, ou não é um Bearer");
            filterChain.doFilter(request, response);
            return;
        }
        if(!JwtUtils.isTokenValid(token)){
            log.warn("JWT token está está invalido ou expirado");
            filterChain.doFilter(request, response);
            return;
        }

        String username = JwtUtils.getUserNameFromToken(token);
        toAuthentication(request, username);

        filterChain.doFilter(request, response);
    }

    private void toAuthentication(HttpServletRequest request, String userName) {
        UserDetails userDetails = detailsService.loadUserByUsername(userName);

        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.
                authenticated(userDetails, null, userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
