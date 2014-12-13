package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.beans.factory.annotation.Autowired
import ru.jconsulting.igetit.Buy

class BuyMarshaller implements MarshallerRegistrar {

    @Autowired
    private SpringSecurityService springSecurityService

    @Override
    void register() {
        JSON.registerObjectMarshaller(Buy) { Buy buy ->
            return [
                    id: buy.id,
                    name: buy.name,
                    brand: buy.brand?.id,
                    category: buy.category?.id,
                    price: buy.price,
                    created: buy.dateCreated,
                    images: buy.images,
                    likes: buy.getTotalLikes(),
                    iLiked: buy.userLiked(springSecurityService.getCurrentUser()),
                    ownerName: buy.owner.username,
                    ownerId: buy.owner.id
            ]
        }
    }
}
