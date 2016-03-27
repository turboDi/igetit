package ru.jconsulting.igetit

class Category {

    String name
    Gender gender

    static constraints = {
    }

    public enum Gender {
        MALE, FEMALE
    }
}
