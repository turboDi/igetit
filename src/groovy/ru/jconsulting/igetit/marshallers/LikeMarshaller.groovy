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
                    id: like.id,
                    liker: marshallPerson(p) << [
                            city: p.city,
                            myFollow: PersonFollower.findByPersonAndFollowerAndDeleted(p, currentPerson(), false)?.id
                    ],
                    dateCreated: like.dateCreated
            ]
        }
    }
}
