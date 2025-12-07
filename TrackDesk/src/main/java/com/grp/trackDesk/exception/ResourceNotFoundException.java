package com.grp.trackDesk.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s non trouvé avec %s : '%s'", resourceName, fieldName, fieldValue),
              HttpStatus.NOT_FOUND);
    }
    

}
