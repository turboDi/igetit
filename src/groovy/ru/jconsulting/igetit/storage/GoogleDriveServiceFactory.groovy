package ru.jconsulting.igetit.storage

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.util.Base64
import com.google.api.client.util.SecurityUtils
import com.google.api.services.drive.Drive

import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 11.05.14 18:02
 */
class GoogleDriveServiceFactory {

    String appName
    String accountId
    String privateKey
    String privateKeyFilePath

    Drive createDrive(HttpTransport httpTransport, JsonFactory jsonFactory, Collection<String> scopes) {

        def credential = new GoogleCredential.Builder().setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(accountId)
                .setServiceAccountScopes(scopes)
                .setServiceAccountPrivateKey(retrievePrivateKey())
                .build();

        credential.refreshToken();

        new Drive.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(appName)
                .build()
    }

    private PrivateKey retrievePrivateKey() {
        if (privateKeyFilePath) {
            SecurityUtils.loadPrivateKeyFromKeyStore(SecurityUtils.getPkcs12KeyStore(),
                    new FileInputStream(privateKeyFilePath), "notasecret", "privatekey", "notasecret");
        } else if (privateKey) {
            byte[] bytes = Base64.decodeBase64(privateKey)
            SecurityUtils.getRsaKeyFactory().generatePrivate(new PKCS8EncodedKeySpec(bytes))
        } else {
            throw new IllegalStateException('GDrive requires either private key string or file location specified')
        }
    }
}
