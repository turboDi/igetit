package ru.jconsulting.igetit

import org.grails.databinding.BindUsing

class Price {

    BigDecimal value

    @BindUsing({
        obj, source -> Currency.getInstance(source['currency'] as String)
    })
    Currency currency

    static belongsTo = Buy

    static constraints = {
        value max: new BigDecimal(1000000), scale: 2
    }
}
