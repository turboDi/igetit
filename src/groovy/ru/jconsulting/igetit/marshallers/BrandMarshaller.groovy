package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Brand

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 05.05.14 21:41
 */
class BrandMarshaller implements MarshallerRegistrar {

    @Override
    void register() {
        JSON.registerObjectMarshaller(Brand) { Brand brand ->
            return [
                    id: brand.id,
                    name: brand.name
            ]
        }
    }
}
