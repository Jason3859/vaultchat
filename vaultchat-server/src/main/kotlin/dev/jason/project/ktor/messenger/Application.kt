package dev.jason.project.ktor.messenger

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import dev.jason.project.ktor.messenger.data.projectModule
import dev.jason.project.ktor.messenger.plugins.configureRouting
import dev.jason.project.ktor.messenger.plugins.configureSerialization
import dev.jason.project.ktor.messenger.plugins.configureSockets
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvException
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import org.koin.ktor.plugin.Koin
import java.io.File

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    initFirebase()
    initKoin()
    configureSerialization()
    configureSockets()
    configureRouting()
}

fun getDotenvInstance(): Dotenv? {
    return try {
        dotenv()
    } catch (_: DotenvException) {
        println(".env file not found!")
        null
    }
}

private fun Application.initKoin() {
    install(Koin) {
        modules(projectModule)
    }
}

private fun Application.initFirebase() {
    val file = adminSdkJsonFromEnvToFile()

    val serviceAccount = file?.inputStream()
        ?: javaClass.classLoader
            .getResourceAsStream("vaultchat-admin-sdk.json")

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    FirebaseApp.initializeApp(options)
}

private fun adminSdkJsonFromEnvToFile(): File? {
    val content = System.getenv("ADMIN_SDK_JSON") ?: return null

    val parent = File("ext-res")
    val child = File(parent, "vaultchat-admin-sdk.json")

    if (!parent.exists())
        parent.mkdir()

    if (!child.exists())
        child.createNewFile()

    child.writeText(content)

    return child
}