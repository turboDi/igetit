import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson.JacksonFactory
import com.google.api.services.drive.DriveScopes
import grails.util.Environment
import org.codehaus.groovy.grails.orm.hibernate.HibernateEventListeners
import org.flywaydb.core.Flyway
import ru.jconsulting.igetit.EventProducer
import ru.jconsulting.igetit.utils.ConstraintRegistrar
import ru.jconsulting.igetit.security.HeaderBearerTokenReader
import ru.jconsulting.igetit.IGetItExceptionResolver
import ru.jconsulting.igetit.PersonEmailListener
import ru.jconsulting.igetit.security.PasswordGenerator
import ru.jconsulting.igetit.storage.FileSystemStorage
import ru.jconsulting.igetit.storage.GoogleDriveServiceFactory
import ru.jconsulting.igetit.marshallers.*
import ru.jconsulting.igetit.storage.GoogleDriveStorage

import static ru.jconsulting.igetit.utils.SystemUtils.env

//noinspection GroovyUnusedAssignment
beans = {

    buyMarshaller(BuyMarshaller)
    categoryMarshaller(CategoryMarshaller)
    commentMarshaller(CommentMarshaller)
    imageMarshaller(ImageMarshaller)
    personMarshaller(PersonMarshaller)
    priceMarshaller(PriceMarshaller)
    eventMarshaller(EventMarshaller)
    cityMarshaller(CityMarshaller)
    personFavoriteMarshaller(PersonFavoriteMarshaller)
    personFollowerMarshaller(PersonFollowerMarshaller)
    likeMarshaller(LikeMarshaller)
    shopMarshaller(ShopMarshaller)
    customMarshallerRegistrar(MarshallerListRegistrar)
    customConstraintRegistrar(ConstraintRegistrar) { bean ->
        bean.initMethod = 'register'
    }

    storage(FileSystemStorage)
    tokenReader(HeaderBearerTokenReader)
    passwordGenerator(PasswordGenerator)

    eventProducer(EventProducer)
    personEmailListener(PersonEmailListener)

    hibernateEventListeners(HibernateEventListeners) {
        listenerMap = [
                'post-commit-update': personEmailListener,
                'post-commit-insert': personEmailListener
        ]
    }

    Environment.executeForCurrentEnvironment {
        production {
            httpTransport(NetHttpTransport)
            jsonFactory(JacksonFactory)
            driveServiceFactory(GoogleDriveServiceFactory) {
                appName = grailsApplication.metadata['app.name']
                accountId = env("DRIVE_ACCOUNT_ID")
                privateKey = env("DRIVE_PRIVATE_KEY")
                privateKeyFilePath = env("DRIVE_PRIVATE_KEY_FILE")
            }

            driveService(
                    driveServiceFactory: "createDrive",
                    httpTransport,
                    jsonFactory,
                    DriveScopes.DRIVE
            )

            storage(GoogleDriveStorage) { bean ->
                bean.autowire = true
            }
        }
    }

    exceptionHandler(IGetItExceptionResolver) {
        exceptionMappings = ['java.lang.Exception': '/error']
    }

    if (Environment.current != Environment.TEST) {
        flyway(Flyway) { bean ->
            bean.initMethod = 'migrate'
            dataSource = ref('dataSource')
            locations = 'migrations'
        }
    }

    def sessionFactoryBeanDef = getBeanDefinition('sessionFactory')
    if (sessionFactoryBeanDef) {
        sessionFactoryBeanDef.dependsOn = ['flyway', 'customConstraintRegistrar']
    }
}
