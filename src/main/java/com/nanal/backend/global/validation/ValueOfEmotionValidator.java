package com.nanal.backend.global.validation;

import com.nanal.backend.domain.diary.entity.Emotion;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ValueOfEmotionValidator implements ConstraintValidator<ValueOfEmotion, CharSequence> {
    private List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEmotion annotation) {
        acceptedValues = Emotion.EMOTIONS;
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return acceptedValues.contains(value.toString());
    }
}
