package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Buy
import ru.jconsulting.igetit.Comment

class BuyMarshaller extends BaseMarshaller implements MarshallerRegistrar {

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
                    iLiked: buy.userLiked(currentPerson()),
                    commentsCount: Comment.countByBuy(buy),
                    ownerName: marshallPerson(buy.owner)
            ]
        }
    }
}
