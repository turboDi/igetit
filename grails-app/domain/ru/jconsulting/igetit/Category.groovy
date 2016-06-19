package ru.jconsulting.igetit

class Category {

    String name
    Gender gender

    static constraints = {
    }

    public static enum Gender {
        MALE, FEMALE
    }
}
