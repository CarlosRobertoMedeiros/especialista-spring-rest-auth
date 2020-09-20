package com.example.algafood.auth.core;
/*
 *  @criado em: 08/09/2020 - {09:47}
 *  @projeto  : algafood-api
 *  @autor    : roberto
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtKeyStoreProperties jwtKeyStoreProperties;

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //Configura os Clientes no Authorization Server
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //security.checkTokenAccess("isAuthenticated()");
        security.checkTokenAccess("permitAll()")
                .tokenKeyAccess("permitAll()")
                .allowFormAuthenticationForClients();//não passar como http basic e sim informar no corpo da requisição
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                Arrays.asList(new JwtCustomClaimsTokenEnhancer(),jwtAccessTokenConverter()));

        endpoints
            .authenticationManager(authenticationManager)
            .userDetailsService(userDetailsService)
            .reuseRefreshTokens(false)
            .accessTokenConverter(jwtAccessTokenConverter())
            .tokenEnhancer(tokenEnhancerChain)
            .approvalStore(approvalStore(endpoints.getTokenStore())) //depois do accessTokenConverter Obrigado
            .tokenGranter(tokenGranter(endpoints));//Linha com Pkce e Authorization Code
    }

    private ApprovalStore approvalStore(TokenStore tokenStore){
        TokenApprovalStore approvalStore = new TokenApprovalStore();
        approvalStore.setTokenStore(tokenStore);
        return approvalStore;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // Usado o Código Abaixo PAra Chave Simétrica
        //        jwtAccessTokenConverter.setSigningKey("ashnfjashdfu8afhusafniw4we4e5hj4");//HMAC-Sha256

        //Usado o Código Abaixo para Chave Assimétrica
        ClassPathResource jksResource = new ClassPathResource(jwtKeyStoreProperties.getPath());
        String keyStorePass = jwtKeyStoreProperties.getPassword();
        String keyPairAlias= jwtKeyStoreProperties.getKeypairAlias();

        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(jksResource,keyStorePass.toCharArray());
        KeyPair keyPair =  keyStoreKeyFactory.getKeyPair(keyPairAlias);

        jwtAccessTokenConverter.setKeyPair(keyPair);
        return jwtAccessTokenConverter;
    }

    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        PkceAuthorizationCodeTokenGranter pkceAuthorizationCodeTokenGranter = new PkceAuthorizationCodeTokenGranter(endpoints.getTokenServices(),
                endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory());

        List<TokenGranter> granters = Arrays.asList(
                pkceAuthorizationCodeTokenGranter, endpoints.getTokenGranter());

        return new CompositeTokenGranter(granters);
    }
}
