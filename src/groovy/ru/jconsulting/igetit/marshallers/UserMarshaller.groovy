package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Buy
import ru.jconsulting.igetit.auth.User

class UserMarshaller implements MarshallerRegistrar {

    @Override
    void register() {
        JSON.registerObjectMarshaller(User) { User user ->
            return [
                    id: user.id,
                    username: user.username,
                    buysCount: Buy.countByOwner(user),
                    followedCount: user.followed.size()
            ]
        }
    }
}
