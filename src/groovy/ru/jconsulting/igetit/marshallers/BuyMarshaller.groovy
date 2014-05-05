package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import grails.util.GrailsNameUtils
import org.grails.rateable.RatingLink
import ru.jconsulting.igetit.Buy

class BuyMarshaller implements MarshallerRegistrar {

    @Override
    void register() {
        JSON.registerObjectMarshaller(Buy) { Buy buy ->
            return [
                    id: buy.id,
                    name: buy.name,
                    brand: buy.brand?.id,
                    category: buy.category?.id,
                    price: buy.price,
                    created: buy.created,
                    description: buy.description,
                    image: buy.image,
                    ownerRating: buy.userRating(buy.owner)[0]?.stars,
                    avgRating: getAvgBuyRating(buy)
            ]
        }
    }

    def getAvgBuyRating(Buy buy) {
        RatingLink.createCriteria().get {
            rating {
                projections { avg 'stars' }
                ne "raterId", buy.ownerId
            }
            eq "ratingRef", buy.id
            eq "type", GrailsNameUtils.getPropertyName(buy.class)
            cache true
        }
    }
}
