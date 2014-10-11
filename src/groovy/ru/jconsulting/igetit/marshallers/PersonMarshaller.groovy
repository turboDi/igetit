package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Buy
import ru.jconsulting.igetit.Person
import ru.jconsulting.igetit.PersonFollower

class PersonMarshaller implements MarshallerRegistrar {

    @Override
    void register() {
        JSON.registerObjectMarshaller(Person) { Person person ->
            return [
                    id: person.id,
                    username: person.username,
                    buysCount: Buy.countByOwner(person),
                    followersCount: PersonFollower.countByPerson(person),
                    status: person.status,
                    lastActivity: person.lastActivity
            ]
        }
    }
}
