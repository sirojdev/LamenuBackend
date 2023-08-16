package mimsoft.io.utils.plugins

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.*
import java.io.FileInputStream




fun Application.configureFirebase() {
    val serviceAccount = FileInputStream("amazing-gearing-369511-firebase-adminsdk-aank9-93221e88bd.json")

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    FirebaseApp.initializeApp(options)

}