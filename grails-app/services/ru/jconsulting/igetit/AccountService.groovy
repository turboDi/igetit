package ru.jconsulting.igetit

import com.odobo.grails.plugin.springsecurity.rest.RestAuthenticationToken
import com.odobo.grails.plugin.springsecurity.rest.token.generation.TokenGenerator
import com.odobo.grails.plugin.springsecurity.rest.token.storage.TokenStorageService
import grails.transaction.Transactional
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import ru.jconsulting.igetit.auth.PersonRole
import ru.jconsulting.igetit.auth.Role

@Transactional
class AccountService {

    def userDetailsService
    def mailService
    TokenGenerator tokenGenerator
    TokenStorageService tokenStorageService

    def register(Person p) {
        assert p.save()
        def authority = Role.findByAuthority('ROLE_USER')
        PersonRole.create p, authority
        if (!p.oAuthProvider) {
            mailService.sendMail {
                async true
                to p.email
                subject "Welcome to IGetIt"
                html view: '/account/email', model: [p: p]
            }
        }
        tryReAuthenticate(p.username)
    }

    def verify(String token, String email) {
        Person p = Person.findByConfirmTokenAndEmail(token, email)
        if (p) {
            p.emailConfirmed = true
            p.save()
        }
    }

    def tryReAuthenticate(String username, String oAuthProvider = null) {
        try {
            UserDetails existing = userDetailsService.loadUserByUsername(username)
            if (existing) {
                retrieveToken(existing)
            }
        } catch (UsernameNotFoundException e) {
            if (!oAuthProvider) {
                throw e
            }
        }
    }

    def retrieveToken(UserDetails principal) {
        String tokenValue = tokenGenerator.generateToken()
        tokenStorageService.storeToken(tokenValue, principal)
        new RestAuthenticationToken(principal, principal.password, principal.authorities, tokenValue)
    }
}
