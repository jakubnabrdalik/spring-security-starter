package spring.security.starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import spring.security.starter.model.UserAccount;

@Service("loggedUserGetter")
public class LoggedUserGetter {
    @Autowired
    private UserAccountDetailsService userAccountDetailsService;

    @Autowired
    private BackendAuthenticator backendAuthenticator;

    public UserAccount getLoggedUser() {
        return userAccountDetailsService.getUserAccount(getLoggedUserName());
    }

    public UserDetails getLoggedUserDetails() {
        UserDetails loggedUserDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (backendAuthenticator.isAuthenticated(authentication)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                loggedUserDetails = ((UserDetails) principal);
            } else {
                throw new RuntimeException("Expected class of authentication principal is AuthenticationUserDetails. Given: " + principal.getClass());
            }
        }
        return loggedUserDetails;
    }

    public String getLoggedUserName() {
        return getLoggedUserDetails().getUsername();
    }

    public void setUserAccountDetailsService(UserAccountDetailsService userAccountDetailsService) {
        this.userAccountDetailsService = userAccountDetailsService;
    }
}
