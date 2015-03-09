package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Buy
import ru.jconsulting.igetit.Person
import ru.jconsulting.igetit.PersonFollower

class PersonMarshaller extends BaseMarshaller implements MarshallerRegistrar {

    @Override
    void register() {
        JSON.registerObjectMarshaller(Person) { Person person ->
            return [
                    id: person.id,
                    username: person.username,
                    avatar: person.avatar,
                    buysCount: Buy.countByOwner(person),
                    followersCount: PersonFollower.countByPerson(person),
                    followedCount: PersonFollower.countByFollower(person),
                    iFollow: PersonFollower.countByPersonAndFollower(person, currentPerson()) > 0,
                    status: person.status,
                    lastActivity: person.lastActivity
            ]
        }
    }
}
