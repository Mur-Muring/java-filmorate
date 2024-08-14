package utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class NotBeforeDateValidator implements ConstraintValidator<NotBeforeDate, LocalDate> {

    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate data, ConstraintValidatorContext constraintValidatorContext) {
        if (data != null) {
            return data.isAfter(MIN_DATE);
        }
        return true;
    }
}
