package ru.jconsulting.igetit

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(VerificationController)
@Mock(Person)
class VerificationControllerSpec extends Specification {

    EmailService emailService = Mock(EmailService)

    def setup() {
        Person.metaClass.encodePassword { -> }
        Person user = new Person(username: 'user@ww.ww', email: 'user@ww.ww', fullName: 'FIO', password: 'pwd', confirmToken: '1').save(flush: true, failOnError: true)

        emailService.verify(_ as String) >> {String key -> if (key == '123') user}
        controller.emailService = emailService
    }

    void "test verify existing"() {
        when:
        controller.verify('123')
        then:
        response.redirectedUrl == 'http://mychoiceapp.ru?name=FIO'
    }

    void "test verify nonexistent"() {
        when:
        controller.verify('1')
        then:
        response.status == 404
    }

    void "test verify without key"() {
        when:
        controller.verify(null)
        then:
        response.status == 404
    }
}
