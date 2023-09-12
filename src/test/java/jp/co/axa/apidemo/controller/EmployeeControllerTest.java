package jp.co.axa.apidemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.entities.User;
import jp.co.axa.apidemo.helpers.AuthRequest;
import jp.co.axa.apidemo.helpers.EmployeeRequest;
import jp.co.axa.apidemo.helpers.EmployeeResponse;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.repositories.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class EmployeeControllerTest {

    private static final String BASE_URL = "/api/v1";
    private String token_value="";

    @Autowired
    protected MockMvc mockMvc;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void  setUp() throws JsonProcessingException {
        // create user by repository
        User expectedUser = new User();
        expectedUser.setEmail("tariqul@gmail.com");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode("admin1234");
        expectedUser.setPassword(encodePassword);
        User savedUser = null;
        User findUser = userRepository.findByEmail("tariqul@gmail.com").orElse(null);
        if (findUser == null ) {
            savedUser = userRepository.save(expectedUser);
        } else {
            savedUser = findUser;
        }


        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
        assertThat(savedUser.getUsername()).isNotEmpty();
         AuthRequest authRequest = new AuthRequest();
                authRequest.setEmail(savedUser.getEmail());
                authRequest.setPassword("admin1234");
        String requestBody = objectMapper.writeValueAsString(authRequest);
       try {
        MvcResult result =   mockMvc.perform(post("/auth/login").content(requestBody)
                   .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        token_value = JsonPath.read(result.getResponse().getContentAsString(), "$.accessToken");
        System.out.println(token_value);

       } catch (Exception e) {
           throw new RuntimeException(e);
       }

   }

   @Test
   public void shouldReturnSingleEmployee() {
       String barerToken = "Bearer " + token_value;
       // save Employee
       System.out.println("Should employee Save");
       Employee emp = new Employee();
       emp.setSalary(1000);
       emp.setName("Test employee 1");
       emp.setDepartment("DDSK");
       employeeRepository.save(emp);

       Employee emp1 = new Employee();
       emp1.setSalary(34000);
       emp1.setName("Test employee 2");
       emp1.setDepartment("DDDD");
       employeeRepository.save(emp1);

       try {
           MvcResult result =   mockMvc.perform(authGetRequestBuilder("/api/v1/employees/"+emp1.getId(), barerToken, "get").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
           //token_value = JsonPath.read(result.getResponse().getContentAsString(), "$.accessToken");
           Employee employee = objectMapper.readValue(result.getResponse().getContentAsString(), Employee.class);
           assertThat(employee.getName()).isEqualTo("Test employee 2");
           assertThat(employee.getDepartment()).isEqualTo("DDDD");
           System.out.println(employee);

       } catch (Exception e) {
           throw new RuntimeException(e);
       }
   }

   @Test
   public void shouldGetAllEmployees() {
       String barerToken = "Bearer " + token_value;
       employeeRepository.deleteAll();
       // save Employee
       System.out.println("Should employee Save");
       Employee emp = new Employee();
       emp.setSalary(1000);
       emp.setName("Test employee 1");
       emp.setDepartment("DDSK");
       employeeRepository.save(emp);

       Employee emp1 = new Employee();
       emp1.setSalary(34000);
       emp1.setName("Test employee 2");
       emp1.setDepartment("DDDD");
       employeeRepository.save(emp1);

       try {
           MvcResult result =   mockMvc.perform(authGetRequestBuilder("/api/v1/employees", barerToken, "get").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
           //token_value = JsonPath.read(result.getResponse().getContentAsString(), "$.accessToken");
           List<Employee> employeeList = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), Employee[].class));
           assertThat(employeeList.size()).isEqualTo(2);
           assertThat(employeeList.get(0).getName()).isEqualTo("Test employee 1");
           System.out.println(employeeList);

       } catch (Exception e) {
           throw new RuntimeException(e);
       }


   }

   @Test
   public void shouldEmployeeSavedIntoSystem() throws JsonProcessingException {
       String barerToken = "Bearer " + token_value;
       // save Employee
       EmployeeRequest employeeRequest = new EmployeeRequest();
       employeeRequest.setDepartment("UDDD");
       employeeRequest.setSalary(1000);
       employeeRequest.setName("Test Employee 23");
       String requestBody = objectMapper.writeValueAsString(employeeRequest);
       try {
           MvcResult result =   mockMvc.perform(authGetRequestBuilder("/api/v1/employees", barerToken, "post").content(requestBody)
                   .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

           EmployeeResponse employeeResponse =  objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeResponse.class);
           System.out.println(employeeResponse);
           assertThat(employeeResponse.getStatus()).isEqualTo("success");
           assertThat(employeeResponse.getMsg()).isEqualTo("Employee Saved Successfully");
           assertThat(employeeResponse.getEmployee().getSalary()).isEqualTo(1000);
           assertThat(employeeResponse.getEmployee().getName()).isEqualTo("Test Employee 23");
           assertThat(employeeResponse.getEmployee().getDepartment()).isEqualTo("UDDD");

       } catch (Exception e) {
           throw new RuntimeException(e);
       }

   }

   @Test
   public void shouldEmployeeDeleteInfoSystem() throws JsonProcessingException {
       String barerToken = "Bearer " + token_value;

       //save employee
       System.out.println("Should employee Save");
       Employee emp = new Employee();
       emp.setSalary(1000);
       emp.setName("Test Delete Employee 1");
       emp.setDepartment("DDSK");
       Employee saveEmp = employeeRepository.save(emp);

       try {
           MvcResult result =   mockMvc.perform(authGetRequestBuilder("/api/v1/employees/"+ saveEmp.getId(), barerToken, "delete")
                   .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
           EmployeeResponse employeeResponse =  objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeResponse.class);
           assertThat(employeeResponse.getStatus()).isEqualTo("success");
           assertThat(employeeResponse.getMsg()).isEqualTo("Employee Delete successfully");
           System.out.println(employeeResponse);

       } catch (Exception e) {
           throw new RuntimeException(e);
       }
   }

   @Test
   public void shouldEmployeeUpdateFromSystem() throws  JsonProcessingException {
       String barerToken = "Bearer " + token_value;
       // save Employee
       //save employee
       System.out.println("Should employee Update");
       Employee emp = new Employee();
       emp.setSalary(1000);
       emp.setName("Test Update Employee 6");
       emp.setDepartment("DDSK");
       Employee saveEmp = employeeRepository.save(emp);


       try {
           Employee findEmployee = employeeRepository.findById(saveEmp.getId()).orElse(null);
           assert findEmployee != null;
           Employee updateEmp = new Employee();
           updateEmp.setId(findEmployee.getId());
           updateEmp.setDepartment("MMK");
           updateEmp.setName("Test Update Employee 9");
           updateEmp.setSalary(5000);
           String requestBody = objectMapper.writeValueAsString(updateEmp);
           MvcResult result =   mockMvc.perform(authGetRequestBuilder("/api/v1/employees/"+findEmployee.getId(), barerToken, "put" ).content(requestBody)
                   .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
           EmployeeResponse employeeResponse =  objectMapper.readValue(result.getResponse().getContentAsString(), EmployeeResponse.class);
           assertThat(employeeResponse.getStatus()).isEqualTo("success");
           assertThat(employeeResponse.getMsg()).isEqualTo("Employee Update Successfully");
           System.out.println(employeeResponse);


       } catch (Exception e) {
           throw new RuntimeException(e);
       }
   }

    public static MockHttpServletRequestBuilder authGetRequestBuilder(String url, String token, String requestType) {
        switch (requestType) {
            case "post":
                return MockMvcRequestBuilders.post(url).header("Authorization", token);
            case "get":
                return MockMvcRequestBuilders.get(url)
                        .header("Authorization", token);
            case "delete":
                return MockMvcRequestBuilders.delete(url).header("Authorization", token);
            default:
                return MockMvcRequestBuilders.put(url).header("Authorization", token);
        }

    }

}


