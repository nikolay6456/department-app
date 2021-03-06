package com.nikolay.webapp.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nikolay.model.Department;
import com.nikolay.service.DepartmentService;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * The type Test department controller.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/mock-test-webapp.xml"})
public class TestDepartmentController {

  /**
   * The constant LOGGER.
   */
  public static final Logger LOGGER = LogManager.getLogger();

  @Autowired
  private DepartmentController departmentController;

  @Autowired
  private DepartmentService mockDepartmentService;

  private Department dep1 = new Department();
  private Department dep2 = new Department();
  private List<Department> departments;

  private MockMvc mockMvc;

  /**
   * Sets up.
   *
   * @throws ParseException the parse exception
   */
  @Before
  public void setUp() throws ParseException {
    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    viewResolver.setSuffix(".html");
    viewResolver.setPrefix("/WEB-INF/templates/");

    LOGGER.debug("execute before test method");
    dep1 = new Department("New Department", BigDecimal.valueOf(500));
    dep2 = new Department(14L, "Services", BigDecimal.valueOf(3249));
    departments = Arrays.asList(dep1, dep2);
    mockMvc = MockMvcBuilders.standaloneSetup(new ErrorController(), departmentController)
        .setViewResolvers(viewResolver)
        .build();
  }

  /**
   * Tear down.
   */
  @After
  public void tearDown() {
    reset(mockDepartmentService);
    LOGGER.error("execute: afterTest()");
  }

  /**
   * Test get department by id.
   *
   * @throws Exception the exception
   */
  @Test
  public void testGetDepartmentById() throws Exception {
    LOGGER.debug("test TestDepartmentRestController: run testGetDepartmentById()");
    when(mockDepartmentService.getDepartmentById(14L)).thenReturn(dep2);
    mockMvc.perform(
        get("/department/14")
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("department"))
        .andExpect(model().attributeExists("department"))
        .andExpect(model().attribute("department", dep2));
    verify(mockDepartmentService).getDepartmentById(14L);
  }

  /**
   * Test get all departments.
   *
   * @throws Exception the exception
   */
  @Test
  public void testGetAllDepartments() throws Exception {
    LOGGER.debug("test TestDepartmentRestController: run testGetAllDepartments()");
    when(mockDepartmentService.getAllDepartments()).thenReturn(departments);
    mockMvc.perform(
        get("/departments/")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("departments"))
        .andExpect(model().attributeExists("departmentList"));
    verify(mockDepartmentService).getAllDepartments();
  }

  /**
   * Test get add department.
   *
   * @throws Exception the exception
   */
  @Test
  public void testGetAddDepartment() throws Exception {
    LOGGER.debug("test TestDepartmentRestController: run testGetAddDepartment()");
    mockMvc.perform(get("/department/add")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("addDepartment"))
        .andExpect(model().attributeExists("department"));
  }

  /**
   * Test get update department.
   *
   * @throws Exception the exception
   */
  @Test
  public void testGetUpdateDepartment() throws Exception {
    LOGGER.debug("test TestDepartmentRestController: run testGetAddDepartment()");
    when(mockDepartmentService.getDepartmentById(14L)).thenReturn(dep2);
    mockMvc.perform(get("/department/14/edit")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("editDepartment"))
        .andExpect(model().attributeExists("department"));
    verify(mockDepartmentService).getDepartmentById(14L);
  }

  /**
   * Test post add department.
   *
   * @throws Exception the exception
   */
  @Test
  public void testPostAddDepartment() throws Exception {
    LOGGER.debug("test TestDepartmentRestController: run testPostAddDepartment()");
    when(mockDepartmentService.saveDepartment(any(Department.class))).thenReturn(1L);
    mockMvc.perform(post("/department/add")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .param("departmentName", dep1.getDepartmentName())
        .requestAttr("department", new Department()))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/departments"));
    verify(mockDepartmentService).saveDepartment(any(Department.class));
  }

  /**
   * Test post update department.
   *
   * @throws Exception the exception
   */
  @Test
  public void testPostUpdateDepartment() throws Exception {
    LOGGER.debug("test TestDepartmentRestController: run testPostUpdateDepartment()");
    doNothing().when(mockDepartmentService).updateDepartment(any(Department.class));
    mockMvc.perform(post("/department/14/edit")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .param("id", dep2.getId().toString())
        .param("departmentName", dep2.getDepartmentName())
        .requestAttr("department", dep2))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(view().name("redirect:/departments"));
    verify(mockDepartmentService).updateDepartment(any(Department.class));
  }

  /**
   * Test error handler.
   *
   * @throws Exception the exception
   */
  @Test
  public void testErrorHandler() throws Exception {
    LOGGER.debug("test TestDepartmentRestController: run testErrorHandler()");
    when(mockDepartmentService.getDepartmentById(anyLong())).thenThrow(Exception.class);
    mockMvc.perform(get("/department/{id}", anyLong()))
        .andDo(print())
        .andExpect(view().name("_404"));
    verify(mockDepartmentService).getDepartmentById(anyLong());
  }

  /**
   * Test remove department.
   *
   * @throws Exception the exception
   */
  @Test
  public void testRemoveDepartment() throws Exception {
    LOGGER.debug("test TestDepartmentRestController: run testRemoveDepartment()");
    doNothing().when(mockDepartmentService).deleteDepartment(1L);
    mockMvc.perform(
        get("/department/1/delete")
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(view().name("redirect:/departments"))
        .andExpect(status().isFound());
    verify(mockDepartmentService).deleteDepartment(1L);

  }

}
