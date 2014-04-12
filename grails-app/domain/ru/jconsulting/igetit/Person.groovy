package ru.jconsulting.igetit

class Person {

    byte[] avatar

    String nickname

    static hasMany = [followed : Person]

    static constraints = {
        avatar nullable: true
    }
}
