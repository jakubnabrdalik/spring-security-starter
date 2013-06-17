package spring.security.starter.service;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import spring.security.starter.model.Todo;

import java.util.List;

@Service("backendService")
public class BackendService {

    protected static String VERY_IMPORTANT_DATA = "Very important data";

    @Secured("ROLE_ADMIN")
    public String securedForAdmin() {
        return VERY_IMPORTANT_DATA;
    }

    @PreAuthorize("isAuthenticated()")
    public String preAuthorizeForAuthenticated() {
        return VERY_IMPORTANT_DATA;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String preAuthorizeForAdmin() {
        return VERY_IMPORTANT_DATA;
    }

    @PostAuthorize("hasRole('ROLE_ADMIN') && isFullyAuthenticated()")
    public String postAuthorizeForAdmin() {
        return VERY_IMPORTANT_DATA;
    }

    @PostFilter("filterObject.assignee == authentication.name")
    public List<Todo> listTodoesAssignedToUser() {
        return Todo.findAllTodoes();
    }

    @PreAuthorize("#todo.assignee == authentication.name")
    public boolean markAsCompleted(Todo todo) {
        todo.setCompleted(true);
        todo.persist();
        return true;
    }

    @PreAuthorize("@permissionChecker.isPermitedToCloseTodo(#todo, authentication.name)")
    public void close(Todo todo) {
        todo.setCompleted(true);
        todo.persist();
    }

}
