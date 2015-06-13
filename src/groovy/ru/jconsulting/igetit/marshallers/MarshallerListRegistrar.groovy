package ru.jconsulting.igetit.marshallers

import org.springframework.beans.factory.annotation.Autowired

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 05.05.14 21:03
 */
class MarshallerListRegistrar {

    @Autowired
    List<MarshallerRegistrar> marshallerList = []

    void register() {
        marshallerList.each{ it.register() }
    }
}
