package com.example.tr.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AmountValidator implements ConstraintValidator<ValidAmount, Double> {

    @Override
    public boolean isValid(Double amount, ConstraintValidatorContext context) {
        // 금액이 null이거나 0보다 작거나 같으면 유효하지 않음
        return amount != null && amount > 0;
    }
}
