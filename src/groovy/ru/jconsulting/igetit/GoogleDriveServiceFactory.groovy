package ru.jconsulting.igetit

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.services.drive.Drive

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 11.05.14 18:02
 */
class GoogleDriveServiceFactory {

    Drive createDrive(HttpTransport httpTransport, JsonFactory jsonFactory, String accountId, Collection<String> scopes, String p12File) {

        def credential = new GoogleCredential.Builder().setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(accountId)
                .setServiceAccountScopes(scopes)
                .setServiceAccountPrivateKeyFromP12File(new File(p12File))
                .build();

        credential.refreshToken();

        return new Drive.Builder(httpTransport, jsonFactory, credential).build()
    }
}
