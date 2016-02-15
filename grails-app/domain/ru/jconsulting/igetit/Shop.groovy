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
            if (!obj.sourceId && (!obj.eshop || !UrlValidator.getInstance().isValid(val))) {
                errors.rejectValue('name', 'validation.eshop.url')
            }
        }
        city nullable: true, validator: { val, obj, errors ->
            if (obj.eshop && obj.city) {
                errors.rejectValue('city', 'validation.eshop.city')
            }
        }
    }
}
