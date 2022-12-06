package com.raiot.raiotprojects.classes;

public class ChoiceClassString {
    String value;
    String label;

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

    @Override
    public String toString() {
        return label;
    }
}
