package springBootLearn.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import springBootLearn.DTO.EmployeeCreateDTO;
import springBootLearn.DTO.EmployeeDTO;
import springBootLearn.DTO.EmployeePasswordDTO;
import springBootLearn.DTO.EmployeeUpdateDTO;
import springBootLearn.model.Employee;


@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeDTO toDTO(Employee employee);
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "oldEmail", constant = "")
    EmployeeUpdateDTO toUpdateDTO(Employee employee);
    Employee toEntity(EmployeeCreateDTO employeeCreateDTO);
    @Mapping(target = "id", ignore = true)
    void updateEmployee(EmployeeUpdateDTO employeeUpdateDTO, @MappingTarget Employee employee);
    @Mapping(source = "newPassword", target = "password")
    void updatePassword(EmployeePasswordDTO employeePasswordDTO, @MappingTarget Employee employee);

}
