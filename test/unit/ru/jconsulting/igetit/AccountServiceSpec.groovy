package ru.jconsulting.igetit

import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.generation.TokenGenerator
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
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

    Person user
    Role role

    def setup() {
        Person.metaClass.encodePassword { -> }
        role = new Role(authority: 'ROLE_USER').save(failOnError: true, flush: true)
        user = new Person(username: 'user@ww.ww', email: 'user@ww.ww', fullName: 'FIO', password: 'pwd', confirmToken: '1').save(flush: true, failOnError: true)
        def userDetailsService = mockFor(UserDetailsService)
        userDetailsService.demand.loadUserByUsername { String username ->
            Person p = Person.findByUsername(username)
            if (p) return new User(p.username, p.password, Collections.emptyList())
            throw new UsernameNotFoundException('')
        }
        service.userDetailsService = userDetailsService.createMock()
        service.tokenGenerator = [ generateAccessToken: { UserDetails u ->
            new AccessToken(u, u.authorities, UUID.randomUUID().toString())
        } ] as TokenGenerator
    }

    void "test reauth existent"() {
        when:
        def token = service.tryReAuthenticate(user.username)
        then:
        token.principal.username == user.username
    }

    void "test reauth nonexistent oauth"() {
        when:
        def token = service.tryReAuthenticate('123', 'werwer')
        then:
        token == null
    }

    void "test reauth nonexistent"() {
        when:
        service.tryReAuthenticate('123')
        then:
        thrown(UsernameNotFoundException)
    }

    void "test register"() {
        given:
        Person p = new Person(username: 'user2@ww.ww', email: 'user2@ww.ww', fullName: 'FIO', password: 'pwd', confirmToken: '1',)
        when:
        def token = service.register(p)
        then:
        token.principal.username == 'user2@ww.ww'
        p.authorities.size() == 1 && p.authorities[0] == role
    }
}
