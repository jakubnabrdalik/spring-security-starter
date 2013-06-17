package spring.security.starter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.starter.model.UserAccount;

import javax.annotation.Resource;

@Service("userBootstrap")
public class UserBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private StandardPasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (UserAccount.findAllUserAccounts().size() == 0) {
            UserAccount user = new UserAccount();
            user.setUsername("jakub");
            user.setPassword(passwordEncoder.encode("jakub123"));
            user.setAuthority("ROLE_USER");
            user.persist();
            user = new UserAccount();
            user.setUsername("piotr");
            user.setPassword(passwordEncoder.encode("piotr123"));
            user.setAuthority("ROLE_USER");
            user.persist();
            user = new UserAccount();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setAuthority("ROLE_ADMIN");
            user.persist();
            user.flush();
        }
    }

    public void setPasswordEncoder(StandardPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
