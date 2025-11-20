package springBootLearn.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployeeUpdateDTO {
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @Email
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String oldEmail;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String token;

}
