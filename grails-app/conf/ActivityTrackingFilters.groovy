import groovy.time.TimeCategory
import ru.jconsulting.igetit.Person

class ActivityTrackingFilters {

    def springSecurityService

    def filters = {
        all(controller:'*', action:'*') {
            afterView = { Exception e ->
                use (TimeCategory) {
                    Person p = springSecurityService.getCurrentUser() as Person
                    if (p && p.lastActivity < new Date() - 5.minutes) {
                        Person.withTransaction {
                            p.lastActivity = new Date()
                            p.save()
                        }
                    }
                    false
                }
            }
        }
    }
}
