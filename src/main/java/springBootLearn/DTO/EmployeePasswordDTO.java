package springBootLearn.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeePasswordDTO {
    private String email;

    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 8, max = 20, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
            message = "Au moins une majuscule, une minuscule et un chiffre")
    private String newPassword;
}
