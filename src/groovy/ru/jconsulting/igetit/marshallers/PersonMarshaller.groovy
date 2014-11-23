package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.beans.factory.annotation.Autowired
import ru.jconsulting.igetit.Buy
import ru.jconsulting.igetit.Person
import ru.jconsulting.igetit.PersonFollower

class PersonMarshaller implements MarshallerRegistrar {

    @Autowired
    private SpringSecurityService springSecurityService

    @Override
    void register() {
        JSON.registerObjectMarshaller(Person) { Person person ->
            Person current = springSecurityService.getCurrentUser() as Person
            return [
                    id: person.id,
                    username: person.username,
                    avatar: person.avatar,
                    buysCount: Buy.countByOwner(person),
                    followersCount: PersonFollower.countByPerson(person),
                    followedCount: PersonFollower.countByFollower(person),
                    iFollow: PersonFollower.countByPersonAndFollower(person, current) > 0,
                    status: person.status,
                    lastActivity: person.lastActivity
            ]
        }
    }
}
