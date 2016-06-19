package ru.jconsulting.igetit.marshallers

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.converters.configuration.ConvertersConfigurationInitializer
import org.springframework.beans.factory.annotation.Autowired

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 05.05.14 21:03
 */
class MarshallerListInitializer extends ConvertersConfigurationInitializer {

    @Autowired
    List<MarshallerRegistrar> marshallerList = []

    @Override
    public void initialize(GrailsApplication application) {
        super.initialize application
        marshallerList.each{ it.register() }
    }
}
