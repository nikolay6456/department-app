package com.nikolay.webapp.validate;

import com.nikolay.model.Department;
import com.nikolay.service.DepartmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The type Department validator.
 */
@Component
public class DepartmentValidator implements Validator {

  @Autowired
  private DepartmentService departmentService;

  /**
   * The constant LOGGER.
   */
  public static final Logger LOGGER = LogManager.getLogger();

  @Override
  public boolean supports(Class<?> depClass) {
    return Department.class.equals(depClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    LOGGER.debug("DepartmentValidator: validate()");
 
    Department department = (Department) o;

    String departmentName = department.getDepartmentName();

    if(departmentName == null || departmentName.isEmpty()) {

        errors.rejectValue("departmentName", "department.name.empty");

    } else {

        if(departmentService.checkDepartmentByName(departmentName)) {
          
          errors.rejectValue("departmentName", "department.alreadyExists");

        }
    }
  }

}