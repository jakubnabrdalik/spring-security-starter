// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package spring.security.starter.model;

import spring.security.starter.model.Todo;

privileged aspect Todo_Roo_JavaBean {
    
    public String Todo.getTitle() {
        return this.title;
    }
    
    public void Todo.setTitle(String title) {
        this.title = title;
    }
    
    public Boolean Todo.getCompleted() {
        return this.completed;
    }
    
    public void Todo.setCompleted(Boolean completed) {
        this.completed = completed;
    }
    
    public String Todo.getAssignee() {
        return this.assignee;
    }
    
    public void Todo.setAssignee(String assignee) {
        this.assignee = assignee;
    }
    
}
