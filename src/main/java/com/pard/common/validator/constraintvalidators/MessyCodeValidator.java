package com.pard.common.validator.constraintvalidators;

import com.pard.common.utils.StringUtils;
import com.pard.common.validator.constraints.NotMessyCode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by wawe on 17/5/30.
 */
public class MessyCodeValidator implements ConstraintValidator<NotMessyCode, String> {

    public void initialize(NotMessyCode notMessyCode) {

    }

    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (null == s) return true;
        return !StringUtils.isMessyCode(s);
    }
}
