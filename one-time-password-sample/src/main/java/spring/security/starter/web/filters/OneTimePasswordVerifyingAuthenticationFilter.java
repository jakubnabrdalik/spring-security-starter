package spring.security.starter.web.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import spring.security.starter.model.UserAccount;
import spring.security.starter.service.UserAccountDetailsService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OneTimePasswordVerifyingAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String ONE_TIME_PASSWORD_VERIFICATION_URL = "/j_spring_security_one_time_password_check";
    private String oneTimePasswordRequestKey = "oneTimePassword";
    private String usernameRequestKey = "username";

    @Autowired
    private UserAccountDetailsService userAccountDetailsService;

    public OneTimePasswordVerifyingAuthenticationFilter() {
        super(ONE_TIME_PASSWORD_VERIFICATION_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String username = request.getParameter(usernameRequestKey);
        String receivedOneTimePassword = request.getParameter(oneTimePasswordRequestKey);
        UserAccount userAccount = userAccountDetailsService.getUserAccount(username);
        if(userAccount == null) {
            throw new UsernameNotFoundException("User with username " + username + " not found");
        }

        String expectedOneTimePassword = userAccount.getOneTimePassword();
        if(expectedOneTimePassword == null || expectedOneTimePassword.isEmpty() || !expectedOneTimePassword.equals(receivedOneTimePassword)) {
            throw new BadCredentialsException("One Time Password did not match");
        }

        PreAuthenticatedAuthenticationToken authenticationToken = authorizeUser(username);
        userAccountDetailsService.clearOneTimePasswordFor(username);
        return authenticationToken;
    }

   private PreAuthenticatedAuthenticationToken authorizeUser(String username) {
        UserDetails userDetails = userAccountDetailsService.loadUserByUsername(username);
        PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationToken.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return authenticationToken;
    }

    public void setUserAccountDetailsService(UserAccountDetailsService userAccountDetailsService) {
        this.userAccountDetailsService = userAccountDetailsService;
    }
}
