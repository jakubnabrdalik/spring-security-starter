// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package spring.security.starter.web;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import spring.security.starter.model.Todo;
import spring.security.starter.web.ApplicationConversionServiceFactoryBean;

privileged aspect ApplicationConversionServiceFactoryBean_Roo_ConversionService {
    
    declare @type: ApplicationConversionServiceFactoryBean: @Configurable;
    
    public Converter<Todo, String> ApplicationConversionServiceFactoryBean.getTodoToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<spring.security.starter.model.Todo, java.lang.String>() {
            public String convert(Todo todo) {
                return new StringBuilder().append(todo.getTitle()).append(' ').append(todo.getAssignee()).toString();
            }
        };
    }
    
    public Converter<Long, Todo> ApplicationConversionServiceFactoryBean.getIdToTodoConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, spring.security.starter.model.Todo>() {
            public spring.security.starter.model.Todo convert(java.lang.Long id) {
                return Todo.findTodo(id);
            }
        };
    }
    
    public Converter<String, Todo> ApplicationConversionServiceFactoryBean.getStringToTodoConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, spring.security.starter.model.Todo>() {
            public spring.security.starter.model.Todo convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Todo.class);
            }
        };
    }
    
    public void ApplicationConversionServiceFactoryBean.installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getTodoToStringConverter());
        registry.addConverter(getIdToTodoConverter());
        registry.addConverter(getStringToTodoConverter());
    }
    
    public void ApplicationConversionServiceFactoryBean.afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
    
}
