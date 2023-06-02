package com.provectus.kafka.ui.config.auth;

import java.net.InetSocketAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginReactiveAuthenticationManager;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

@Configuration
public class OAuthClientSecurityConfig extends AbstractAuthSecurityConfig {

  @Bean
  public ReactiveAuthenticationManager loginAuthenticationManager(ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User>  customOauth2UserService) {

    WebClientReactiveAuthorizationCodeTokenResponseClient accessTokenResponseClient =
        new WebClientReactiveAuthorizationCodeTokenResponseClient();

    var httpClient = HttpClient
        .create()
        .proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP).address(new InetSocketAddress("172.26.0.222", 3128)));

    accessTokenResponseClient.setWebClient(WebClient.builder()
        .clientConnector(
            new ReactorClientHttpConnector(httpClient)
        )
        .build());

    return new OAuth2LoginReactiveAuthenticationManager(accessTokenResponseClient,customOauth2UserService);
  }

}

