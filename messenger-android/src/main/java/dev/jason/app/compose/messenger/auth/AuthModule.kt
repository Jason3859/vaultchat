package dev.jason.app.compose.messenger.auth

import dev.jason.app.compose.messenger.auth.data.email.FirebaseEmailAuthentication
import dev.jason.app.compose.messenger.auth.data.github.FirebaseGitHubAuthentication
import dev.jason.app.compose.messenger.auth.data.google.FirebaseGoogleAuthentication
import dev.jason.app.compose.messenger.auth.ui.viewmodel.email_auth.EmailAuthViewModel
import dev.jason.app.compose.messenger.auth.ui.viewmodel.github_auth.GitHubAuthViewModel
import dev.jason.app.compose.messenger.auth.ui.viewmodel.google_auth.GoogleAuthViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    singleOf(::FirebaseGoogleAuthentication)
    singleOf(::FirebaseGitHubAuthentication)
    singleOf(::FirebaseEmailAuthentication)
    viewModelOf(::GoogleAuthViewModel)
    viewModelOf(::GitHubAuthViewModel)
    viewModelOf(::EmailAuthViewModel)
}