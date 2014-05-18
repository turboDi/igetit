package ru.jconsulting.igetit

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.util.Base64
import com.google.api.client.util.SecurityUtils
import com.google.api.services.drive.Drive

import java.security.spec.PKCS8EncodedKeySpec

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 11.05.14 18:02
 */
class GoogleDriveServiceFactory {

    Drive createDrive(HttpTransport httpTransport, JsonFactory jsonFactory, String accountId, Collection<String> scopes, String privateKey) {

        byte[] bytes = Base64.decodeBase64(privateKey)

        def credential = new GoogleCredential.Builder().setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(accountId)
                .setServiceAccountScopes(scopes)
                .setServiceAccountPrivateKey(SecurityUtils.getRsaKeyFactory().generatePrivate(new PKCS8EncodedKeySpec(bytes)))
                .build();

        credential.refreshToken();

        return new Drive.Builder(httpTransport, jsonFactory, credential).build()
    }
}
