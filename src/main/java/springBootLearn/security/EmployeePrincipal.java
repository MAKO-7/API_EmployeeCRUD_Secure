package springBootLearn.security;

import org.springframework.security.core.GrantedAuthority;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import springBootLearn.model.Employee;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class EmployeePrincipal implements UserDetails {
    private Employee employee;

    @Override
    public String getUsername() {
        return employee.getEmail();
    }
    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    public Long getId() {
        return employee.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + employee.getRole()));

    }

}
