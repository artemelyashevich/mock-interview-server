package com.mock.interview.lib.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@UtilityClass
public class GenericSpecification {

    public static <T> Specification<T> create(@NonNull Map<String, Object> searchCriteria) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new ArrayList<Predicate>();

            for (var entry : searchCriteria.entrySet()) {
                var key = entry.getKey();
                var value = entry.getValue();

                if (value == null || value.toString().isBlank()) {
                    continue;
                }

                if (key.contains(".")) {
                    handleNestedField(key, value, root, criteriaBuilder, predicates);
                }
                if (key.endsWith("_like")) {
                    handleLikeOperator(key, value, root, criteriaBuilder, predicates);
                }
                if (key.endsWith("_in")) {
                    handleInOperator(key, value, root, predicates);
                }
                if (key.endsWith("_from")) {
                    handleFromOperator(key, value, root, criteriaBuilder, predicates);
                }
                if (key.endsWith("_to")) {
                    handleToOperator(key, value, root, criteriaBuilder, predicates);
                }
                if (key.endsWith("_not")) {
                    handleNotOperator(key, value, root, criteriaBuilder, predicates);
                } else {
                    handleEqualsOperator(key, value, root, criteriaBuilder, predicates);
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static <T> void handleNestedField(String key, Object value, Root<T> root, 
                                            CriteriaBuilder cb, List<Predicate> predicates) {
        var parts = key.split("\\.");
        var path = root.get(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            path = path.get(parts[i]);
        }
        predicates.add(cb.equal(path, value));
    }

    private static <T> void handleLikeOperator(String key, Object value, Root<T> root, 
                                             CriteriaBuilder cb, List<Predicate> predicates) {
        var fieldName = key.replace("_like", "");
        predicates.add(cb.like(cb.lower(root.get(fieldName)), 
                             "%" + value.toString().toLowerCase() + "%"));
    }

    private static <T> void handleInOperator(String key, Object value, Root<T> root, 
                                            List<Predicate> predicates) {
        var fieldName = key.replace("_in", "");
        if (value instanceof Collection) {
            predicates.add(root.get(fieldName).in((Collection<?>) value));
        } else if (value instanceof Object[]) {
            predicates.add(root.get(fieldName).in((Object[]) value));
        }
    }

    @SuppressWarnings("all")
    private static <T> void handleFromOperator(String key, Object value, Root<T> root,
                                             CriteriaBuilder cb, List<Predicate> predicates) {
        var fieldName = key.replace("_from", "");
        if (value instanceof LocalDateTime) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(fieldName), (LocalDateTime) value));
        } else if (value instanceof Comparable comparable) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(fieldName),comparable));
        }
    }

    @SuppressWarnings("all")
    private static <T> void handleToOperator(String key, Object value, Root<T> root, 
                                           CriteriaBuilder cb, List<Predicate> predicates) {
        var fieldName = key.replace("_to", "");
        if (value instanceof LocalDateTime) {
            predicates.add(cb.lessThanOrEqualTo(root.get(fieldName), (LocalDateTime) value));
        } else if (value instanceof Comparable) {
            predicates.add(cb.lessThanOrEqualTo(root.get(fieldName), (Comparable) value));
        }
    }

    private static <T> void handleNotOperator(String key, Object value, Root<T> root, 
                                            CriteriaBuilder cb, List<Predicate> predicates) {
        var fieldName = key.replace("_not", "");
        predicates.add(cb.notEqual(root.get(fieldName), value));
    }

    private static <T> void handleEqualsOperator(String key, Object value, Root<T> root, 
                                               CriteriaBuilder cb, List<Predicate> predicates) {
        predicates.add(cb.equal(root.get(key), value));
    }
}