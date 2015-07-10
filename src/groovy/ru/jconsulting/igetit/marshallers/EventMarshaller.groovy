package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Buy
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
                    buy: event.buy ? marshallBuy(event.buy) : null,
                    comment: event.comment,
                    type: event.type,
                    dateCreated: event.dateCreated
            ]
        }
    }

    private static marshallBuy(Buy buy) {
        [
                id: buy.id,
                image: buy.images?.iterator()?.next()
        ]
    }
}
