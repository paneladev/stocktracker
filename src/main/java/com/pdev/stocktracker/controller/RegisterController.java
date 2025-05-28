package com.pdev.stocktracker.controller;

import com.pdev.stocktracker.controller.request.RegisterUserRequest;
import com.pdev.stocktracker.entity.Role;
import com.pdev.stocktracker.entity.User;
import com.pdev.stocktracker.exception.ResourceAlreadyExistsException;
import com.pdev.stocktracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserRepository repository;

    @PostMapping
    public ResponseEntity<Void> register(@RequestHeader(value = "isAdmin", required = false) boolean isAdmin,
                                         @RequestBody RegisterUserRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email já cadastrado, email: " + request.getEmail());
        }

        User newUser = User.builder()
                .name(request.getNome())
                .email(request.getEmail())
                .roles(isAdmin ? List.of(Role.ADMIN, Role.USER) : List.of(Role.USER))
                .password(new BCryptPasswordEncoder().encode(request.getSenha()))
                .build();

        repository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
