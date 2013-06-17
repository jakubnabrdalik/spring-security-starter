package spring.security.starter.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import spring.security.starter.model.UserAccount;
import spring.security.starter.service.UserAccountDetailsService;

@Controller
public class LoginController {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserAccountDetailsService userAccountDetailsService;

    @RequestMapping(method = RequestMethod.POST, value = "/j_spring_security_check")
    public String handleLogin(@RequestParam("j_username") String username, @RequestParam("j_password") String password, ModelMap modelMap) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException | LockedException | BadCredentialsException e) {
            modelMap.put("login_error", "Bad login or password");
            return "redirect:/login";
        }

        UserAccount userAccount = userAccountDetailsService.getUserAccount(username);
        String oneTimePassword = generateRandomOneTimePassword();
        userAccount.setOneTimePassword(oneTimePassword);
        userAccount.persist();
        sendViaDifferentProtocol(oneTimePassword);
        modelMap.put("username", username);
        return "redirect:/OTPlogin";
    }

    private void sendViaDifferentProtocol(String oneTimePassword) {
        logger.debug("One time password sent: " + oneTimePassword);
    }

    private String generateRandomOneTimePassword() {
        return "this depends very much on the protocol you use for sending";
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setUserAccountDetailsService(UserAccountDetailsService userAccountDetailsService) {
        this.userAccountDetailsService = userAccountDetailsService;
    }
}
