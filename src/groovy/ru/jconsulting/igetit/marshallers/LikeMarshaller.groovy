package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
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
            return [
                    liker: marshallPerson(like.liker)
            ]
        }
    }
}
