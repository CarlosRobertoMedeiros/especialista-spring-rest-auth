package com.example.algafood.auth.dominio;
/*
 *  @criado em: 27/06/2020 - {10:23}
 *  @projeto  : algafood-api
 *  @autor    : roberto
 */

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
@Table(name = "tb_grupo", schema = "app")
public class Grupo {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToMany
    @JoinTable(name = "tb_grupo_permissao",
               joinColumns = @JoinColumn(name = "grupo_id"),
               inverseJoinColumns = @JoinColumn(name = "permissao_id")
    )
    private Set<Permissao> permissoes = new HashSet<>();

}
