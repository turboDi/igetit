package ru.jconsulting.igetit

import org.apache.commons.lang.builder.HashCodeBuilder

class PersonFollower implements Serializable {

    Person person
    Person follower
    Date dateCreated

    static PersonFollower create(Person person, Person follower, boolean flush = false) {
        new PersonFollower(person: person, follower: follower).save(flush: flush, insert: true)
    }

    static mapping = {
        person unique: 'follower'
        version false
    }

    boolean equals(other) {
        if (!(other instanceof PersonFollower)) {
            return false
        }

        other.person?.id == person?.id &&
                other.follower?.id == follower?.id
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (person) builder.append(person.id)
        if (follower) builder.append(follower.id)
        builder.toHashCode()
    }
}
