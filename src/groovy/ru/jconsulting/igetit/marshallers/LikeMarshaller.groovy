package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Person
import ru.jconsulting.igetit.PersonFollower
import ru.jconsulting.likeable.Like

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 29.03.2015
 */
class LikeMarshaller extends BaseMarshaller implements MarshallerRegistrar {
    @Override
    void register() {
        JSON.registerObjectMarshaller(Like) { Like like ->
            Person p = like.liker as Person
            return [
                    liker: marshallPerson(p) << [
                            city: p.city,
                            iFollow: PersonFollower.countByPersonAndFollowerAndDeleted(p, currentPerson(), false) > 0
                    ],
                    dateCreated: like.dateCreated
            ]
        }
    }
}
