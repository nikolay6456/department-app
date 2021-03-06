package com.nikolay.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nikolay.dao.DepartmentDAO;
import com.nikolay.model.Department;
import java.math.BigDecimal;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The type Test department rest dao.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/test-dao-rest.xml"})
public class TestDepartmentRestDao {

    private static final Logger LOGGER = LogManager.getLogger();

    @Value("${department.endpoint}")
    private String url;

    @Value("${department.endpoint.with.id}")
    private String urlWithIdParam;

    @Value("${department.endpoint.with.name}")
    private String urlWithNameParam;

    @Autowired
    private DepartmentDAO departmentRestDao;

    @Autowired
    private RestTemplate mockRestTemplate;

    private Department dep1;
    private Department dep2;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        dep1 = new Department("New Department", BigDecimal.valueOf(500));
        dep2 = new Department(1L, "Services", BigDecimal.valueOf(3249));
        LOGGER.error("execute: beforeTest()");
    }

    @Test
    public void testGetDepartmentById() {
        LOGGER.debug("test TestDepartmentRestDao: run testGetDepartmentById()");
        when(mockRestTemplate.getForObject(urlWithIdParam, Department.class, 1L))
                .thenReturn(dep2);
        Department department = departmentRestDao.getDepartmentById(1L);
        assertNotNull(department);
        assertEquals(1L, department.getId().longValue());
        verify(mockRestTemplate, times(1))
                .getForObject(urlWithIdParam, Department.class, 1L);
    }

    @Test
    public void testCheckDepartmentByName() {
        LOGGER.debug("test TestDepartmentRestDao run: testCheckDepartmentByName()");
        when(mockRestTemplate.getForObject(urlWithNameParam, Boolean.class, "Services"))
                .thenReturn(true);
        Boolean checkDepartmentByName = departmentRestDao.checkDepartmentByName("Services");
        assertNotNull(checkDepartmentByName);
        assertTrue(checkDepartmentByName);
        verify(mockRestTemplate, times(1)).getForObject(urlWithNameParam, Boolean.class, "Services");
    }

    @Test
    public void testGetAllDepartment() {
        LOGGER.debug("test TestDepartmentRestDao: run testGetAllDepartment()");
        when(mockRestTemplate.getForObject(url, Department[].class)).thenReturn(
                new Department[]{dep1, dep2});
        List<Department> departmentList = departmentRestDao.getAllDepartments();
        assertNotNull(departmentList);
        assertEquals(2, departmentList.size());
        verify(mockRestTemplate, times(1)).getForObject(url, Department[].class);
    }

    @Test
    public void testDeleteDepartment() {
        LOGGER.debug("test TestDepartmentRestDao: run testDeleteDepartment()");
        doNothing().when(mockRestTemplate).delete(urlWithIdParam, 1L);
        departmentRestDao.deleteDepartment(1L);
        verify(mockRestTemplate, times(1)).delete(urlWithIdParam, 1L);
    }

    @Test
    public void testUpdateDepartment() {
        LOGGER.debug("test TestDepartmentRestDao: run testUpdateDepartment()");
        doNothing().when(mockRestTemplate).put(urlWithIdParam, dep2, 1L);
        departmentRestDao.updateDepartment(dep2);
        verify(mockRestTemplate, times(1)).put(urlWithIdParam, dep2, 1L);
    }

    @Test
    public void testSaveDepartment() {
        LOGGER.debug("test TestDepartmentRestDao: run testSaveDepartment()");
        when(mockRestTemplate.postForEntity(url, dep1, Long.class))
                .thenReturn(new ResponseEntity<>(1L, HttpStatus.FOUND));
        Long departmentId = departmentRestDao.saveDepartment(dep1);
        assertNotNull(departmentId);
        assertEquals(1L, departmentId.longValue());
        verify(mockRestTemplate, times(1)).postForEntity(url, dep1, Long.class);
    }

}
