package com.mock.interview.lib.specification;

import lombok.Data;
import org.springframework.data.domain.Sort;
import java.util.HashMap;
import java.util.Map;

@Data
public class SearchCriteria {
    private Map<String, Object> filters = new HashMap<>();
    private Integer page = 0;
    private Integer size = 20;
    private String sortBy = "id";
    private Sort.Direction sortDirection = Sort.Direction.ASC;

    public void addFilter(String key, Object value) {
        if (value != null) {
            filters.put(key, value);
        }
    }

    public void removeFilter(String key) {
        filters.remove(key);
    }
}