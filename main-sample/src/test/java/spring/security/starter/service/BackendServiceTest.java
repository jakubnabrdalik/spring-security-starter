package spring.security.starter.service;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.TestingAuthenticationProvider;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.security.starter.model.Todo;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static spring.security.starter.service.BackendService.VERY_IMPORTANT_DATA;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/spring/applicationContext.xml",
        "classpath:META-INF/spring/applicationContext-security.xml"})
public class BackendServiceTest extends AbstractJUnit4SpringContextTests {

    private static final String USERNAME = "user";

    @Resource
    BackendService backendService;

    @Resource
    ProviderManager authenticationManager;

    @Before
    public void setup() {
        authenticationManager.getProviders().add(new TestingAuthenticationProvider());
    }

    @Test
    public void shouldAllowCallingSecureForAdmin() {
        //given
        loginDefaultUserWithRoles("ROLE_ADMIN");

        //when
        String message = backendService.securedForAdmin();

        //then
        assertEquals(VERY_IMPORTANT_DATA, message);
    }

    @Test
    public void shouldAllowCallingPreAuthorizeForAdmin() {
        //given
        loginDefaultUserWithRoles("ROLE_ADMIN");

        //when
        String message = backendService.preAuthorizeForAdmin();

        //then
        assertEquals(VERY_IMPORTANT_DATA, message);
    }

    @Test
    public void shouldAllowCallingPreAuthorizeForAuthenticated() {
        //given
        loginUser();

        //when
        String message = backendService.preAuthorizeForAuthenticated();

        //then
        assertEquals(VERY_IMPORTANT_DATA, message);
    }

    @Test
    public void shouldAllowMarkingTodoAsCompletedForAssignee() {
        //given
        loginDefaultUserWithRoles("ROLE_USER");
        Todo todo = new Todo();
        todo.setAssignee(USERNAME);

        //when
        boolean result = backendService.markAsCompleted(todo);

        //then
        assertTrue(result);
    }

    @Test(expected = AccessDeniedException.class)
    public void shouldDisallowMarkingTodoAsCompletedForNotAssignee() {
        //given
        loginUserWithRoles("piotr", "ROLE_USER");
        Todo todo = new Todo();
        todo.setAssignee("not_authenticated_user");

        //when
        backendService.markAsCompleted(todo);
    }

    @Test(expected = AccessDeniedException.class)
    public void shouldDisallowClosingForNotAssignee() {
        //given
        loginUserWithRoles("piotr", "ROLE_USER");
        Todo todo = new Todo();
        todo.setAssignee("not_authenticated_user");

        //when
        backendService.close(todo);
    }

    @Test(expected = AccessDeniedException.class)
    public void shouldDisallowSecuredForUser() {
        //given
        loginDefaultUserWithRoles("ROLE_USER");

        //when
        backendService.securedForAdmin();
    }

    @Test(expected = AccessDeniedException.class)
    public void shouldDisallowPreAuthorizeForUser() {
        //given
        loginDefaultUserWithRoles("ROLE_USER");

        //when
        backendService.preAuthorizeForAdmin();
    }

    @Test
    public void shouldFilterAssignedTodoes() {
        //given
        saveTodoes("piotr", "piotr", "jakub");
        loginUserWithRoles("piotr", "ROLE_USER");

        //when
        List<Todo> todoes = backendService.listTodoesAssignedToUser();

        //then
        Assert.assertEquals(2, todoes.size());
    }

    private void loginUser() {
        loginDefaultUserWithRoles(new String[]{});
    }

    private void loginDefaultUserWithRoles(String... roles) {
        loginUserWithRoles(USERNAME, roles);
    }

    private void loginUserWithRoles(String username, String... roles) {
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken(
                    new User(username, "pass", Collections.<GrantedAuthority>emptyList()),
                    "DUMMY_CREDENTIALS",
                    AuthorityUtils.createAuthorityList(roles)));
    }

    private void saveTodoes(String... assignees) {
        for (String assignee: assignees) {
            Todo todo = new Todo();
            todo.setAssignee(assignee);
            todo.persist();
        }
    }

}
