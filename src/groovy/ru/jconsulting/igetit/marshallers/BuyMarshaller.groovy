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
                    category: buy.category,
                    owner: marshallPerson(buy.owner),
                    price: buy.price,
                    dateCreated: buy.dateCreated,
                    images: buy.images,
                    commentsCount: Comment.countByBuy(buy),
                    likesCount: buy.getTotalLikes(),
                    iLiked: buy.userLiked(currentPerson())
            ]
        }
    }
}
