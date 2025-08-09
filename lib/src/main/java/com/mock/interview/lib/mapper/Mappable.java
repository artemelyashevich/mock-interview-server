package com.mock.interview.lib.mapper;

import com.mock.interview.lib.entity.AbstractEntity;

import java.util.List;

public interface Mappable<E extends AbstractEntity, T> {

  List<T> toModels(List<E> entities);

  T toModel(E entity);

  E toEntity(T model);
}
