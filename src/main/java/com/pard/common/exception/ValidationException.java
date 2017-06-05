package com.pard.common.exception;

import com.pard.common.validator.ValidateResults;

/**
 * Created by wawe on 17/5/30.
 */
public class ValidationException extends BusinessException {
    private ValidateResults results;

    public ValidationException(String message) {
        super(message, 400);
    }

    public ValidationException(ValidateResults results) {
        super(results.toString(), 400);
        this.results = results;
    }

    public ValidateResults getResults() {
        return results;
    }
}
