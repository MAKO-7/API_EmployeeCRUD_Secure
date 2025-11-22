package springBootLearn.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpValidationDTO {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String otpCode;
}
