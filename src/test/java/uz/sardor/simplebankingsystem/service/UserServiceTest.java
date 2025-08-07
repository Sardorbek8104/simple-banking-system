package uz.sardor.simplebankingsystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService functionality tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;
    private Role sampleRole;

    @BeforeEach
    void setUp() {
        sampleRole = new Role();
        sampleRole.setRole(UserRole.USER);

        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setName("Sardor");
        sampleUser.setEmail("sardor@example.com");
        sampleUser.setPassword("encodedPassword");
        sampleUser.setRole(sampleRole);
        sampleUser.setStatus(UserStatus.INACTIVE);
    }



    @Test
    @DisplayName("GIVEN a valid UserCreateRequest WHEN create is called THEN a UserResponse is returned with inactive status")
    void shouldCreateUser_whenRequestIsValid_andReturnInactiveStatus() {
        // GIVEN
        UserCreateRequest createRequest = UserCreateRequest.builder()
                .name("Sardor")
                .email("sardor@example.com")
                .password("testPassword123")
                .build();

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(roleRepository.findByRole(UserRole.USER).get()).thenReturn(sampleRole);
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        // WHEN
        UserResponse response = userService.create(createRequest);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(createRequest.getName());
        assertThat(response.getEmail()).isEqualTo(createRequest.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }



    @Test
    @DisplayName("GIVEN a valid UserUpdateRequest and a valid ID WHEN update is called THEN the user is updated and UserResponse is returned")
    void shouldUpdateUser_whenRequestAndIdAreValid() {
        // GIVEN
        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .name("Sardor Updated")
                .email("updated@example.com")
                .password("newPassword123")
                .status("ACTIVE")
                .role("ADMIN")
                .build();
        Role adminRole = new Role();
        adminRole.setRole(UserRole.ADMIN);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(sampleUser));
        when(roleRepository.findByRole(UserRole.valueOf(updateRequest.getRole().toUpperCase(Locale.ROOT))).get()).thenReturn(adminRole);
        when(passwordEncoder.encode(any(String.class))).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        // WHEN
        UserResponse response = userService.update(1L, updateRequest);

        // THEN
        assertThat(response).isNotNull();
        assertThat(sampleUser.getName()).isEqualTo(updateRequest.getName());
        assertThat(sampleUser.getEmail()).isEqualTo(updateRequest.getEmail());
        assertThat(sampleUser.getRole()).isEqualTo(adminRole);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }



    @Test
    @DisplayName("GIVEN a non-existent user ID and a valid role WHEN update is called THEN a ResourceNotFoundException is thrown")
    void shouldThrowException_whenUpdatingWithNonExistentId() {
        // GIVEN
        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .name("Sardor")
                .email("sardor@example.com")
                .password("password123")
                .role("USER") // Roleni to'g'ri qiymat bilan o'rnatish
                .status("ACTIVE")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        when(roleRepository.findByRole(any(UserRole.class)).get()).thenReturn(new Role(UserRole.USER));

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> userService.update(99L, updateRequest));

        verify(userRepository, times(1)).findById(99L);
        verify(userRepository, never()).save(any(User.class));
    }



    @Test
    @DisplayName("GIVEN a valid user ID WHEN getById is called THEN a UserResponse is returned")
    void shouldReturnUser_whenIdIsValid() {
        // GIVEN
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(sampleUser));

        // WHEN
        UserResponse response = userService.getById(1L);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo(sampleUser.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }



    @Test
    @DisplayName("GIVEN a non-existent user ID WHEN getById is called THEN a ResourceNotFoundException is thrown")
    void shouldThrowException_whenGettingWithNonExistentId() {
        // GIVEN
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> userService.getById(99L));
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("GIVEN a valid user ID WHEN delete is called THEN the user is successfully deleted")
    void shouldDeleteUser_whenIdIsValid() {
        // GIVEN
        when(userRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(userRepository).deleteById(anyLong());

        // WHEN
        userService.delete(1L);

        // THEN
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("GIVEN a non-existent user ID WHEN delete is called THEN a ResourceNotFoundException is thrown")
    void shouldThrowException_whenDeletingWithNonExistentId() {
        // GIVEN
        when(userRepository.existsById(anyLong())).thenReturn(false);

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> userService.delete(99L));
        verify(userRepository, times(1)).existsById(99L);
        verify(userRepository, never()).deleteById(anyLong());
    }

}