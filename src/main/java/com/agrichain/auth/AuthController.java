package com.agrichain.auth;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register new accounts and manage login sessions")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/farmer")
    @Operation(summary = "Register a farmer", description = "Creates a new farmer account and returns an access token and refresh token.", security = {})
    public ResponseEntity<AuthResponse> registerFarmer(@Valid @RequestBody FarmerRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerFarmer(request));
    }

    @PostMapping("/register/offtaker")
    @Operation(summary = "Register an off-taker", description = "Creates a new off-taker (buyer) account and returns an access token and refresh token.", security = {})
    public ResponseEntity<AuthResponse> registerOffTaker(@Valid @RequestBody OffTakerRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerOffTaker(request));
    }

    @PostMapping("/register/admin")
    @Operation(summary = "Register an admin", description = "Creates a new admin account and returns an access token and refresh token.", security = {})
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody AdminRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerAdmin(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates a user with email and password. Returns an access token (1 hour) and a refresh token (7 days).", security = {})
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh-token")
    @Hidden
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Revokes the provided refresh token, ending the user's session.")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }
}
