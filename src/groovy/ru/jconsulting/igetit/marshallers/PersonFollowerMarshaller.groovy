package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Person
import ru.jconsulting.igetit.PersonFollower

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 22.03.2015
 */
class PersonFollowerMarshaller extends BaseMarshaller implements MarshallerRegistrar {

    @Override
    void register() {
        JSON.registerObjectMarshaller(PersonFollower) { PersonFollower personFollower ->
            [
                    follower: marshall(personFollower.follower),
                    deleted: personFollower.deleted
            ]
        }
        JSON.createNamedConfig('personFollowed') {
            it.registerObjectMarshaller(PersonFollower) { PersonFollower personFollower ->
                [
                        followed: marshall(personFollower.person),
                        deleted: personFollower.deleted
                ]
            }
        }
    }

    private marshall(Person person) {
        marshallPerson(person) << [iFollow: PersonFollower.countByPersonAndFollower(person, currentPerson()) > 0]
    }
}
