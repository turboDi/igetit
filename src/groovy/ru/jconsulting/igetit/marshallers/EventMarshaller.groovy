package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Event

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 07.03.2015
 */
class EventMarshaller extends BaseMarshaller implements MarshallerRegistrar {

    @Override
    void register() {
        JSON.registerObjectMarshaller(Event) { Event event ->
            return [
                    id: event.id,
                    person: marshallPerson(event.initiator),
                    text: event.text,
                    dateCreated: event.dateCreated
            ]
        }
    }
}
