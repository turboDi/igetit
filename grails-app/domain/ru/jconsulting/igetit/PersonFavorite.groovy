package ru.jconsulting.igetit

class PersonFavorite {

    static belongsTo = [person: Person, buy: Buy]

    static constraints = {
        person unique: 'buy'
    }

    static PersonFavorite create(Person person, Buy buy, boolean flush = false) {
        new PersonFavorite(person: person, buy: buy).save(flush: flush, insert: true)
    }
}
