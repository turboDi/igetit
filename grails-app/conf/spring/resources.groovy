import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson.JacksonFactory
import com.google.api.services.drive.DriveScopes
import grails.util.Environment
import ru.jconsulting.igetit.storage.FileSystemStorage
import ru.jconsulting.igetit.storage.GoogleDriveServiceFactory
import ru.jconsulting.igetit.auth.IGetItRestAuthenticationTokenJsonRenderer
import ru.jconsulting.igetit.marshallers.*
import ru.jconsulting.igetit.storage.GoogleDriveStorage

//noinspection GroovyUnusedAssignment
beans = {

    brandMarshaller(BrandMarshaller)
    buyMarshaller(BuyMarshaller)
    categoryMarshaller(CategoryMarshaller)
    commentMarshaller(CommentMarshaller)
    imageMarshaller(ImageMarshaller)
    personMarshaller(PersonMarshaller)
    priceMarshaller(PriceMarshaller)
    eventMarshaller(EventMarshaller)
    cityMarshaller(CityMarshaller)
    personFollowerMarshaller(PersonFollowerMarshaller)
    customMarshallerRegistrar(MarshallerListRegistrar)

    storage(FileSystemStorage)

    restAuthenticationTokenJsonRenderer(IGetItRestAuthenticationTokenJsonRenderer)

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
}
