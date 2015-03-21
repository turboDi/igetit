package ru.jconsulting.igetit

import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(AccountController)
@Mock(Person)
class AccountControllerSpec extends Specification {

    Person user
    AccountService accountService = Mock(AccountService)

    def setup() {
        Person.metaClass.encodePassword { -> }
        user = new Person(username: 'user@ww.ww', email: 'user@ww.ww', fullName: 'FIO', password: 'pwd', confirmToken: '1').save(flush: true, failOnError: true)

        accountService.register(_ as Person) >> {Person u -> [username: u.username] }
        accountService.tryReAuthenticate(_ as String) >> { u -> [username: u] }
        controller.accountService = accountService

        controller.restAuthenticationTokenJsonRenderer = [generateJson : {t ->
            (t as JSON).toString()
        }]
    }

    void "test register"() {
        given:
        params.username = 'qwe@ww.ww'
        params.fullName = '123'
        params.password = '123'
        when:
        controller.register(null)
        then:
        params.confirmToken != null
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
        controller.register(null)
        then:
        response.status == 422
        response.json.errors.size() == 1
        0 * accountService.tryReAuthenticate(_,_)
    }

    void "test register with oauth"() {
        given:
        params.username = 'qqq'
        params.fullName = '123'
        when:
        controller.register('test')
        then:
        params.password == 'N/A'
        response.status == 200
        response.json.username == 'qqq'
        1 * accountService.tryReAuthenticate(_,_)
    }
}
