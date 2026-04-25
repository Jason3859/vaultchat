package dev.jason.app.compose.vaultchat.messaging.ui

import dev.jason.app.compose.vaultchat.messaging.ui.main.MainHomeViewModel
import dev.jason.app.compose.vaultchat.messaging.ui.messaging.MessagingViewModel
import dev.jason.app.compose.vaultchat.messaging.ui.profile.ProfileScreenViewModel
import dev.jason.app.compose.vaultchat.messaging.ui.search.SearchUsersViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val MessagingUiKoinModule = module {
    viewModelOf(::SearchUsersViewModel)
    viewModelOf(::MessagingViewModel)
    viewModelOf(::MainHomeViewModel)
    viewModelOf(::ProfileScreenViewModel)
}