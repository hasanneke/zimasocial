package com.zimaberlin.zimasocial.dto;
import com.zimaberlin.zimasocial.entity.Profile;
import com.zimaberlin.zimasocial.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Profile profile;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = profile.getRoles().stream().map(Enum::name).toList();
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return profile.getEmail();
    }
}
