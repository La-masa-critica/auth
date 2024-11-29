package LMC.auth.config.boostrap;

import LMC.auth.models.Permission;
import LMC.auth.models.Role;
import LMC.auth.repositories.PermissionRepository;
import LMC.auth.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final DataInitializationProperties properties;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (!properties.isEnabled()) {
            logger.info("Data initialization is disabled");
            return;
        }
        try {
            initializePermissions();
            initializeRoles();
            logger.info("Data initialization completed successfully");
        } catch (Exception e) {
            logger.error("Error during data initialization", e);
            throw new RuntimeException("Failed to initialize data", e);
        }
    }

    private void initializePermissions() {
        if (permissionRepository.count() == 0) {
            Set<Permission> permissions = properties.getPermissions().stream()
                    .map(name -> Permission.builder()
                            .name(name)
                            .build())
                    .collect(Collectors.toSet());

            permissionRepository.saveAll(permissions);
            logger.info("Created {} default permissions", permissions.size());
        }
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            for (DataInitializationProperties.RoleDefinition roleDef : properties.getRoles()) {
                Set<Permission> rolePermissions = new HashSet<>();

                for (String permissionName : roleDef.getPermissions()) {
                    Permission permission = permissionRepository.findByName(permissionName)
                            .orElseThrow(() -> new RuntimeException(
                                    "Permission not found: " + permissionName));
                    rolePermissions.add(permission);
                }

                Role role = Role.builder()
                        .name(roleDef.getName())
                        .permissions(rolePermissions)
                        .build();

                roleRepository.save(role);
                logger.info("Created role: {} with {} permissions",
                        role.getName(), rolePermissions.size());
            }
        }
    }
}