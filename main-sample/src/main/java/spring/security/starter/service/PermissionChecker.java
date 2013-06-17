package spring.security.starter.service;

import org.springframework.stereotype.Service;
import spring.security.starter.model.Todo;

@Service("permissionChecker")
public class PermissionChecker {

    public boolean isPermitedToCloseTodo(Todo todo, String name) {
        return todo.getAssignee().equals(name);
    }

}
