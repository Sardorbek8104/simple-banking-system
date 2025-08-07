package uz.sardor.simplebankingsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.sardor.simplebankingsystem.dto.auth.request.LoginRequest;
import uz.sardor.simplebankingsystem.dto.auth.request.RefreshTokenRequest;
import uz.sardor.simplebankingsystem.dto.auth.request.RegisterRequest;
import uz.sardor.simplebankingsystem.dto.auth.response.LoginResponse;
import uz.sardor.simplebankingsystem.dto.auth.response.RegisterResponse;
import uz.sardor.simplebankingsystem.service.AuthService;

@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    private ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws JsonProcessingException {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    private ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/getRefreshToken")
    private ResponseEntity<LoginResponse> getRefreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws JsonProcessingException {
        LoginResponse refreshToken = authService.getRefreshToken(refreshTokenRequest);
        return ResponseEntity.ok(refreshToken);
    }

}
