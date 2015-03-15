package ru.jconsulting.igetit

import ru.jconsulting.igetit.auth.PersonRole
import ru.jconsulting.igetit.auth.Role

class Person {

    transient springSecurityService

    String username
    String password
    String email
    Image avatar
    String fullName
    City city

    Date dateCreated
    Date lastActivity = new Date()
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    boolean emailConfirmed
    String confirmToken = ''

    static transients = ['springSecurityService']
    static embedded = ['city']

    static constraints = {
        username blank: false, unique: true
        email nullable: true, email: true, unique: true
        password blank: false
        fullName blank: false
        avatar nullable: true
        city nullable: true
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
