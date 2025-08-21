package com.mock.interview.lib.model;

import com.mock.interview.lib.contract.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.With;

import java.util.Collections;
import java.util.List;

@With
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Builder(toBuilder = true)
@AllArgsConstructor
public class InterviewTemplateModel extends AbstractModel {

    @EqualsAndHashCode.Include
    private Long id;
    
    @NonNull
    private String title;
    
    private String description;
    
    private DifficultyLevel difficulty;
    
    @Builder.Default
    private List<String> technologyStack = Collections.emptyList();

    public boolean isForTechnology(String technology) {
        return technologyStack.stream()
            .anyMatch(stackTech -> stackTech.equalsIgnoreCase(technology));
    }

    public boolean isAtDifficultyLevel(DifficultyLevel level) {
        return this.difficulty == level;
    }
}
