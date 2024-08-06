package com.diego.nunez.Prueba.Tecnica;

import com.diego.nunez.Prueba.Tecnica.config.SecurityConfigTest;
import com.diego.nunez.Prueba.Tecnica.controller.auth.AuthController;
import com.diego.nunez.Prueba.Tecnica.filter.JwtAuthenticationFilter;
import com.diego.nunez.Prueba.Tecnica.repository.IUserRepository;
import com.diego.nunez.Prueba.Tecnica.service.Impl.JwtServiceImpl;
import com.diego.nunez.Prueba.Tecnica.service.Impl.UserServiceImpl;
import com.mysql.cj.protocol.AuthenticationProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Import(SecurityConfigTest.class)
class PruebaTecnicaApplicationTests {
	@MockBean
	private AuthenticationProvider authenticationProvider;

	@MockBean
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@MockBean
	private UserServiceImpl userServiceImpl;

	@MockBean
	private JwtServiceImpl jwtServiceImpl;

	@Autowired
	private AuthController authController;

	@Test
	void contextLoads() {
		assertNotNull(authController);
	}
}
