package com.agrichain.user;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserProfileResponse getMyProfile();
}
