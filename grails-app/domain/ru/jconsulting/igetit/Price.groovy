package ru.jconsulting.igetit

import org.grails.rateable.Rateable

class Price implements Rateable {

    BigDecimal value

    Currency currency

    static belongsTo = Buy

    static constraints = {
        value max: new BigDecimal(1000000), scale: 2
    }
}
