package ru.jconsulting.igetit

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 27.05.2015
 */
class PersonEmailListenerSpec extends NonTransactionalIntegrationSpec {

    def personEmailListener
    def esMock = Mock(EmailService)

    void setup() {
        Person.withNewTransaction {
            new Person(username: 'p1@ww.ww', fullName: 'FIO', password: '123', email: 'p1@ww.ww').save(failOnError: true)
        }
        esMock.sendVerification(_ as Person) >> { Person p -> }
        personEmailListener.emailService = esMock
    }

    void cleanup() {
        Person.findAll()*.delete(flush: true)
    }

    void "test create new person"() {
        when:
        Person.withNewTransaction {
            new Person(username: 'p2@ww.ww', fullName: 'FIO', password: '123', email: 'p2@ww.ww').save(failOnError: true)
        }
        then:
        1 * esMock.sendVerification(_)
    }

    void "test update person email"() {
        when:
        Person.withNewTransaction {
            Person p = Person.findByEmail('p1@ww.ww')
            p.email = 'p1Changed@ww.ww'
            p.save()
        }
        then:
        1 * esMock.sendVerification(_)
    }

    void "test update person name"() {
        when:
        Person.withNewTransaction {
            Person p = Person.findByEmail('p1@ww.ww')
            p.fullName = 'new FIO'
            p.save()
        }
        then:
        0 * esMock.sendVerification(_)
    }
}
