package uz.sardor.simplebankingsystem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uz.sardor.simplebankingsystem.entity.Role;
import uz.sardor.simplebankingsystem.entity.User;
import uz.sardor.simplebankingsystem.enums.UserRole;
import uz.sardor.simplebankingsystem.enums.UserStatus;
import uz.sardor.simplebankingsystem.repository.RoleRepository;
import uz.sardor.simplebankingsystem.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private static final String DEFAULT_PASSWORD = "0304";

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeUsers();
    }

    private void initializeRoles() {
        roleRepository.findByRole(UserRole.USER).orElseGet(() -> {
            return roleRepository.save(
                    Role.builder()
                            .role(UserRole.USER)
                            .build()
            );
        });

        roleRepository.findByRole(UserRole.ADMIN).orElseGet(() -> {
            return roleRepository.save(
                    Role.builder()
                            .role(UserRole.ADMIN)
                            .build()
            );
        });
    }

    private void initializeUsers() {
        Role userRole = roleRepository.findByRole(UserRole.USER)
                .orElseThrow(() -> new IllegalStateException("USER role not found"));
        Role adminRole = roleRepository.findByRole(UserRole.ADMIN)
                .orElseThrow(() -> new IllegalStateException("ADMIN role not found"));

        userRepository.findByEmail("sardor@gmail.com").ifPresentOrElse(
                user -> System.out.println("User sardor@gmail.com already exists."),
                () -> {
                    User user = User.builder()
                            .name("sardor")
                            .email("sardor@gmail.com")
                            .password(passwordEncoder.encode(DEFAULT_PASSWORD))
                            .role(userRole)
                            .status(UserStatus.ACTIVE)
                            .build();
                    userRepository.save(user);
                }
        );

        userRepository.findByEmail("norboyev@gmail.com").ifPresentOrElse(
                user -> System.out.println("User norboyev@gmail.com already exists."),
                () -> {
                    User user = User.builder()
                            .name("norboyev")
                            .email("norboyev@gmail.com")
                            .password(passwordEncoder.encode(DEFAULT_PASSWORD))
                            .role(adminRole)
                            .status(UserStatus.ACTIVE)
                            .build();
                    userRepository.save(user);
                }
        );
    }
}
