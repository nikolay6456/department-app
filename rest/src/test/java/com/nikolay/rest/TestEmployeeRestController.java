package com.nikolay.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikolay.model.Employee;
import com.nikolay.service.EmployeeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/test-rest-mock.xml"})
public class TestEmployeeRestController {

    @Autowired
    EmployeeService mockEmployeeService;

    @Resource
    private EmployeeRestController employeeRestController;

    private MockMvc mockMvc;

    private Employee emp1;
    private Employee emp2;
    private Employee emp3;
    private List<Employee> employees;

    @Before
    public void setUp() {
        emp3 = new Employee(3L, "Nikolay Kozak", LocalDate.of(1999, 2, 28), BigDecimal.valueOf(350));
        emp1 = new Employee(1L, 1L, "Nikolay Kozak", LocalDate.of(1999, 2, 28), BigDecimal.valueOf(350));
        emp2 = new Employee(2L, 1L, "Dmitry Kozak", LocalDate.of(2000, 12, 5), BigDecimal.valueOf(300));
        employees = Arrays.asList(emp1, emp2);
        mockMvc = standaloneSetup(employeeRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mockEmployeeService);
        reset(mockEmployeeService);
    }

    @Test
    public void testGetAllEmployee() throws Exception {
        when(mockEmployeeService.getAllEmployees()).thenReturn(employees);
        mockMvc.perform(
                get("/employee/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        verify(mockEmployeeService).getAllEmployees();
    }

    @Test
    public void testAddEmployee() throws Exception {
        when(mockEmployeeService.saveEmployee(any(Employee.class))).thenReturn(1L);
        String employee = new ObjectMapper().writeValueAsString(emp3);
        mockMvc.perform(
                post("/employee/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employee))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
        verify(mockEmployeeService).saveEmployee(any(Employee.class));
    }

    @Test
    public void testUpdateDepartment() throws Exception {
        when((mockEmployeeService.getEmployeeById(1L))).thenReturn(emp1);
        doNothing().when(mockEmployeeService).updateEmployee(emp1);
        String department = new ObjectMapper().writeValueAsString(emp1);
        mockMvc.perform(
                put("/employee/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(department)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
        verify(mockEmployeeService).getEmployeeById(1L);
        verify(mockEmployeeService).updateEmployee(emp1);
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        when(mockEmployeeService.getEmployeeById(1L)).thenReturn(emp1);
        mockMvc.perform(
                get("/employee/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(status().isFound());
        verify(mockEmployeeService).getEmployeeById(1L);
    }

    @Test
    public void testGetEmployeeByDateOfBirthday() throws Exception {
        LocalDate date = LocalDate.of(1999, 2, 28);
        when(mockEmployeeService.getEmployeeByDateOfBirthday(date)).thenReturn(Collections.singletonList(emp1));
        mockMvc.perform(
                get("/employee/date/{date}", date)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].fullName").value("Nikolay Kozak"));
        verify(mockEmployeeService).getEmployeeByDateOfBirthday(date);
    }

    @Test
    public void testGetEmployeeBetweenDatesOfBirthday() throws Exception {
        LocalDate from = LocalDate.of(1999, 2, 28);
        LocalDate to = LocalDate.of(2000, 12, 5);
        when(mockEmployeeService.getEmployeeBetweenDatesOfBirthday(from, to)).thenReturn(employees);
        mockMvc.perform(
                get("/employee/date/{from}/{to}", from, to)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].fullName").value("Nikolay Kozak"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].fullName").value("Dmitry Kozak"));
        verify(mockEmployeeService).getEmployeeBetweenDatesOfBirthday(from, to);
    }


    @Test
    public void testRemoveEmployee() throws Exception {
        doNothing().when(mockEmployeeService).deleteEmployee(1L);
        mockMvc.perform(
                delete("/employee/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        verify(mockEmployeeService).deleteEmployee(1L);
    }
}
