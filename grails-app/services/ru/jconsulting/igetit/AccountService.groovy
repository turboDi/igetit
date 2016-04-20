package ru.jconsulting.igetit

import grails.plugin.springsecurity.rest.token.generation.TokenGenerator
import grails.transaction.Transactional
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import ru.jconsulting.igetit.auth.PersonRole
import ru.jconsulting.igetit.auth.Role

import static grails.plugin.springsecurity.SpringSecurityUtils.authoritiesToRoles

@Transactional
class AccountService {

    def userDetailsService
    def passwordGenerator
    TokenGenerator tokenGenerator

    @Transactional(readOnly = true)
    def tryReAuthenticate(Person person) {
        try {
            def token = generateToken(person)
            if (!authoritiesToRoles(token.authorities).contains('ROLE_OAUTH_USER')) {
                throw new AccessDeniedException("This user is not configured to login via OAuth")
            }
            token
        } catch (UsernameNotFoundException ignored) {
            log.debug('Requested Person not found, reauth failed...')
        }
    }

    def register(Person person) {
        assert person.save()
        PersonRole.create person, getUserRole()
        if (person.oAuthProvider) {
            PersonRole.create person, getOAuthUserRole()
        }
        generateToken(person)
    }

    def update(Person p) {
        assert p.save()
    }

    def resetPassword(Person p) {
        def newPassword = passwordGenerator.generate(9)
        p.password = newPassword
        p.save(validate: false)
        newPassword
    }

    private getUserRole() {
        Role.findByAuthority('ROLE_USER')
    }

    private getOAuthUserRole() {
        Role.findByAuthority('ROLE_OAUTH_USER')
    }

    private generateToken(Person person) {
        UserDetails principal = userDetailsService.loadUserByUsername(person.username)
        if (!principal.enabled) {
            throw new AccessDeniedException("This user was deleted")
        }
        tokenGenerator.generateAccessToken(principal)
    }
}
