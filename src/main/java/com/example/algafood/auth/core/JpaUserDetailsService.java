package com.example.algafood.auth.core;
/*
 *  @criado em: 15/09/2020 - {15:14}
 *  @projeto  : algafood-api
 *  @autor    : roberto
 */

import com.example.algafood.auth.dominio.Usuario;
import com.example.algafood.auth.dominio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)//Para corrigir a inicialização
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email informado !"));

        return new AuthUser(usuario, getAuthorities(usuario));
    }

    private Collection<GrantedAuthority> getAuthorities(Usuario usuario){
        return usuario.getGrupos().stream()
                .flatMap(grupo -> grupo.getPermissoes().stream())
                .map(permissao -> new SimpleGrantedAuthority(permissao.getNome().toUpperCase()))
                .collect(Collectors.toSet());
    }
}
