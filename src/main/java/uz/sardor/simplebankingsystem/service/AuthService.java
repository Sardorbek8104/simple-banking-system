package uz.sardor.simplebankingsystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.sardor.simplebankingsystem.dto.auth.request.LoginRequest;
import uz.sardor.simplebankingsystem.dto.auth.request.RefreshTokenRequest;
import uz.sardor.simplebankingsystem.dto.auth.request.RegisterRequest;
import uz.sardor.simplebankingsystem.dto.auth.response.LoginResponse;
import uz.sardor.simplebankingsystem.dto.auth.response.RegisterResponse;
import uz.sardor.simplebankingsystem.entity.User;
import uz.sardor.simplebankingsystem.enums.UserRole;
import uz.sardor.simplebankingsystem.enums.UserStatus;
import uz.sardor.simplebankingsystem.exception.exception.ResourceNotFoundException;
import uz.sardor.simplebankingsystem.repository.RoleRepository;
import uz.sardor.simplebankingsystem.repository.UserRepository;
import uz.sardor.simplebankingsystem.service.security.JwtService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest loginRequest) throws JsonProcessingException {

        Optional<User> byEmail = userRepository.findByEmail(loginRequest.getEmail());
        if (byEmail.isEmpty()) {
            throw new ResourceNotFoundException("username or password incorrect");
        }
        User user = byEmail.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("username or password incorrect");
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        }
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        System.out.println(accessToken + " " + refreshToken);
        LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken);

        return loginResponse;
    }

    public RegisterResponse register(RegisterRequest registerRequest) {
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(roleRepository.findByRole(UserRole.USER).get())
                .status(UserStatus.INACTIVE)
                .build();
        userRepository.save(user);
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(user.getId());
        registerResponse.setName(user.getName());
        registerResponse.setEmail(registerRequest.getEmail());
        return registerResponse;
    }

    public LoginResponse getRefreshToken(RefreshTokenRequest request) throws JsonProcessingException {
        if (request.getRefreshToken() == null || request.getRefreshToken().isEmpty()) {
            throw new ResourceNotFoundException("refresh token invalid");
        }
        String refreshToken = request.getRefreshToken();
        jwtService.validateRefreshToken(refreshToken);
        Claims claims = jwtService.refreshTokenClaims(refreshToken);
        String email = claims.getSubject();
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            throw new ResourceNotFoundException("user not found");
        }
        User user = byEmail.get();
        String accessToken = jwtService.generateAccessToken(user);
        return new LoginResponse(accessToken);
    }
}
