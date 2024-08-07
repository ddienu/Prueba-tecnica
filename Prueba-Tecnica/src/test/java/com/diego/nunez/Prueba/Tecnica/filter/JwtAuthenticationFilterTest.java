package com.diego.nunez.Prueba.Tecnica.filter;

import com.diego.nunez.Prueba.Tecnica.service.Impl.JwtServiceImpl;
import com.diego.nunez.Prueba.Tecnica.service.auth.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTest {

    @Mock
    private JwtServiceImpl jwtService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private HandlerExceptionResolver exceptionResolver;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter authenticationFilter;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doFilterInternalTokenNull() throws ServletException, IOException {
        StringWriter responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);

        when(jwtService.getTokenFromRequest(request)).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);

        authenticationFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        Assertions.assertTrue(responseWriter.toString().contains("{ \"data\": { \"message\": \"Unauthorized Access\" } }"));
    }

    @Test
    void doFilterInternalAuthPath() throws ServletException, IOException {
        String path = "/api/auth/login";

        when(request.getServletPath()).thenReturn(path);

        authenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);

    }

    @Test
    void doFilterInternatTokenValid() throws ServletException, IOException {
        String token = "validToken";
        String username = "test@test.com";
        String path = "/api/protected";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getServletPath()).thenReturn(path);
        when(jwtService.getTokenFromRequest(request)).thenReturn(token);
        when(jwtService.getEmailFromToken(token)).thenReturn(username);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

       authenticationFilter.doFilterInternal(request, response, filterChain);

       Assertions.assertNotNull(userDetails);
       verify(jwtService, times(1)).getEmailFromToken(token);
       verify(jwtService, times(1)).getTokenFromRequest(request);
    }

    @Test
    void doFilterInternalThrowsJwtExpiredTokenException(){
        String token = "validToken";
        String username = "test@test.com";
        String path = "/api/protected";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getServletPath()).thenReturn(path);
        when(jwtService.getTokenFromRequest(ArgumentMatchers.any(HttpServletRequest.class))).thenReturn(token);
        when(jwtService.getEmailFromToken(ArgumentMatchers.anyString())).thenThrow(ExpiredJwtException.class);
        when(jwtService.isTokenValid(ArgumentMatchers.anyString(), ArgumentMatchers.any(UserDetails.class))).thenReturn(false);
        doThrow(new ExpiredJwtException(null, null, "Token expired")).when(exceptionResolver).resolveException(
                ArgumentMatchers.any(HttpServletRequest.class),
                ArgumentMatchers.any(HttpServletResponse.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.any(ExpiredJwtException.class)
        );

        Assertions.assertThrows(ExpiredJwtException.class, () -> {
           authenticationFilter.doFilterInternal(request, response, filterChain);
        });

    }
}
