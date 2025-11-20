package springBootLearn.service;

import lombok.Data;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import springBootLearn.DTO.*;
import springBootLearn.Mapper.EmployeeMapper;
import springBootLearn.model.Employee;
import springBootLearn.model.Role;
import springBootLearn.repository.EmployeeRepository;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public EmployeeDTO getEmployee(final String employeeEmail){
        Employee employee = employeeRepository.findByEmail(employeeEmail).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request")
            );
        return employeeMapper.toDTO(employee);
    }
    public List<EmployeeDTO> getEmployees(){
        return StreamSupport
                .stream(employeeRepository.findAll().spliterator(), false)
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }
    public void deleteEmployee(final Long id){
        employeeRepository.deleteById(id);
    }

    public EmployeeDTO createEmployee(EmployeeCreateDTO employeecreatedto){
        Optional<Employee> employee = employeeRepository.findByEmail(employeecreatedto.getEmail());
        if(employee.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
        }
        employeecreatedto.setPassword(passwordEncoder.encode(employeecreatedto.getPassword()));
        employeecreatedto.setRole(Role.EMPLOYEE);
        employee = Optional.of(employeeMapper.toEntity(employeecreatedto));
        return employeeMapper.toDTO(employeeRepository.save(employee.get()));
    }

    public EmployeeUpdateDTO updateEmployee(EmployeeUpdateDTO employeeupdatedto){
        Employee employee = employeeRepository.findByEmail(employeeupdatedto.getOldEmail()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request")
        );
        employeeMapper.updateEmployee(employeeupdatedto, employee);
        employeeRepository.save(employee);
        return employeeMapper.toUpdateDTO(employee);
    }

    public void updatePassword(EmployeePasswordDTO employeepassworddto){
        Employee employee = employeeRepository.findByEmail(employeepassworddto.getEmail()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request")
        );
        if(!passwordEncoder.matches(employeepassworddto.getOldPassword(), employee.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match");
        }
        employeepassworddto.setNewPassword(passwordEncoder.encode(employeepassworddto.getNewPassword()));
        employeeMapper.updatePassword(employeepassworddto, employee);
        employeeRepository.save(employee);
    }

}

