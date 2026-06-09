package dev.jason.app.compose.vaultchat.messaging.ui

import dev.jason.app.compose.vaultchat.messaging.ui.home.HomeViewModel
import dev.jason.app.compose.vaultchat.messaging.ui.messaging.MessagingViewModel
import dev.jason.app.compose.vaultchat.messaging.ui.profile.ProfileScreenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val MessagingUiKoinModule = module {
    viewModelOf(::MessagingViewModel)
    viewModelOf(::ProfileScreenViewModel)
    viewModelOf(::HomeViewModel)
}