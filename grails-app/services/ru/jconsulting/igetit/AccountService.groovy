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
    TokenGenerator tokenGenerator

    def register(Person p) {
        assert p.save()
        def authority = Role.findByAuthority('ROLE_USER')
        PersonRole.create p, authority
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
}
