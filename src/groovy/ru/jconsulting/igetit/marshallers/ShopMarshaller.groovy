package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Shop

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 1/29/2016
 */
class ShopMarshaller implements MarshallerRegistrar {

    @Override
    void register() {
        JSON.registerObjectMarshaller(Shop) { Shop shop ->
            return [
                    id: shop.id,
                    name: shop.name,
                    city: shop.city,
                    eshop: shop.eshop,
                    deleted: shop.deleted
            ]
        }
    }
}
