package dev.jason.app.compose.vaultchat.ui.main.concrete

import dev.jason.app.compose.vaultchat.ui.main.concrete.home.HomeViewModel
import dev.jason.app.compose.vaultchat.ui.main.concrete.messaging.MessagingViewModel
import dev.jason.app.compose.vaultchat.ui.main.concrete.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val MainKoinModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::MessagingViewModel)
    viewModelOf(::ProfileViewModel)
}