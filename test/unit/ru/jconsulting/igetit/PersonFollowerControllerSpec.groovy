package ru.jconsulting.igetit

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(PersonFollowerController)
@Mock([Person, PersonFollower])
class PersonFollowerControllerSpec extends Specification {

    Person user1, user2, user3

    def setup() {
        Person.metaClass.encodePassword { -> }
        user1 = new Person(username: 'user1@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        user2 = new Person(username: 'user2@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        user3 = new Person(username: 'user3@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        PersonFollower.create user1, user2, true
        PersonFollower.create user1, user3, true
        controller.metaClass.getAuthenticatedUser = { -> user3 }
        controller.params.format = 'json'
    }

    void "test get all followers"() {
        given:
        params.personId = user1.id
        when:
        controller.index()
        then:
        response.json*.follower*.id == [user2.id, user3.id]
    }

    void "test add to followers"() {
        given:
        params.personId = user2.id
        when:
        controller.save()
        then:
        response.json.person.id == user2.id && response.json.follower.id == user3.id
        PersonFollower.countByPersonAndFollower(user2, user3) == 1
    }

    void "test add to undefined followers"() {
        given:
        params.personId = 100500
        when:
        controller.save()
        then:
        response.status == 422
        response.json.errors.size() == 1
    }

    void "test stop following"() {
        given:
        params.personId = user1
        when:
        controller.delete()
        then:
        response.status == 204
        PersonFollower.countByPersonAndFollower(user1, user3) == 0
        PersonFollower.countByPerson(user1) == 1
    }

    void "test query without personId parameter"() {
        when:
        controller.index()
        then:
        thrown(IllegalStateException)
    }

    void "test delete without personId parameter"() {
        when:
        controller.delete()
        then:
        thrown(IllegalStateException)
    }
}
