import grails.orm.HibernateCriteriaBuilder
import ru.jconsulting.igetit.postgres.TextSearchExpression

class BootStrap {

    def init = { servletContext ->

        HibernateCriteriaBuilder.metaClass.textSearch = { String propertyName, propertyValue ->
            propertyName = calculatePropertyName(propertyName)
            propertyValue = calculatePropertyValue(propertyValue)

            return addToCriteria(new TextSearchExpression(propertyName, propertyValue))
        }
    }
}
