package com.agrogest.user.service;

import com.agrogest.user.dto.*;
import com.agrogest.user.model.User;
import com.agrogest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserResponse createUser(CreateUserRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }

        User user = User.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .passwordHash(request.getPassword())
                .telefono(request.getTelefono())
                .rol(request.getRol())
                .activo(true)
                .build();

        User saved = repository.save(user);

        // eventProducer.publishUserCreated(saved.getId(), saved.getEmail(), saved.getNombre()); // Kafka deshabilitado temporalmente

        return toResponse(saved);
    }

    @Override
    public UserResponse getUser(UUID id) {
        User user = findOrThrow(id);
        return toResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {

        User user = findOrThrow(id);

        if (!user.getEmail().equals(request.getEmail())
                && repository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está en uso por otro usuario");
        }

        user.setNombre(request.getNombre());
        user.setApellido(request.getApellido());
        user.setEmail(request.getEmail());
        user.setTelefono(request.getTelefono());
        user.setFotoPerfil(request.getFotoPerfil());
        user.setRol(request.getRol());

        return toResponse(repository.save(user));
    }

    @Override
    public UserResponse deactivateUser(UUID id) {

        User user = findOrThrow(id);

        if (!user.getActivo()) {
            throw new RuntimeException("El usuario ya está inactivo");
        }

        user.setActivo(false);
        return toResponse(repository.save(user));
    }

    @Override
    public UserResponse restoreUser(UUID id) {

        User user = findOrThrow(id);

        if (user.getActivo()) {
            throw new RuntimeException("El usuario ya está activo");
        }

        user.setActivo(true);
        return toResponse(repository.save(user));
    }

    private User findOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    private UserResponse toResponse(User u) {
        UserResponse res = new UserResponse();
        res.setId(u.getId());
        res.setNombre(u.getNombre());
        res.setApellido(u.getApellido());
        res.setEmail(u.getEmail());
        res.setTelefono(u.getTelefono());
        res.setRol(u.getRol());
        res.setFotoPerfil(u.getFotoPerfil());
        res.setActivo(u.getActivo());
        res.setCreatedAt(u.getCreatedAt());
        return res;
    }
}
