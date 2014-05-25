package ru.jconsulting.igetit.auth

import org.apache.commons.lang.builder.HashCodeBuilder
import ru.jconsulting.igetit.Person

class PersonRole implements Serializable {

    private static final long serialVersionUID = 1

    Person person
    Role role

    boolean equals(other) {
        if (!(other instanceof PersonRole)) {
            return false
        }

        other.person?.id == person?.id &&
                other.role?.id == role?.id
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (person) builder.append(person.id)
        if (role) builder.append(role.id)
        builder.toHashCode()
    }

    static PersonRole get(long personId, long roleId) {
        PersonRole.where {
            person == Person.load(personId) &&
                    role == Role.load(roleId)
        }.get()
    }

    static PersonRole create(Person person, Role role, boolean flush = false) {
        new PersonRole(person: person, role: role).save(flush: flush, insert: true)
    }

    static boolean remove(Person p, Role r, boolean flush = false) {

        int rowCount = PersonRole.where {
            person == Person.load(p.id) &&
                    role == Role.load(r.id)
        }.deleteAll()

        rowCount > 0
    }

    static void removeAll(Person p) {
        PersonRole.where {
            person == Person.load(p.id)
        }.deleteAll()
    }

    static void removeAll(Role r) {
        PersonRole.where {
            role == Role.load(r.id)
        }.deleteAll()
    }

    static mapping = {
        id composite: ['role', 'person']
        version false
    }
}
