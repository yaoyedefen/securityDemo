package example.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for the hello resource.
 *
 * @author Josh Cummings
 */
@RestController
public class HelloController {

    @GetMapping("/")
    //@EnableMethodSecurity
    //@PreAuthorize("hasAuthority('ROLE_USER')")
    //@PreAuthorize("hasRole('ROLE_USER')")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")

    //@EnableMethodSecurity(jsr250Enabled = true, prePostEnabled = false)
    //@RolesAllowed({"USER"})
    //@RolesAllowed({"ADMIN"})
    public String hello(Authentication authentication) {
        return "Hello, " + authentication.getName() + "!";
    }

}
