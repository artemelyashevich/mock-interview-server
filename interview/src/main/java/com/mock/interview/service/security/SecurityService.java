package com.mock.interview.service.security;

import com.mock.interview.lib.entity.UserEntity;
import com.mock.interview.lib.exception.MockInterviewException;
import com.mock.interview.lib.model.UserModel;
import com.mock.interview.mapper.UserModelMapper;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityService {

    private static final UserModelMapper mapper = UserModelMapper.INSTANCE;

    private EntityManager em;
    private final ThreadLocal<UserModel> currentUser = new ThreadLocal<>();

    @Async
    public void setUser(Long id) throws MockInterviewException {
        var previous = currentUser.get();
        if (previous != null && previous.getId().equals(id)) {
            return;
        }
        currentUser.remove();
        var user = em.find(UserEntity.class, id);
        if (user == null) {
            throw new MockInterviewException("User with id: '%s' was not found".formatted(id), 404);
        }
        var model = mapper.toModel(user);
        currentUser.set(model);
    }

    public UserModel getCurrentUser() {
        return currentUser.get();
    }

    @PreDestroy
    public void destroy() {
        currentUser.remove();
    }
}
