package com.mock.interview.auth.application.service;

import com.mock.interview.auth.application.port.in.RoleService;
import com.mock.interview.auth.application.port.out.RoleRepository;
import com.mock.interview.lib.exception.ResourceAlreadyExistException;
import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.RoleModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<RoleModel> findAll() {
        log.debug("Attempting to fetch all roles");

        var roles = roleRepository.findAll();

        log.debug("Roles found: {}", roles);
        return roles;
    }

    @Override
    public RoleModel findByName(String name) {
        log.debug("Attempting to find role by name {}", name);

        var role = roleRepository.findByName(name);

        log.debug("Role found: {}", role);
        return role;
    }

    @Override
    @Transactional
    public RoleModel save(RoleModel roleModel) {
        log.debug("Attempting to save role {}", roleModel);

        if (roleRepository.existsByName(roleModel.getName())) {
            var message = String.format("Role with name %s already exists", roleModel.getName());
            log.debug(message);
            throw new ResourceAlreadyExistException(message);
        }

        var role = roleRepository.save(roleModel);

        log.debug("Role saved: {}", role);
        return role;
    }

    @Override
    public void delete(RoleModel roleModel) {
        log.debug("Attempting to delete role {}", roleModel);

        if (!roleRepository.existsByName(roleModel.getName())) {
            var message = String.format("Role with name %s does not exist", roleModel.getName());
            log.debug(message);
            throw new ResourceNotFoundException(message);
        }

        roleRepository.delete(roleModel);
        log.debug("Role deleted: {}", roleModel);
    }
}
