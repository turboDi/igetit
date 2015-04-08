package ru.jconsulting.igetit

import groovyx.gpars.dataflow.DataflowQueue
import org.grails.datastore.mapping.core.Datastore
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEventListener
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import org.springframework.context.ApplicationEvent
import ru.jconsulting.likeable.Like

import static groovyx.gpars.dataflow.Dataflow.operator

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 23.02.15 19:47
 */
class IGetItPersistenceEventListener extends AbstractPersistenceEventListener {

    final def eventQueue = new DataflowQueue()

    public IGetItPersistenceEventListener(Datastore ds) {
        super(ds)
        operator(inputs: [eventQueue], maxForks: 10) { event ->
            Event.withTransaction {
                event.save()
            }
        }
    }

    @Override
    protected void onPersistenceEvent(AbstractPersistenceEvent event) {
        def newEvent = initEvent(event.entityObject)
        if (newEvent) {
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
                        text: "$obj.author.fullName left comment on your buy",
                        refId: obj.id,
                        type: 'comment'
                );
            case Like:
                Person initiator = obj.liker
                def target = obj.target
                return new Event(
                        effector: (Person) target.hasProperty('owner')?.getProperty(target) ?: target.author,
                        initiator: initiator,
                        text: "$initiator.fullName liked your $obj.type",
                        refId: obj.likeRef,
                        type: obj.type
                )
            case PersonFollower:
                return new Event(
                        effector: obj.person,
                        initiator: obj.follower,
                        text: "$obj.follower.fullName begun to follow you",
                        refId: obj.follower.id,
                        type: 'person'
                )
            default: return null
        }
    }
}
