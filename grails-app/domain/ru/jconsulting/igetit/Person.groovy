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
    String oAuthProvider
    int followersCount

    static hasMany = [buys: Buy]
    static transients = ['springSecurityService']
    static embedded = ['city']

    static constraints = {
        username blank: false, unique: true
        email nullable: true, email: true, unique: true
        password blank: false
        fullName blank: false
        avatar nullable: true
        city nullable: true
        oAuthProvider nullable: true
    }

    static mapping = {
        password column: '`password`'
        avatar cascade: 'save-update, delete'
        followersCount formula: "(select count(*) from person_follower pf where pf.person_id = id)"
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
