package com.mock.interview.lib.specification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public abstract class GenericSpecificationService<T, ID> {

    protected abstract GenericSpecificationRepository<T, ID> getRepository();

    @SuppressWarnings("unchecked")
    public Page<T> search(SearchCriteria searchCriteria) {
        var spec = (Specification<T>) GenericSpecification.create(searchCriteria.getFilters());
        
        var sort = Sort.by(
            searchCriteria.getSortDirection(), 
            searchCriteria.getSortBy()
        );
        
        var pageable = PageRequest.of(
            searchCriteria.getPage(), 
            searchCriteria.getSize(), 
            sort
        );
        
        return getRepository().search(spec, pageable);
    }

    @SuppressWarnings("unchecked")
    public List<T> searchAll(SearchCriteria searchCriteria) {
        var spec = (Specification<T>) GenericSpecification.create(searchCriteria.getFilters());
        return getRepository().search(spec);
    }
}