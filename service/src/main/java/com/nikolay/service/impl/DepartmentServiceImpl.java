package com.nikolay.service.impl;

import com.nikolay.dao.DepartmentDAO;
import com.nikolay.model.Department;
import com.nikolay.service.DepartmentService;
import com.nikolay.service.exception.OperationFailedException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * The type Department service.
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    /**
     * The constant LOGGER.
     */
    public static final Logger LOGGER = LogManager.getLogger();

    private DepartmentDAO departmentDAO;

    /**
     * Sets department dao.
     *
     * @param departmentDAO the department dao
     */
    public void setDepartmentDAO(DepartmentDAO departmentDAO) {
        LOGGER.debug("getAllDepartments()");
        this.departmentDAO = departmentDAO;
    }

    @Override
    public List<Department> getAllDepartments() {
        LOGGER.debug("getAllDepartments()");
        return departmentDAO.getAllDepartments();
    }

    @Override
    public Department getDepartmentById(Long departmentId) {
        LOGGER.debug("getDepartmentById(id): id = {}", departmentId);
        if (departmentId == null || departmentId < 0) {
            throw new OperationFailedException("Department identifier shouldn't be null");
        }
        return departmentDAO.getDepartmentById(departmentId);
    }

    @Override
    public Boolean checkDepartmentByName(String departmentName) {
        LOGGER.debug("checkDepartmentByName(departmentName): departmentName = {}", departmentName);
        if (departmentName == null) {
            throw new OperationFailedException("Department name shouldn't be null");
        }
        return departmentDAO.checkDepartmentByName(departmentName);
    }

    @Override
    public Long saveDepartment(Department department) {
        LOGGER.debug("saveDepartment(department): departmentName = {}",
                department.getDepartmentName());
        checkDepartment(department);
        if (department.getId() != null) {
            throw new OperationFailedException(("Department identifier should be null"));
        }
        return departmentDAO.saveDepartment(department);
    }

    @Override
    public void updateDepartment(Department department) {
        LOGGER.debug("updateDepartment(department): departmentId = {}", department.getId());
        checkDepartment(department);
        if (department.getId() == null) {
            throw new OperationFailedException("Department identifier shouldn't be null");
        }
        departmentDAO.updateDepartment(department);
    }

    @Override
    public void deleteDepartment(Long departmentId) {
        LOGGER.debug("deleteDepartment(id) id = {}", departmentId);
        if (departmentId == null || departmentId < 0) {
            throw new OperationFailedException("Department identifier shouldn't be null");
        }
         departmentDAO.deleteDepartment(departmentId);
    }

    /**
     * Check object department for null
     */
    private void checkDepartment(Department department) throws OperationFailedException {
        if (department == null) {
            throw new OperationFailedException("Department shouldn't be null");
        }
        if (department.getDepartmentName() == null) {
            throw new OperationFailedException("Department name shouldn't be null");
        }
    }
}
