import grails.orm.HibernateCriteriaBuilder
import ru.jconsulting.igetit.postgres.TextSearchExpression

class BootStrap {

    def emailService
    def personEmailListener

    def init = { servletContext ->

        personEmailListener.emailService = emailService

        HibernateCriteriaBuilder.metaClass.textSearch = { String propertyName, propertyValue ->
            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new TextSearchExpression(propertyName, propertyValue))
        }
    }
}
