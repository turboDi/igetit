package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.City

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 15.03.2015
 */
class CityMarshaller implements MarshallerRegistrar {

    @Override
    void register() {
        JSON.registerObjectMarshaller(City) { City city ->
            return [
                    placeId: city.placeId,
                    description: city.description
            ]
        }
    }
}
