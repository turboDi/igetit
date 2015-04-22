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
                    fullName: person.fullName,
                    avatar: person.avatar,
                    city: person.city,
                    buysCount: Buy.countByOwner(person),
                    followersCount: person.followersCount,
                    followedCount: PersonFollower.countByFollower(person),
                    iFollow: PersonFollower.countByPersonAndFollower(person, currentPerson()) > 0,
                    lastActivity: person.lastActivity
            ]
        }
    }
}
