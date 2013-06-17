package spring.security.starter.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.security.starter.model.Todo;

@RequestMapping("/todoes")
@Controller
@RooWebScaffold(path = "todoes", formBackingObject = Todo.class)
public class TodoController {
}
