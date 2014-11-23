package ru.jconsulting.igetit

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(PersonController)
@Mock([Person, PersonFollower])
class PersonControllerSpec extends Specification {

    Person user1, user2

    def setup() {
        Person.metaClass.encodePassword { -> }
        user1 = new Person(username: 'user1', email: 'ww@ww.ww', password: 'pwd').save(flush: true, failOnError: true)
        user2 = new Person(username: 'user2', email: 'ww1@ww.ww', password: 'pwd').save(flush: true, failOnError: true)

        def springSecurityServiceMock = mockFor(SpringSecurityService)
        springSecurityServiceMock.demand.getCurrentUser { -> user1 }
        controller.springSecurityService = springSecurityServiceMock.createMock()
    }

    void "test follow nonexistent user"() {
        given:
        params.id = 10
        when:
        controller.follow()
        then:
        response.status == 404
        PersonFollower.count() == 0
    }

    void "test self follow"() {
        given:
        params.id = user1.id
        when:
        controller.follow()
        then:
        thrown(IllegalArgumentException)
    }

    void "test follow"() {
        given:
        params.id = user2.id
        when:
        controller.follow()
        then:
        response.contentAsString == '1'
        PersonFollower.countByPerson(user2) == 1
        PersonFollower.countByFollower(user1) == 1
    }

    void "test stop following"() {
        given:
        params.id = user2.id
        PersonFollower.create(user2, user1, true)
        when:
        controller.follow()
        then:
        response.contentAsString == '0'
        PersonFollower.countByPerson(user2) == 0
        PersonFollower.countByFollower(user1) == 0
    }
}
