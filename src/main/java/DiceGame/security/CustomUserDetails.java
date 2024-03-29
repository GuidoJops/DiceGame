package DiceGame.security;


import DiceGame.model.domain.Player;
import DiceGame.model.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private Player user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return mapRolesToAuthorities(user.getRoles());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "CustomUserDetails{" +
                "id='" + getId() + '\'' +
                ", userName='" + getUsername() + '\'' +
                ", password=[PROTECTED]'" +
                ", authorities=" + getAuthorities() +
                '}';
    }

    public boolean isAdmin() {
        return getAuthorities().stream().
                anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
    }

    public String getId() {
        return user.getId();
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role->
                new SimpleGrantedAuthority(role.getType().toString())).toList();
    }


}
