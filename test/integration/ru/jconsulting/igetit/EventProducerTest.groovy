package ru.jconsulting.igetit

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 09.03.2015
 */
class EventProducerTest extends GroovyTestCase {

    Person p1, p2
    Buy buy

    @Override
    protected void setUp() throws Exception {
        EventProducer.metaClass.saveNewEvent = { Event e -> e.save() }
    }

    void testAddComment() {
        prepareCommonData()
        new Comment(text: "123", author: p2, buy: buy).save()

        Event event = Event.findByEffector(p1);

        assertEquals 'comment', event.type
        assertEquals '123', event.comment
        assertEquals buy, event.buy
        assertEquals p2, event.initiator
    }

    void testLikeComment() {
        prepareCommonData()
        def c = new Comment(text: "123", author: p2, buy: buy).save()
        c.like(p1)

        Event event = Event.findByEffector(p2);

        assertEquals 'like', event.type
        assertEquals '123', event.comment
        assertEquals buy, event.buy
        assertEquals p1, event.initiator
    }

    void testLikeBuy() {
        prepareCommonData()
        buy.like(p2)

        Event event = Event.findByEffector(p1);

        assertEquals 'like', event.type
        assertEquals buy, event.buy
        assertEquals p2, event.initiator
    }

    void testBegunFollow() {
        prepareCommonData()

        PersonFollower.create(p1, p2)

        Event event = Event.findByEffector(p1);

        assertEquals 'personFollower', event.type
        assertEquals p2, event.initiator
    }

    private void prepareCommonData() {
        p1 = new Person(username: 'p1@ww.ww', fullName: 'FIO', password: '123').save(flush: true, failOnError: true)
        p2 = new Person(username: 'p2@ww.ww', fullName: 'FIO', password: '123').save(flush: true, failOnError: true)
        buy = new Buy(name: 'myBuy', owner: p1).save(flush: true, failOnError: true)
    }
}
