package ru.jconsulting.igetit

import ru.jconsulting.igetit.auth.PersonRole
import ru.jconsulting.igetit.auth.Role

class Person {

    transient springSecurityService
    transient accountService

    String username
    String password
    String oldPassword
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
    String confirmToken = UUID.randomUUID()
    String oAuthProvider
    int followersCount

    boolean deleted

    static hasMany = [buys: Buy]
    static transients = ['springSecurityService', 'accountService', 'oldPassword']
    static embedded = ['city']

    static constraints = {
        username blank: false, unique: true
        email nullable: true, email: true, unique: true
        password blank: false, validator: { val, obj, errors ->
            obj.accountService.isPasswordValid(obj, errors)
        }
        oldPassword bindable: true
        fullName blank: false
        avatar nullable: true
        city nullable: true
        oAuthProvider nullable: true
        lastActivity bindable: false
        enabled bindable: false
        accountExpired bindable: false
        accountLocked bindable: false
        passwordExpired bindable: false
        emailConfirmed bindable: false
        deleted bindable: false
    }

    static mapping = {
        password column: '`password`'
        avatar cascade: 'save-update, delete'
        followersCount formula: "(select count(*) from person_follower pf where pf.person_id = id and not pf.deleted)"
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
        if (isDirty('email')) {
            emailConfirmed = false
        }
    }

    protected void encodePassword() {
        password = springSecurityService.encodePassword(password)
    }
}
