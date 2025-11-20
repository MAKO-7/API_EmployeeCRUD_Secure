package springBootLearn.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import springBootLearn.model.Employee;

import java.util.Optional;

@Repository
 public interface EmployeeRepository extends CrudRepository<Employee, Long> {
     Optional<Employee> findByEmail(@NotBlank @Email String email);
 }
