package ru.jconsulting.igetit

import grails.plugin.mail.MailService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(EmailService)
@Mock([Person, MailService])
class EmailServiceSpec extends Specification {

    Person user

    def setup() {
        Person.metaClass.encodePassword { -> }
        user = new Person(username: 'user@ww.ww', email: 'user@ww.ww', fullName: 'FIO', password: 'pwd', confirmToken: '1').save(flush: true, failOnError: true)
    }

    void "test key encoding/decoding"() {
        when:
        String encoded = service.encodeKey(email: 'test', confirmToken: '123')
        def decoded = service.decodeKey(encoded)
        then:
        'test' == decoded.email
        '123' == decoded.confirmToken
    }

    void "test verify invalid confirm token"() {
        when:
        String key = service.encodeKey(email: 'user@ww.ww', confirmToken: '2')
        Person p = service.verify(key)
        then:
        !p && !user.emailConfirmed
    }

    void "test verify invalid email"() {
        when:
        String key = service.encodeKey(email: 'user@ww1.ww', confirmToken: '1')
        Person p = service.verify(key)
        then:
        !p && !user.emailConfirmed
    }

    void "test verify"() {
        when:
        String key = service.encodeKey(email: 'user@ww.ww', confirmToken: '1')
        Person p = service.verify(key)
        then:
        p && user.emailConfirmed
    }

    void "test malformed key verify"() {
        when:
        Person p = service.verify('123456')
        then:
        !p && !user.emailConfirmed
    }
}
