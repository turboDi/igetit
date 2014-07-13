import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson.JacksonFactory
import com.google.api.services.drive.DriveScopes
import grails.util.Environment
import ru.jconsulting.igetit.FileSystemService
import ru.jconsulting.igetit.GoogleDriveService
import ru.jconsulting.igetit.GoogleDriveServiceFactory
import ru.jconsulting.igetit.marshallers.*

beans = {

    brandMarshaller(BrandMarshaller)
    buyMarshaller(BuyMarshaller)
    categoryMarshaller(CategoryMarshaller)
    commentMarshaller(CommentMarshaller)
    imageMarshaller(ImageMarshaller)
    personMarshaller(PersonMarshaller)
    priceMarshaller(PriceMarshaller)
    customMarshallerRegistrar(MarshallerListRegistrar)

    storage(FileSystemService)

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

            storage(GoogleDriveService)
        }
    }
}
