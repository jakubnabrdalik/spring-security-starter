// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package spring.security.starter.model;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import spring.security.starter.model.UserAccount;

privileged aspect UserAccount_Roo_Finder {
    
    public static TypedQuery<UserAccount> UserAccount.findUserAccountsByUsernameEquals(String username) {
        if (username == null || username.length() == 0) throw new IllegalArgumentException("The username argument is required");
        EntityManager em = UserAccount.entityManager();
        TypedQuery<UserAccount> q = em.createQuery("SELECT o FROM UserAccount AS o WHERE o.username = :username", UserAccount.class);
        q.setParameter("username", username);
        return q;
    }
    
}
