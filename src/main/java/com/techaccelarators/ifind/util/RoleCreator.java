package com.techaccelarators.ifind.util;

import com.techaccelarators.ifind.domain.Role;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class RoleCreator implements CommandLineRunner {
    private final AdminRoleProperties adminRoleProperties;
    private final UserRoleProperties userRoleProperties;
    private final RoleRepository roleRepository;

    public RoleCreator(AdminRoleProperties adminRoleProperties, UserRoleProperties userRoleProperties, RoleRepository roleRepository) {
        this.adminRoleProperties = adminRoleProperties;
        this.userRoleProperties = userRoleProperties;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = new Role();
        adminRole.setName(adminRoleProperties.getName());
        adminRole.setStatus(Status.ACTIVE);
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setName(userRoleProperties.getName());
        userRole.setStatus(Status.ACTIVE);
        roleRepository.save(userRole);
    }
}
