package com.mock.interview.lib.contract;

import java.util.List;

public interface EntityMappable<E extends AbstractEntity, T extends AbstractModel> {

    List<T> toModels(List<E> entities);

    T toModel(E entity);

    E toEntity(T model);
}
