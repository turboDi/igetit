import grails.orm.HibernateCriteriaBuilder
import ru.jconsulting.igetit.postgres.TextSearchExpression

class BootStrap {

    def grailsApplication
    def eventProducer
    def personEmailListener
    def emailService
    def customMarshallerRegistrar

    def init = { servletContext ->
        customMarshallerRegistrar.register()
        grailsApplication.mainContext.addApplicationListener eventProducer
        personEmailListener.emailService = emailService

        HibernateCriteriaBuilder.metaClass.textSearch = { String propertyName, propertyValue ->
            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new TextSearchExpression(propertyName, propertyValue))
        }
    }
}
