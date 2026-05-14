package com.agrogest.user.service;

import com.agrogest.user.dto.*;
import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUser(UUID id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(UUID id, UpdateUserRequest request);

    UserResponse deactivateUser(UUID id);

    UserResponse restoreUser(UUID id);
}
