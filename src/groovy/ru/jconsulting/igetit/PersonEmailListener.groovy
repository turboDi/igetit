package ru.jconsulting.igetit

import org.hibernate.event.PostInsertEvent
import org.hibernate.event.PostInsertEventListener
import org.hibernate.event.PostUpdateEvent
import org.hibernate.event.PostUpdateEventListener
import org.springframework.beans.factory.annotation.Autowired

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 27.05.2015
 */
class PersonEmailListener implements PostUpdateEventListener, PostInsertEventListener {

    @Autowired
    def emailService

    @Override
    void onPostUpdate(PostUpdateEvent event) {
        processEvent(event, {
            event.dirtyProperties.find {
                event.persister.propertyNames[it] == 'email'
            }
        })
    }

    @Override
    void onPostInsert(PostInsertEvent event) {
        processEvent(event)
    }

    def processEvent(event, condition = { true }) {
        def p = event.entity
        if (p instanceof Person && p.email && condition()) {
            emailService.sendVerification(p)
        }
    }
}
