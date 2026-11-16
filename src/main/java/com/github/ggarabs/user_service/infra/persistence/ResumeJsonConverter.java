package com.github.ggarabs.user_service.infra.persistence;

import com.github.ggarabs.user_service.domain.model.Resume;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

@Converter
public class ResumeJsonConverter implements AttributeConverter<Resume, String> {

    private static final JsonMapper MAPPER = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    @Override
    public String convertToDatabaseColumn(Resume resume) {
        return resume == null ? null : MAPPER.writeValueAsString(resume);
    }

    @Override
    public Resume convertToEntityAttribute(String json) {
        return json == null ? null : MAPPER.readValue(json, Resume.class);
    }
}
