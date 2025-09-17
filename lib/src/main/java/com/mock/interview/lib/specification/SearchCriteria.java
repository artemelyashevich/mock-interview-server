package com.mock.interview.lib.specification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
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