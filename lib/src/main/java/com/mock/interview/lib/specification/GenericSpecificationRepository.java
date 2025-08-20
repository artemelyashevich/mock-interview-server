package com.mock.interview.lib.specification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface GenericSpecificationRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    default Page<T> search(Specification<T> spec, Pageable pageable) {
        return findAll(spec, pageable);
    }

    default List<T> search(Specification<T> spec) {
        return findAll(spec);
    }
}