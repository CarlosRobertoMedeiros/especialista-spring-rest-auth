package com.example.algafood.auth.dominio;
/*
 *  @criado em: 19/07/2020 - {10:21}
 *  @projeto  : algafood-api
 *  @autor    : roberto
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Optional<Usuario> findByEmail(String email);
}
