package com.mock.interview.lib.contract;

import java.util.List;

public interface DtoMappable<D extends AbstractDto, M extends AbstractModel> {

    List<D> toDto(List<M> models);

    D toDto(M model);

    List<M> toModel(List<D> dtoList);

    M toModel(D dto);
}
