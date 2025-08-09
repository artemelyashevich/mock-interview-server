package com.mock.interview.auth.infrastructure.persistence.adapter;

import com.mock.interview.auth.application.port.out.RoleRepository;
import com.mock.interview.auth.infrastructure.persistence.mapper.RoleEntityMapper;
import com.mock.interview.auth.infrastructure.persistence.repository.RoleEntityRepository;
import com.mock.interview.lib.exception.ResourceNotFoundException;
import com.mock.interview.lib.model.RoleModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

  private static final RoleEntityMapper roleEntityMapper = RoleEntityMapper.INSTANCE;

  private final RoleEntityRepository roleRepository;

  @Override
  public RoleModel findByName(String name) {
    return roleEntityMapper.toModel(roleRepository.findByName(name)
            .orElseThrow(() -> {
              var message = String.format("Role with name %s not found", name);
              log.info(message);
              return new ResourceNotFoundException(message);
            }));
  }

  @Override
  public List<RoleModel> findAll() {
    return roleEntityMapper.toModels(roleRepository.findAll());
  }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
  public RoleModel save(RoleModel roleModel) {
    return roleEntityMapper.toModel(
            roleRepository.save(
                    roleEntityMapper.toEntity(roleModel)
            )
    );
  }

  @Override
  public void delete(RoleModel roleModel) {
    roleRepository.deleteById(roleModel.getId());
  }
}
