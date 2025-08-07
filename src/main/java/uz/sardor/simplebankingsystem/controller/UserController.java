package uz.sardor.simplebankingsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.sardor.simplebankingsystem.dto.user.request.UserCreateRequest;
import uz.sardor.simplebankingsystem.dto.user.request.UserUpdateRequest;
import uz.sardor.simplebankingsystem.dto.user.response.UserResponse;
import uz.sardor.simplebankingsystem.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    private ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    private ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    private ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    private ResponseEntity<String> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok("Deleted user with id " + id);
    }

    @DeleteMapping("/getAll")
    private ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }
}
