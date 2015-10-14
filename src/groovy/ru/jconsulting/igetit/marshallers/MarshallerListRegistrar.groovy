package ru.jconsulting.igetit.marshallers

import org.springframework.beans.factory.annotation.Autowired

import javax.annotation.PostConstruct

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 05.05.14 21:03
 */
class MarshallerListRegistrar {

    @Autowired
    List<MarshallerRegistrar> marshallerList = []

    @PostConstruct
    void register() {
        marshallerList.each{ it.register() }
    }
}
