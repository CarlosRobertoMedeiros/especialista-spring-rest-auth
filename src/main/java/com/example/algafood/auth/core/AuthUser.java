package com.example.algafood.auth.core;
/*
 *  @criado em: 15/09/2020 - {15:17}
 *  @projeto  : algafood-api
 *  @autor    : roberto
 */

import com.example.algafood.auth.dominio.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class AuthUser extends User {

    private Long userId;
    private String fullName;

    public AuthUser(Usuario usuario, Collection<? extends GrantedAuthority> authorities){
        super(usuario.getEmail(),usuario.getSenha(), authorities);
        this.userId = usuario.getId();
        this.fullName = usuario.getNome();
    }

}
