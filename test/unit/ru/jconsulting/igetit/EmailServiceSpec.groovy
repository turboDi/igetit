package ru.jconsulting.igetit

import grails.plugin.mail.MailService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.web.GroovyPageUnitTestMixin
import spock.lang.Specification

@TestFor(EmailService)
@Mock([Person, MailService])
@TestMixin(GroovyPageUnitTestMixin)
class EmailServiceSpec extends Specification {

    Person user
    String confirmToken

    def setup() {
        Person.metaClass.encodePassword { -> }
        user = new Person(username: 'user@ww.ww', email: 'user@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        confirmToken = user.confirmToken
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
        String key = service.encodeKey(email: 'user@ww1.ww', confirmToken: confirmToken)
        Person p = service.verify(key)
        then:
        !p && !user.emailConfirmed
    }

    void "test verify"() {
        when:
        String key = service.encodeKey(email: 'user@ww.ww', confirmToken: confirmToken)
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

    void "test verify mail contents"() {
        when:
        String key = service.encodeKey(user)
        def content = render(view: '/account/verify', model: [p: user, key: key, siteCfg: [url: 'site', email: 'info']])
        then:
        content.contains key
        content.contains 'site'
        content.contains 'info'
    }
}
