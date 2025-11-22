package springBootLearn.service;


import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import springBootLearn.DTO.*;
import springBootLearn.Mapper.EmployeeMapper;
import springBootLearn.model.Employee;
import springBootLearn.model.Role;
import springBootLearn.repository.EmployeeRepository;
import springBootLearn.security.EmployeePrincipal;
import springBootLearn.security.JwtUtils;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    JwtUtils jwtUtils;

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

    public void generateAndSendOtpCode(final String email){
        Instant now = Instant.now();
        Employee employee = employeeRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request")
        );
        SecureRandom random = new SecureRandom();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
        int code = 100000 + random.nextInt(900000);
        String otpCode = String.valueOf(code);

        employee.setOtpCode(otpCode);
        employee.setOtpExpirationDate(LocalDateTime.now().plusMinutes(5));
        employeeRepository.save(employee);

        emailService.sendEmail(
                email,
                "Code de validation",
                "Bonjour "+employee.getFirstName()+" Vous avez assayé de vous connecter sur cipestudio.com le " +
                        LocalDateTime.now().format(formatter)+ ". Voici votre code de validation: "+ otpCode);
    }

    public String validateOtpCode(OtpValidationDTO otpvalidationdto){
        Employee employee = employeeRepository.findByEmail(otpvalidationdto.getEmail()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request")
        );
        if(employee.getOtpCode() == null || employee.getOtpExpirationDate() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
        }
        if(LocalDateTime.now().isAfter(employee.getOtpExpirationDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otp code has expired");
        }
        if(!employee.getOtpCode().equals(otpvalidationdto.getOtpCode())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otp code mismatch");
        }
        employee.setOtpCode(null);
        employee.setOtpExpirationDate(null);
        employeeRepository.save(employee);

        return jwtUtils.generateToken(new EmployeePrincipal(employee));
    }


}

