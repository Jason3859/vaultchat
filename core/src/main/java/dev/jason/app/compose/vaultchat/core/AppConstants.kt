package dev.jason.app.compose.vaultchat.core

object AppConstants {

    const val URL_HOST = "10.0.2.2:8080"
    const val BASE_HTTP_URL = "http://$URL_HOST"
    const val BASE_WS_URL = "ws://$URL_HOST"
//    const val BASE_URL = "https://vaultchat-67jk.onrender.com"

    const val TEXT_PLAIN = "text/plain"
    const val EXTRA_NAV_DESTINATION_KEY = "nav_destination"
    const val ACTION_START_MAIN_ACTIVITY = "dev.jason.app.compose.vaultchat.main.ACTION_START_MAIN_ACTIVITY"
    const val SHARE_ACTIVITY_LAUNCH_MAIN_ACTIVITY_ACTION = "SHARE_ACTIVITY_LAUNCH_MAIN_ACTIVITY_ACTION"
    const val ACTION_MAIN_ACTIVITY_CLEAR_OTHER_USER = "ACTION_MAIN_ACTIVITY_CLEAR_OTHER_USER"
}