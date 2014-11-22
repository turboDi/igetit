package ru.jconsulting.igetit

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(AccountController)
@Mock(Person)
class AccountControllerSpec extends Specification {

    Person user

    def setup() {
        Person.metaClass.encodePassword { -> }
        user = new Person(username: 'user', email: 'ww@ww.ww', password: 'pwd', confirmToken: '1').save(flush: true, failOnError: true)
    }

    void "test verify invalid confirm token"() {
        given:
        params.key = '2'
        params.email = 'ww@ww.ww'
        when:
        controller.verify()
        then:
        response.status == 404
        !user.emailConfirmed
    }

    void "test verify invalid email"() {
        given:
        params.key = '1'
        params.email = 'ww@ww1.ww'
        when:
        controller.verify()
        then:
        response.status == 404
        !user.emailConfirmed
    }

    void "test verify without params"() {
        when:
        controller.verify()
        then:
        response.status == 404
        !user.emailConfirmed
    }

    void "test verify"() {
        given:
        params.key = '1'
        params.email = 'ww@ww.ww'
        when:
        controller.verify()
        then:
        response.status == 200
        user.emailConfirmed
    }
}
