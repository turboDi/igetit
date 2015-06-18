import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson.JacksonFactory
import com.google.api.services.drive.DriveScopes
import grails.util.Environment
import org.codehaus.groovy.grails.orm.hibernate.HibernateEventListeners
import ru.jconsulting.igetit.EventProducer
import ru.jconsulting.igetit.IGetItExceptionResolver
import ru.jconsulting.igetit.PersonEmailListener
import ru.jconsulting.igetit.storage.FileSystemStorage
import ru.jconsulting.igetit.storage.GoogleDriveServiceFactory
import ru.jconsulting.igetit.auth.IGetItRestAuthenticationTokenJsonRenderer
import ru.jconsulting.igetit.marshallers.*
import ru.jconsulting.igetit.storage.GoogleDriveStorage

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
    customMarshallerRegistrar(MarshallerListRegistrar)

    storage(FileSystemStorage)

    restAuthenticationTokenJsonRenderer(IGetItRestAuthenticationTokenJsonRenderer)

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
            }

            driveService(
                    driveServiceFactory: "createDrive",
                    httpTransport,
                    jsonFactory,
                    System.getenv("DRIVE_ACCOUNT_ID"),
                    DriveScopes.DRIVE,
                    System.getenv("DRIVE_PRIVATE_KEY")
            )

            storage(GoogleDriveStorage) { bean ->
                bean.autowire = true
            }
        }
    }

    exceptionHandler(IGetItExceptionResolver) {
        exceptionMappings = ['java.lang.Exception': '/error']
    }
}
