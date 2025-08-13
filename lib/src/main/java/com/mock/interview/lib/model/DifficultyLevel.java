package com.mock.interview.lib.model;

public enum DifficultyLevel {
        JUNIOR,
        INTERMEDIATE,
        SENIOR,
        EXPERT;

        public String getDisplayName() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }
