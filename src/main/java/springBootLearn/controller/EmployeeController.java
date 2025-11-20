package springBootLearn.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import springBootLearn.DTO.*;
import springBootLearn.security.EmployeePrincipal;
import springBootLearn.security.JwtUtils;
import springBootLearn.service.EmployeeService;

import javax.swing.*;


@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;
    /**
     * Read - Get all employees
     * @return - An Iterable object of Employee fulfilled
     */
    @PreAuthorize("hasAuthority('SCOPE_ROLE_MANAGER')")
    @GetMapping("/employees")
    public List<EmployeeDTO> getEmployees(){
        System.out.println();
        return employeeService.getEmployees();
    }

    /**
     * Read - Get an employee
     * @return - An object of Employee or not found messages
     */
    @GetMapping("/employee/me")
    public EmployeeDTO getEmployee(Authentication authentication){
        return employeeService.getEmployee(authentication.getName());
    }

    /**
     * Deleted an employee
     * @return - Status Code
     */
    @PreAuthorize("hasAuthority('SCOPE_ROLE_MANAGER')")
    @DeleteMapping("/employee")
    public void deleteEmployee(@RequestParam Long id){
        employeeService.deleteEmployee(id);
    }
    /**
     * Update an employee
     * @return - An employee updated
     */
    @PutMapping("/employeeUp/me")
    public EmployeeUpdateDTO updateEmployee(
            Authentication authentication,
            @Valid @RequestBody EmployeeUpdateDTO employeeUpdateDTO){
        employeeUpdateDTO.setToken("");
        employeeUpdateDTO.setOldEmail(authentication.getName());
        employeeUpdateDTO =  employeeService.updateEmployee(employeeUpdateDTO);
        if(!authentication.getName().equals(employeeUpdateDTO.getEmail())){
            UserDetails newEmployeePrincipal =  userDetailsService.loadUserByUsername(employeeUpdateDTO.getEmail());
            String newToken = jwtUtils.generateToken((EmployeePrincipal) newEmployeePrincipal);
            employeeUpdateDTO.setToken(newToken);
        }
        return employeeUpdateDTO;
    }

    /**
     * Create an employee
     * @return - An employee created
     */
    @PostMapping("/employeeCreate")
    public EmployeeDTO createEmployee(@Valid @RequestBody EmployeeCreateDTO employeeCreateDTO){
        return employeeService.createEmployee(employeeCreateDTO);
    }

    /**
     * Change password of an employee
     * @return - Status code
     */
    @PatchMapping("/employeePassword/me")
    public ResponseEntity<Void> updateEmployeePassword(
            Authentication authentication,
            @Valid @RequestBody EmployeePasswordDTO employeePasswordDTO){

            employeePasswordDTO.setEmail(authentication.getName());
            employeeService.updatePassword(employeePasswordDTO);
            return ResponseEntity.ok().build();
    }
    /**
     * Connected an employee
     * @return - Status code
     */
    @PostMapping("/employeeConnect")
    public ResponseEntity<JwtResponseDTO> authenticateEmployee(
            @Valid @RequestBody EmployeeConnectDTO employeeConnectDTO){
            try {
                Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(employeeConnectDTO.getEmail(), employeeConnectDTO.getPassword())
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                EmployeePrincipal employeePrincipal = (EmployeePrincipal) authentication.getPrincipal();
                String jwt = jwtUtils.generateToken(employeePrincipal);

                return ResponseEntity.ok(new JwtResponseDTO(jwt));
            }catch (org.springframework.security.core.AuthenticationException e) {
                return ResponseEntity.badRequest().build();
            }
    }

}
