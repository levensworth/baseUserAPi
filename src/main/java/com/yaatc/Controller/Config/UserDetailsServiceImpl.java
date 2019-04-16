package com.yaatc.Controller.Config;

import com.yaatc.Entity.User;
import com.yaatc.Interface.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

@Component
class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {

        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;


        final User user = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user by the email " + email));

        final Collection<GrantedAuthority> authorities = new HashSet<>();
        String role = user.getRole();
        authorities.add(new SimpleGrantedAuthority(role));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isConfirm(),
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                authorities);
    }

}

