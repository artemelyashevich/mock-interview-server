package com.mock.interview.auth.application.service;

import com.mock.interview.auth.application.port.in.RoleService;
import com.mock.interview.auth.application.port.out.RoleRepository;
import com.mock.interview.auth.infrastructure.persistence.mapper.RoleEntityMapper;
import com.mock.interview.lib.exception.ResourceAlreadyExistException;
import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.RoleModel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private static final RoleEntityMapper roleEntityMapper = RoleEntityMapper.INSTANCE;

    private final RoleRepository roleRepository;

    @Override
    public List<RoleModel> findAll() {
        log.debug("Attempting to fetch all roles");

        var roles = roleRepository.findAll();

        log.debug("Roles found: {}", roles);
        return roles;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public RoleModel findByName(@NonNull String name) {
        log.debug("Attempting to find role by name {}", name);

        var role = roleRepository.findByName(name);

        var r = new RoleModel();

        if (role.isEmpty()) {
            r = save(RoleModel.builder().name(name).build());
        } else {
            r = roleEntityMapper.toModel(role.get());
        }

        log.debug("Role found: {}", role);
        return r;
    }

    @Override
    @Transactional
    public RoleModel save(@NonNull RoleModel roleModel) {
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
    public void delete(@NonNull RoleModel roleModel) {
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
