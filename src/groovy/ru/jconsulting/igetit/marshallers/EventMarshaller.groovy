package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.springframework.beans.factory.annotation.Autowired
import ru.jconsulting.igetit.Buy
import ru.jconsulting.igetit.Comment
import ru.jconsulting.igetit.Event

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 07.03.2015
 */
class EventMarshaller extends BaseMarshaller implements MarshallerRegistrar {

    @Autowired
    LinkGenerator grailsLinkGenerator

    @Override
    void register() {
        JSON.registerObjectMarshaller(Event) { Event event ->
            return [
                    id: event.id,
                    person: marshallPerson(event.initiator),
                    buy: event.buy ? marshallBuy(event.buy) : null,
                    comment: event.comment ? marshallComment(event.comment) : null,
                    type: event.type,
                    dateCreated: event.dateCreated
            ]
        }
    }

    private marshallBuy(Buy buy) {
        [
                image: buy.images?.iterator()?.next(),
                link: grailsLinkGenerator.link(absolute: true, controller: 'buy', action: 'show', params: [id: buy.id])
        ]
    }

    private marshallComment(Comment comment) {
        [
                text: comment.text,
                link: grailsLinkGenerator.link(absolute: true, controller: 'comment', action: 'show', params: [id: comment.id])
        ]
    }
}
