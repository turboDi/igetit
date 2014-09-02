package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.beans.factory.annotation.Autowired
import ru.jconsulting.igetit.Price

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 05.05.14 21:45
 */
class PriceMarshaller implements MarshallerRegistrar {

    @Autowired
    private SpringSecurityService springSecurityService

    @Override
    void register() {
        JSON.registerObjectMarshaller(Price) {Price price ->
            return [
                    id: price.id,
                    value: price.value,
                    currency: price.currency,
                    likes: price.getTotalLikes(),
                    iLiked: price.userLiked(springSecurityService.getCurrentUser())
            ]
        }
    }
}
