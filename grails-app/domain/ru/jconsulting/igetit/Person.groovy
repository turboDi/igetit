package ru.jconsulting.igetit

import org.apache.commons.validator.EmailValidator
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
    String oAuthProvider

    Date dateCreated
    Date lastActivity = new Date()
    boolean deleted
    boolean emailConfirmed
    String confirmToken = UUID.randomUUID()

    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    int followersCount
    String oldPassword

    static hasMany = [buys: Buy]
    static transients = ['springSecurityService', 'oldPassword']
    static embedded = ['city']

    static constraints = {
        username blank: false, unique: true, validator: { val, obj, errors ->
            if (!obj.oAuthProvider && !EmailValidator.getInstance().isValid(val)) {
                Object[] args = ['username', Person.name, val]
                errors.rejectValue('username', 'default.invalid.email.message', args, null)
            }
            errors.hasErrors()
        }
        password nullable: true, changePassword: 'NEW', validator: { val, obj, errors ->
            if (!obj.oAuthProvider && !val) {
                Object[] args = ['password', Person.name]
                errors.rejectValue('password', 'default.blank.message', args, null)
            }
            errors.hasErrors()
        }
        oldPassword changePassword: 'CURRENT'
        email nullable: true, email: true, unique: true
        avatar nullable: true
        fullName blank: false
        city nullable: true
        oAuthProvider nullable: true

        dateCreated bindable: false
        lastActivity bindable: false
        deleted bindable: false
        emailConfirmed bindable: false
        confirmToken bindable: false

        enabled bindable: false
        accountExpired bindable: false
        accountLocked bindable: false
        passwordExpired bindable: false

        followersCount bindable: false
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
        if (!email && !oAuthProvider) {
            email = username
        }
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
        password = oAuthProvider ? 'N/A' : springSecurityService.encodePassword(password)
    }
}
