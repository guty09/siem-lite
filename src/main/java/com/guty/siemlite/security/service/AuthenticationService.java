package com.guty.siemlite.security.service;

import com.guty.siemlite.audit.model.AuditAction;
import com.guty.siemlite.audit.service.AuditLogService;
import com.guty.siemlite.security.dto.LoginRequest;
import com.guty.siemlite.security.dto.LoginResponse;
import com.guty.siemlite.security.dto.RegisterRequest;
import com.guty.siemlite.security.dto.UserResponse;
import com.guty.siemlite.security.jwt.JwtService;
import com.guty.siemlite.security.model.Role;
import com.guty.siemlite.security.model.User;
import com.guty.siemlite.security.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final AuditLogService auditLogService;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CustomUserDetailsService userDetailsService,
            AuditLogService auditLogService) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.auditLogService = auditLogService;
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        auditLogService.log(
                AuditAction.USER_LOGIN_SUCCESS,
                user.getUsername(),
                "User logged in successfully"
        );

        return new LoginResponse(
                token,
                user.getUsername(),
                user.getRole().name()
        );
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Role role = Role.valueOf(request.getRole().toUpperCase());

        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                role
        );

        User savedUser = userRepository.save(user);

        auditLogService.log(
                AuditAction.USER_REGISTERED,
                savedUser.getUsername(),
                "User registered with role: " + savedUser.getRole().name()
        );

        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole().name()
        );
    }

    public UserResponse getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );
    }
}