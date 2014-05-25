package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Buy
import ru.jconsulting.igetit.Person

class PersonMarshaller implements MarshallerRegistrar {

    @Override
    void register() {
        JSON.registerObjectMarshaller(Person) { Person person ->
            return [
                    id: person.id,
                    username: person.username,
                    buysCount: Buy.countByOwner(person),
                    followedCount: person.followed.size()
            ]
        }
    }
}
