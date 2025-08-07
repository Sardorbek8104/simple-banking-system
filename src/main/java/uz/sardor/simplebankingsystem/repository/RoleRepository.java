package uz.sardor.simplebankingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.sardor.simplebankingsystem.entity.Role;
import uz.sardor.simplebankingsystem.enums.UserRole;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRole(UserRole userRole);
}
