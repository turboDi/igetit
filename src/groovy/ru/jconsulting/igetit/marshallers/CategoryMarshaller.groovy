package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Category

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 05.05.14 21:39
 */
class CategoryMarshaller implements MarshallerRegistrar {

    @Override
    void register() {
        JSON.registerObjectMarshaller(Category) { Category category ->
            return [
                    id: category.id,
                    name: category.name
            ]
        }
    }
}
