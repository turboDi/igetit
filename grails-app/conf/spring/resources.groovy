import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson.JacksonFactory
import com.google.api.services.drive.DriveScopes
import ru.jconsulting.igetit.GoogleDriveServiceFactory
import ru.jconsulting.igetit.marshallers.*

beans = {
    customMarshallerRegistrar(MarshallerListRegistrar) {
        marshallerList = [
                new BrandMarshaller(),
                new BuyMarshaller(),
                new CategoryMarshaller(),
                new CommentMarshaller(),
                new ImageMarshaller(),
                new UserMarshaller(),
                new PriceMarshaller()
        ]
    }

    def config = application.config.google.drive

    httpTransport(NetHttpTransport)
    jsonFactory(JacksonFactory)
    driveServiceFactory(GoogleDriveServiceFactory)

    driveService(
            driveServiceFactory: "createDrive",
            httpTransport,
            jsonFactory,
            config.accountId as String,
            DriveScopes.DRIVE,
            config.p12File as String
    )
}
