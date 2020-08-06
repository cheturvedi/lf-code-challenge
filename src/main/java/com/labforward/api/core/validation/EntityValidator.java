package com.labforward.api.core.validation;

import com.google.common.base.Preconditions;
import com.labforward.api.core.exception.EntityValidationException;
import com.labforward.api.hello.domain.Greeting;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

public class EntityValidator {

	public static final String MESSAGE_NO_ID_MATCH = "id provided does not match resource";
	public static final String MESSAGE_INVALID_ID = "id provided is not valid";

	private SpringValidatorAdapter validatorAdapter;

	@Autowired
	public EntityValidator(SpringValidatorAdapter validatorAdapter) {
		this.validatorAdapter = validatorAdapter;
	}

	public void validateUpdate(Object target, Object... groups) throws EntityValidationException {
		validate(target, groups);
	}

	
	public void validateId(String id) throws EntityValidationException {
		Greeting target = new Greeting();
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(target, target.getClass().getName());
		try {
			
			Preconditions.checkArgument(id != null);
			Preconditions.checkArgument(!id.trim().isEmpty());
			if (id != "default") {
				UUID.fromString(id);
				
			}
		}catch(IllegalArgumentException e) {
			FieldError fieldError = new FieldError(target.getClass().getName(), "id", MESSAGE_INVALID_ID);
			result.addError(fieldError);
			throw new EntityValidationException(result);
		}
	}

	public void validateUpdate(String id, Entity target) throws EntityValidationException {
		validateId(id);
		Preconditions.checkArgument(target != null);
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(target, target.getClass().getName());

		
		// validate ids match
		if (!id.equals(target.getId())) {
			FieldError fieldError = new FieldError(target.getClass().getName(), "id", MESSAGE_NO_ID_MATCH);
			result.addError(fieldError);
			throw new EntityValidationException(result);
		}

		validatorAdapter.validate(target, result, EntityUpdateValidatorGroup.class);

		if (result.hasErrors()) {
			throw new EntityValidationException(result);
		}
	}
	

	protected void validate(Object target, Object... groups) throws EntityValidationException {
		Preconditions.checkArgument(target != null);

		BeanPropertyBindingResult result = new BeanPropertyBindingResult(target, target.getClass().getName());
		validatorAdapter.validate(target, result, groups);

		if (result.hasErrors()) {
			throw new EntityValidationException(result);
		}
	}
}
