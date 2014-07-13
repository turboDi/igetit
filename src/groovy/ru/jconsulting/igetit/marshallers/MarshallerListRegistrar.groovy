package ru.jconsulting.igetit.marshallers

import org.springframework.beans.factory.annotation.Autowired

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 05.05.14 21:03
 */
class MarshallerListRegistrar {

    @Autowired
    List<MarshallerRegistrar> marshallerList = []

    void register() {
        marshallerList.each{ it.register() }
    }
}
