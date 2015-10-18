package ru.jconsulting.igetit

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import ru.jconsulting.igetit.marshallers.PersonFollowerMarshaller
import spock.lang.Specification

@TestFor(PersonFollowedController)
@Mock([Person, PersonFollower])
class PersonFollowedControllerSpec extends Specification {

    Person user1, user2, user3
    PersonFollower pf

    def setup() {
        Person.metaClass.encodePassword { -> }
        user1 = new Person(username: 'user1@ww.ww', fullName: 'FIO1', password: 'pwd').save(flush: true, failOnError: true)
        user2 = new Person(username: 'user2@ww.ww', fullName: 'FIO2', password: 'pwd').save(flush: true, failOnError: true)
        user3 = new Person(username: 'user3@ww.ww', fullName: 'FIO3', password: 'pwd').save(flush: true, failOnError: true)

        PersonFollowerMarshaller.metaClass.currentPerson = { -> user3}
        new PersonFollowerMarshaller().register()

        PersonFollower.create user1, user2, true
        pf = PersonFollower.create user1, user3, true
    }

    void "test get all followed"() {
        given:
        params.personId = user2.id
        when:
        controller.index(10)
        then:
        response.json*.followed.id == [user1.id]
        response.json*.followed.myFollow == [pf.id]
        response.json*.followed.name == [user1.fullName]
    }

    void "test get undefined followed"() {
        when:
        controller.index(10)
        then:
        thrown(IllegalStateException)
    }

    void "test get nonexistent followed"() {
        given:
        params.personId = 100500
        when:
        controller.index(10)
        then:
        response.status == 404
    }
}
