package ru.jconsulting.igetit

import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.security.access.AccessDeniedException
import spock.lang.Specification

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 17.05.2015
 */
@TestFor(PersonController)
@Mock(Person)
class PersonControllerSpec extends Specification {

    Person user1, user2
    AccountService accountService = Mock(AccountService)

    def setup() {
        Person.metaClass.encodePassword { -> }
        user1 = new Person(username: 'user1@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        user2 = new Person(username: 'user2@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)

        accountService.register(_ as Person) >> {Person u -> [username: u.username] }
        accountService.tryReAuthenticate(_ as String) >> { u -> [username: u] }
        controller.accountService = accountService

        controller.accessTokenJsonRenderer = [generateJson : {t ->
            (t as JSON).toString()
        }]

        controller.metaClass.getAuthenticatedUser = { -> user1 }
        controller.params.format = 'json'
    }

    void "test self update"() {
        given:
        params.id = user1.id
        params.fullName = 'FIO changed'
        params.username = 'newuser1@ww.ww'
        when:
        controller.update()
        then:
        response.status == 200
        response.json.id == user1.id
        response.json.fullName == 'FIO changed'
        response.json.username == 'user1@ww.ww'
    }

    void "test delete another person"() {
        given:
        params.id = user2.id
        when:
        controller.delete()
        then:
        thrown(AccessDeniedException)
    }

    void "test update another person"() {
        given:
        params.id = user2.id
        params.fullName = 'FIO changed'
        when:
        controller.update()
        then:
        thrown(AccessDeniedException)
    }

    void "test register"() {
        given:
        params.username = 'qwe@ww.ww'
        params.fullName = '123'
        params.password = '123'
        when:
        controller.save()
        then:
        params.email == 'qwe@ww.ww'
        response.status == 200
        response.json.username == 'qwe@ww.ww'
        0 * accountService.tryReAuthenticate(_,_)
    }

    void "test register with invalid params"() {
        given:
        params.fullName = '123'
        params.password = '123'
        when:
        controller.save()
        then:
        response.status == 422
        response.json.errors.size() == 1
        0 * accountService.tryReAuthenticate(_,_)
    }

    void "test register with oauth"() {
        given:
        params.username = 'qqq'
        params.fullName = '123'
        params.oAuthProvider = 'test'
        when:
        controller.save()
        then:
        params.password == 'N/A'
        response.status == 200
        response.json.username == 'qqq'
        1 * accountService.tryReAuthenticate(_,_)
    }
}
