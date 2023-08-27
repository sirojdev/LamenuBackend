package mimsoft.io.utils.plugins

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.*
import java.io.FileInputStream


fun Application.configureFirebase() {
    val serviceAccount = FileInputStream("mimcafeuz-firebase-adminsdk.json")

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    var installed = false
    val installedApps = FirebaseApp.getApps()
    for (app in installedApps) {
        if (app.name.equals(FirebaseApp.DEFAULT_APP_NAME))
            installed = true
    }

    if (!installed)
        FirebaseApp.initializeApp(options)

}