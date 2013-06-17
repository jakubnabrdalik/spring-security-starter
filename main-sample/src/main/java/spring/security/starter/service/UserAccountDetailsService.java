package spring.security.starter.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.security.starter.model.UserAccount;

import javax.persistence.TypedQuery;

@Service("userAccountDetailsService")
public class UserAccountDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount account = getUserAccount(username);
        return new User(account.getUsername(), account.getPassword(),
                AuthorityUtils.createAuthorityList(account.getAuthority()));
    }

    public UserAccount getUserAccount(String username) {
        try {
            TypedQuery<UserAccount> query = UserAccount.findUserAccountsByUsernameEquals(username);
            return query.getSingleResult();
        } catch (EmptyResultDataAccessException ex) {
            throw new UsernameNotFoundException("Could not find user " + username, ex);
        }
    }
}