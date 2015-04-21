package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Price

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 05.05.14 21:45
 */
class PriceMarshaller extends BaseMarshaller implements MarshallerRegistrar {

    @Override
    void register() {
        JSON.registerObjectMarshaller(Price) {Price price ->
            return [
                    id: price.id,
                    value: price.value,
                    currency: price.currency
            ]
        }
    }
}
