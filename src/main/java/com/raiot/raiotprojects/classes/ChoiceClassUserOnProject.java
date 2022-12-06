package com.raiot.raiotprojects.classes;

public class ChoiceClassUserOnProject {
    Number id;
    String name;

    ChoiceClassString role = new ChoiceClassString("Líder", "leader");

    public ChoiceClassUserOnProject(Number id, String name, ChoiceClassString role) {
        this.name = name;
        this.id = id;
        this.role = role;
    }

    public Number getValue() {
        return this.id;
    }

    public ChoiceClassString getRole() { return this.role; }
    public void setRole(ChoiceClassString role) { this.role = role; }

    @Override
    public String toString() {
        return name;
    }
}
