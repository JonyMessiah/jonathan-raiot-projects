package com.raiot.raiotprojects.classes;

public class ChoiceClass {
    Number value;
    String label;

    public ChoiceClass(String label, Number value)
    {
        this.label = label;
        this.value = value;
    }

    public Number getValue() { return this.value; }

    @Override public String toString() {return label;}
}
