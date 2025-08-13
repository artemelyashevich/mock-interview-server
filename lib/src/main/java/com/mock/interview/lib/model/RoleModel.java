package com.mock.interview.lib.model;

import com.mock.interview.lib.contract.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.io.Serializable;

@With
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RoleModel extends AbstractModel {
    private Long id;

    private String name;
}
