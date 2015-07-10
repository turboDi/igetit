package ru.jconsulting.igetit

import groovy.util.logging.Slf4j
import groovyx.gpars.dataflow.DataflowQueue
import org.grails.datastore.mapping.core.Datastore
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEventListener
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEvent
import ru.jconsulting.likeable.Like

import static groovyx.gpars.dataflow.Dataflow.operator

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 23.02.15 19:47
 */
@Slf4j
class EventProducer extends AbstractPersistenceEventListener {

    final def eventQueue = new DataflowQueue()

    @Autowired
    public EventProducer(Datastore ds) {
        super(ds)
        operator(inputs: [eventQueue], maxForks: 10) { Event event ->
            try {
                Event.withTransaction {
                    log.debug("Saving event $event")
                    event.save()
                }
            } catch (Exception e) {
                log.debug('Failed to produce new event', e)
                //FIXME: sometimes comment persisting occurs after event persisting and causes constraint violation error
                if (event.retryCount++ > 3) {
                    log.error('Failed to produce new event', e)
                } else {
                    Thread.sleep(1000)
                    saveNewEvent(event)
                }
            }
        }
    }

    @Override
    protected void onPersistenceEvent(AbstractPersistenceEvent event) {
        def newEvent = initEvent(event.entityObject)
        if (newEvent && newEvent.effector != newEvent.initiator) {
            log.debug("Sending new event $newEvent to event queue")
            saveNewEvent(newEvent)
        }
    }

    @Override
    boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType.isAssignableFrom(PostInsertEvent)
    }

    def saveNewEvent(Event event) {
        eventQueue << event
    }

    static def initEvent(obj) {
        switch (obj) {
            case Comment:
                return new Event(
                        effector: obj.buy.owner,
                        initiator: obj.author,
                        comment: obj,
                        buy: obj.buy,
                        type: 'comment'
                );
            case Like:
                Person initiator = obj.liker
                def target = obj.target
                boolean isBuy = target instanceof Buy
                return new Event(
                        effector: target.owner,
                        initiator: initiator,
                        buy: isBuy ? target : target.buy,
                        comment: isBuy ? null : target,
                        type: 'like'
                )
            case PersonFollower:
                return new Event(
                        effector: obj.person,
                        initiator: obj.follower,
                        type: 'personFollower'
                )
            default: return null
        }
    }
}
