package ru.jconsulting.igetit

import org.codehaus.groovy.grails.validation.routines.UrlValidator

class Shop {

    String name
    String sourceId
    City city
    boolean eshop
    boolean deleted

    static embedded = ['city']

    static constraints = {
        sourceId nullable: true
        deleted bindable: false
        name blank: false, validator: { val, obj, errors ->
            obj.sourceId || obj.eshop && UrlValidator.getInstance().isValid(val)
        }
        city nullable: true, validator: { val, obj, errors ->
            !obj.eshop || !city
        }
    }
}
