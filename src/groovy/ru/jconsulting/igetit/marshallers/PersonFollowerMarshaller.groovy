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
            Person follower = personFollower.follower
            return marshallPerson(follower) <<
                    [iFollow: PersonFollower.countByPersonAndFollower(follower, currentPerson()) > 0]
        }
    }
}
