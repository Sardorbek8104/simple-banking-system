package uz.sardor.simplebankingsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.sardor.simplebankingsystem.dto.user.request.UserCreateRequest;
import uz.sardor.simplebankingsystem.dto.user.request.UserUpdateRequest;
import uz.sardor.simplebankingsystem.dto.user.response.UserResponse;
import uz.sardor.simplebankingsystem.entity.Role;
import uz.sardor.simplebankingsystem.entity.User;
import uz.sardor.simplebankingsystem.enums.UserRole;
import uz.sardor.simplebankingsystem.enums.UserStatus;
import uz.sardor.simplebankingsystem.exception.exception.ResourceNotFoundException;
import uz.sardor.simplebankingsystem.repository.RoleRepository;
import uz.sardor.simplebankingsystem.repository.UserRepository;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserResponse create(UserCreateRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(roleRepository.findByRole(UserRole.USER).get());
        user.setStatus(UserStatus.INACTIVE);
        User save = userRepository.save(user);

        return getUserResponse(save);
    }

    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElse(null);
        Role role = roleRepository.findByRole((UserRole.valueOf(request.getRole().toUpperCase(Locale.ROOT)))).get();
        if (role == null) {
            throw new ResourceNotFoundException("this role not found");
        }
        if (user == null) throw new ResourceNotFoundException("User with id " + id + " not found");
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(role);
        user.setStatus(UserStatus.valueOf(request.getStatus().toUpperCase(Locale.ROOT)));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User save = userRepository.save(user);
        return getUserResponse(save);
    }

    public UserResponse getById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) throw new ResourceNotFoundException("User with id " + id + " not found");
        return getUserResponse(user);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    private UserResponse getUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().getAuthority())
                .status(user.getStatus().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .build();
    }


    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(user -> getUserResponse(user)).toList();
    }
}
