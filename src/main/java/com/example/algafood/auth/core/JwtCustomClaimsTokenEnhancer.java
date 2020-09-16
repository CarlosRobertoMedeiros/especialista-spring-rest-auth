package com.example.algafood.auth.core;
/*
 *  @criado em: 15/09/2020 - {19:06}
 *  @projeto  : algafood-api
 *  @autor    : roberto
 */

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;

public class JwtCustomClaimsTokenEnhancer implements TokenEnhancer {


    //Incrementando as Informações de PayLoad
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication oAuth2Authentication) {

        if (oAuth2Authentication.getPrincipal() instanceof AuthUser) {
            AuthUser authUser = (AuthUser) oAuth2Authentication.getPrincipal();

            HashMap<String, Object> info = new HashMap();
            info.put("nome_completo", authUser.getFullName());
            info.put("usuario_id", authUser.getUserId());

            DefaultOAuth2AccessToken oAuth2AcessToken = (DefaultOAuth2AccessToken) accessToken;
            oAuth2AcessToken.setAdditionalInformation(info);
        }

        return accessToken;

    }
}
