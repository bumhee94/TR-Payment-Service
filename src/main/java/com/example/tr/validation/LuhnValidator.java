package com.example.tr.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LuhnValidator implements ConstraintValidator<ValidCardNumber, String> {

    @Override
    public boolean isValid(String cardNumber, ConstraintValidatorContext context) {
        cardNumber = cardNumber.replaceAll("[\\s-]", "");
        if (cardNumber == null || !cardNumber.matches("\\d+")) {
            return false;
        }
        int totalSum = 0;
        boolean isSecondDigit = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (isSecondDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            totalSum += digit;
            isSecondDigit = !isSecondDigit;
        }
        return totalSum % 10 == 0;
    }
}
