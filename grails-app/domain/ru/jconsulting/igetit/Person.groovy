package ru.jconsulting.igetit

import ru.jconsulting.igetit.auth.PersonRole
import ru.jconsulting.igetit.auth.Role

class Person {

    transient springSecurityService

    String username
    String email
    String password
    String status
    Image avatar
    Date dateCreated
    Date lastActivity
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    static transients = ['springSecurityService']

    static constraints = {
        username blank: false, unique: true
        email email: true, blank: false, unique: true
        password blank: false
        status nullable: true
        avatar nullable: true
        lastActivity nullable: true
    }

    static mapping = {
        password column: '`password`'
        avatar cascade: 'save-update, delete'
    }

    Set<Role> getAuthorities() {
        PersonRole.findAllByPerson(this).collect { it.role } as Set
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService.encodePassword(password)
    }
}
