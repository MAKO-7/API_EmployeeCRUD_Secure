package springBootLearn.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class EmployeeConnectDTO {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
