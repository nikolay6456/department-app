package com.nikolay.rest;

import com.nikolay.model.Department;
import com.nikolay.service.DepartmentService;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Department rest controller.
 */
@RestController
@RequestMapping("/department")
public class DepartmentRestController {

    public static final Logger LOGGER = LogManager.getLogger();

    /**
     * The Department service.
     */
    private DepartmentService departmentService;

    @Autowired
    public DepartmentRestController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * Gets all departments.
     *
     * @return the all departments
     */
    @GetMapping("/")
    public ResponseEntity<List<Department>> getAllDepartments() {
        LOGGER.debug("getAllDepartments()");
        List<Department> departmentList = departmentService.getAllDepartments();
        return new ResponseEntity<>(departmentList, HttpStatus.OK);
    }

    /**
     * Gets department by id.
     *
     * @param id the id
     * @return the department by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable("id") Long id) {
        LOGGER.debug("getDepartmentById(): id = {}", id);
        Department department = departmentService.getDepartmentById(id);
        return new ResponseEntity<>(department, HttpStatus.FOUND);
    }
    
    @GetMapping("/check/{name}")
    public ResponseEntity<Boolean> checkDepartmentByName(@PathVariable("name") String departmentName) {
        LOGGER.debug("checkDepartmentByName(): name = {}", departmentName);
        Boolean checkDepartmentByName = departmentService.checkDepartmentByName(departmentName);
        return new ResponseEntity<>(checkDepartmentByName, HttpStatus.FOUND);
    }

    /**
     * Add department response entity.
     *
     * @param department the department
     * @return the response entity
     */
    @PostMapping("/")
    public ResponseEntity<Long> addDepartment(@RequestBody Department department) {
        LOGGER.debug("addDepartment(): departmentName = {}", department.getDepartmentName());
        Long id = departmentService.saveDepartment(department);
        department.setId(id);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    /**
     * Remove department response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity removeDepartment(@PathVariable("id") Long id) {
        LOGGER.debug("removeDepartment(): id = {}", id);
        departmentService.deleteDepartment(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Update department response entity.
     *
     * @param newDepartment the new department
     * @param id the id
     * @return the response entity
     */
    @PutMapping("/{id}")
    public ResponseEntity updateDepartment(@PathVariable Long id,
            @RequestBody Department newDepartment) {
        LOGGER.debug("updateDepartment(): id = {}, newDepartmentName = {}", id,
                newDepartment.getDepartmentName());
        Department department = departmentService.getDepartmentById(id);
        department.setDepartmentName(newDepartment.getDepartmentName());
        department.setAverageSalary(newDepartment.getAverageSalary());
        departmentService.updateDepartment(department);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}
