package ru.jconsulting.igetit

import org.grails.databinding.BindUsing
import ru.jconsulting.likeable.Likeable

class Price implements Likeable {

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
