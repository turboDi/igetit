package ru.jconsulting.igetit

import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.generation.TokenGenerator
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import ru.jconsulting.igetit.auth.PersonRole
import ru.jconsulting.igetit.auth.Role
import spock.lang.Specification

@TestFor(AccountService)
@Mock([Person, PersonRole, Role])
class AccountServiceSpec extends Specification {

    Person user, oauthUser
    Role role, oauthRole

    def setup() {
        Person.metaClass.encodePassword { -> }
        role = new Role(authority: 'ROLE_USER').save(failOnError: true, flush: true)
        user = new Person(username: 'user@ww.ww', fullName: 'FIO', password: 'pwd', confirmToken: '1').save(flush: true, failOnError: true)
        oauthRole = new Role(authority: 'ROLE_OAUTH_USER').save(failOnError: true, flush: true)
        oauthUser = new Person(username: 'user', fullName: 'FIO', password: 'pwd', oAuthProvider: 'test').save(flush: true, failOnError: true)
        PersonRole.create user, role, true
        PersonRole.create oauthUser, oauthRole, true
        def userDetailsService = mockFor(UserDetailsService)
        userDetailsService.demand.loadUserByUsername { String username ->
            Person p = Person.findByUsername(username)
            if (p) return new User(p.username, p.password, p.authorities.collect{ new SimpleGrantedAuthority(it.authority) })
            throw new UsernameNotFoundException('')
        }
        service.userDetailsService = userDetailsService.createMock()
        service.tokenGenerator = [ generateAccessToken: { UserDetails u ->
            new AccessToken(u, u.authorities, UUID.randomUUID().toString())
        } ] as TokenGenerator
        service.passwordGenerator = [ generate: { n -> '123' }]
    }

    void "test reauth oauth"() {
        when:
        def token = service.tryReAuthenticate(new Person(username: 'user'))
        then:
        token.principal.username == 'user'
    }

    void "test reauth regular"() {
        when:
        service.tryReAuthenticate(new Person(username: 'user@ww.ww'))
        then:
        thrown(AccessDeniedException)
    }

    void "test reauth nonexistent"() {
        when:
        def token = service.tryReAuthenticate(new Person(username: 'nonexistent'))
        then:
        !token
    }

    void "test register"() {
        given:
        Person p = new Person(username: 'user2@ww.ww', email: 'user2@ww.ww', fullName: 'FIO', password: 'pwd', confirmToken: '1')
        when:
        def token = service.register(p)
        then:
        token.principal.username == 'user2@ww.ww'
        p.authorities.size() == 1 && p.authorities[0] == role
    }

    void "test register oauth"() {
        given:
        Person p = new Person(username: 'user3', fullName: 'FIO', password: 'pwd', oAuthProvider: 'test')
        when:
        def token = service.register(p)
        then:
        token.principal.username == 'user3'
        p.authorities.size() == 2 && p.authorities.containsAll([role, oauthRole])
    }

    void "test reset password"() {
        when:
        def pwd = service.resetPassword(user)
        then:
        pwd == '123'
        user.password == '123'
    }
}
