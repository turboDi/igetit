package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.PersonFavorite

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 09.05.2015
 */
class PersonFavoriteMarshaller extends BaseMarshaller implements MarshallerRegistrar {

    @Override
    void register() {
        JSON.registerObjectMarshaller(PersonFavorite) { PersonFavorite personFavorite ->
            [
                    id: personFavorite.id,
                    buy: personFavorite.buy
            ]
        }
    }
}
