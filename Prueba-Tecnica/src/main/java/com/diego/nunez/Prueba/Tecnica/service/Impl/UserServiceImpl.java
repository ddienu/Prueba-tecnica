package com.diego.nunez.Prueba.Tecnica.service.Impl;

import com.diego.nunez.Prueba.Tecnica.dto.UpdatePasswordDto;
import com.diego.nunez.Prueba.Tecnica.dto.UserDto;
import com.diego.nunez.Prueba.Tecnica.entity.Users;
import com.diego.nunez.Prueba.Tecnica.repository.IUserRepository;
import com.diego.nunez.Prueba.Tecnica.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtService;

    @Autowired
    public UserServiceImpl (IUserRepository userRepository, PasswordEncoder passwordEncoder,
                            AuthenticationManager authenticationManager, JwtServiceImpl jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
    @Override
    public String login(UserDto user) {
        String message = "Incorrect email or password. Please check your credentials";
        Users userFound = userRepository.getUserByEmail(user.getEmail())
                .orElseThrow(() -> new BadCredentialsException(message));

        String passwordEncrypted = userFound.getPassword();

        boolean passwordMatch = passwordEncoder.matches(user.getPassword(), passwordEncrypted);
        if(!passwordMatch){
            throw new BadCredentialsException(message);
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        return jwtService.getToken(user);
    }

    @Override
    public Users register(Users user) throws SQLIntegrityConstraintViolationException {
        Optional<Users> userEmail = userRepository.getUserByEmail(user.getEmail());
        if( userEmail.isEmpty()){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("USER");
            user.setName(user.getName());
            user.setEmail(user.getEmail());
            return userRepository.save(user);
        }
        throw new SQLIntegrityConstraintViolationException("The username already exist");
    }

    @Override
    public void updatePassword(String email, UpdatePasswordDto passwords) {
        Optional<Users> userFounded = Optional.ofNullable(userRepository.getUserByEmail(email).orElseThrow(
                () -> new BadCredentialsException("User not found")
        ));

        boolean passwordMatcher = passwordEncoder.matches(passwords.getPassword(), userFounded.get().getPassword());

        if( !passwordMatcher){
            throw new BadCredentialsException("The password does not match");
        }

        userFounded.get().setPassword(passwordEncoder.encode(passwords.getNewPassword()));
        userRepository.save(userFounded.get());
    }
}
