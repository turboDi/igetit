package ru.jconsulting.igetit.marshallers

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 05.05.14 21:03
 */
class MarshallerListRegistrar implements MarshallerRegistrar {

    List<MarshallerRegistrar> marshallerList = []

    @Override
    void register() {
        marshallerList.each{ it.register() }
    }
}
