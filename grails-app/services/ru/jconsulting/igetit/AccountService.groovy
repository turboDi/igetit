package ru.jconsulting.igetit

import grails.plugin.springsecurity.rest.token.generation.TokenGenerator
import grails.transaction.Transactional
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import ru.jconsulting.igetit.auth.PersonRole
import ru.jconsulting.igetit.auth.Role

@Transactional
class AccountService {

    def userDetailsService
    def passwordEncoder
    def passwordGenerator
    TokenGenerator tokenGenerator

    def register(Person p) {
        assert p.save()
        PersonRole.create p, getUserRole()
        tryReAuthenticate(p.username)
    }

    def tryReAuthenticate(String username, String oAuthProvider = null) {
        try {
            UserDetails principal = userDetailsService.loadUserByUsername(username)
            if (!principal.enabled) {
                throw new AccessDeniedException("This user was deleted")
            }
            tokenGenerator.generateAccessToken(principal)
        } catch (UsernameNotFoundException e) {
            if (!oAuthProvider) {
                throw e
            }
        }
    }

    def getUserRole() {
        Role.findByAuthority('ROLE_USER')
    }

    def resetPassword(Person p) {
        def newPassword = passwordGenerator.generate(9)
        p.password = newPassword
        p.save(validate: false)
        newPassword
    }

    def isPasswordValid(Person p, errors) {
        def valid = true
        if (p.isAttached() && p.isDirty('password')) {
            def oldPassword = p.getPersistentValue('password')
            if (!p.oldPassword || !passwordEncoder.isPasswordValid(oldPassword, p.oldPassword, null)) {
                errors.rejectValue('oldPassword', 'validation.password.old.invalid.message', null, 'Current password is incorrect')
                valid = false
            }
            if (passwordEncoder.isPasswordValid(oldPassword, p.password, null)) {
                errors.rejectValue('password', 'validation.password.new.same.message', null, 'Please choose a different password from your current one')
                valid = false
            }
        }
        valid
    }
}
