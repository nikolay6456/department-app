package com.nikolay.dao;

import com.nikolay.model.Department;
import java.util.List;

/**
 * The interface Department dao.
 */
public interface DepartmentDAO {

    /**
     * Gets all departments.
     *
     * @return the all departments
     */
    List<Department> getAllDepartments();

    /**
     * Gets department by id.
     *
     * @param departmentId the department id
     * @return the department by id
     */
    Department getDepartmentById(Long departmentId);

    /**
     * Check department by name.
     *
     * @param departmentName the department name
     * @return the boolean
     */
    Boolean checkDepartmentByName(String departmentName);

    /**
     * Save department long.
     *
     * @param department the department
     * @return the long
     */
    Long saveDepartment(Department department);


    /**
     * Update department.
     *
     * @param department the department
     */
    void updateDepartment(Department department);

    /**
     * Delete department.
     *
     * @param departmentId the department id
     */
    void deleteDepartment(Long departmentId);
}
