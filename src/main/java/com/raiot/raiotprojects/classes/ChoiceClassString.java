package com.raiot.raiotprojects.classes;

public class ChoiceClassString {
    String value;
    String label;

    ChoiceClassUserOnProject choice_class_user_on_project;

    public ChoiceClassString(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void  setValue(String value) {
        this.value = value;
    }

    public void setChoiceClassUserOnProject(ChoiceClassUserOnProject value) { this.choice_class_user_on_project = value; }
    public ChoiceClassUserOnProject getChoiceClassUserOnProject() { return this.choice_class_user_on_project; }
    @Override
    public String toString() {
        return label;
    }
}
