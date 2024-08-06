package com.diego.nunez.Prueba.Tecnica.config;

import com.diego.nunez.Prueba.Tecnica.filter.JwtAuthenticationFilter;
import com.diego.nunez.Prueba.Tecnica.repository.IUserRepository;
import com.diego.nunez.Prueba.Tecnica.service.Impl.JwtServiceImpl;
import com.diego.nunez.Prueba.Tecnica.service.Impl.UserServiceImpl;
import com.mysql.cj.protocol.AuthenticationProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.mockito.Mockito.mock;

@TestConfiguration
@Profile("test")
public class SecurityConfigTest {

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return mock(AuthenticationProvider.class);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return mock(JwtAuthenticationFilter.class);
    }

    @Bean
    public UserServiceImpl userServiceImpl(){
        return mock(UserServiceImpl.class);
    }

    @Bean
    public JwtServiceImpl jwtServiceImpl(IUserRepository userRepository){
        return new JwtServiceImpl(userRepository);
    }
    @Bean
    public IUserRepository userRepository() {
        return mock(IUserRepository.class);
    }
}
