package com.example.algafood.auth.dominio;
/*
 *  @criado em: 19/06/2020 - {13:12}
 *  @projeto  : algafood-api
 *  @autor    : roberto
 */

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_permissao", schema = "app")
public class Permissao {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

}
