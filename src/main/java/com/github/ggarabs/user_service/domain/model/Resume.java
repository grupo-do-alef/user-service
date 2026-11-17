package com.github.ggarabs.user_service.domain.model;

import java.util.List;

public record Resume(List<Education> education, List<String> skills, List<Experience> experiences) {
    public static Resume empty() {
        return new Resume(List.of(), List.of(), List.of());
    }

    public boolean hasRequiredFields() {
        return education != null && !education.isEmpty()
                && education.stream().allMatch(item -> item != null && item.isComplete())
                && skills != null && !skills.isEmpty() && skills.stream().allMatch(Resume::hasText)
                && experiences != null && !experiences.isEmpty()
                && experiences.stream().allMatch(item -> item != null && item.isComplete());
    }

    public record Education(String course, String level, String institution, String start, String end,
            boolean ongoing) {

        private boolean isComplete() {
            return hasText(course) && hasText(level) && hasText(institution) && hasText(start)
                    && (ongoing || hasText(end));
        }
    }

    public record Experience(String role, String company, boolean current, String start, String end,
            String modality, String description) {

        private boolean isComplete() {
            return hasText(role) && hasText(company) && hasText(start) && hasText(modality)
                    && (current || hasText(end));
        }
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
