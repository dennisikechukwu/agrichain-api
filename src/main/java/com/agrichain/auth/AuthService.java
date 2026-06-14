package com.agrichain.auth;

public interface AuthService {

    AuthResponse registerFarmer(FarmerRegisterRequest request);

    AuthResponse registerOffTaker(OffTakerRegisterRequest request);

    AuthResponse registerAdmin(AdminRegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void logout(RefreshTokenRequest request);
}
