package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import ru.jconsulting.igetit.Event

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 07.03.2015
 */
class EventMarshaller extends BaseMarshaller implements MarshallerRegistrar {

    @Autowired
    MessageSource messageSource

    @Override
    void register() {
        JSON.registerObjectMarshaller(Event) { Event event ->
            return [
                    id: event.id,
                    person: marshallPerson(event.initiator),
                    text: messageSource.getMessage(event.text, [event.args] as Object[], LocaleContextHolder.getLocale()),
                    dateCreated: event.dateCreated
            ]
        }
    }
}
