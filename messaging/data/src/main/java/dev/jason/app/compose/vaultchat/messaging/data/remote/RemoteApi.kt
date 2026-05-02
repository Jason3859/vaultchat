package dev.jason.app.compose.vaultchat.messaging.data.remote

import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.messaging.data.dto.MessageDto
import dev.jason.app.compose.vaultchat.messaging.data.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface RemoteApi {
    suspend fun sendMessage(body: MessageDto): Int
    suspend fun searchUsers(name: String, from: String): List<UserDto>
    suspend fun updateStatus(uid: String, status: User.Status)
    suspend fun getConnections(uid: String): List<UserDto>
    suspend fun fetchBlocklist(uid: String): List<UserDto>
    suspend fun block(uid: String, uidToBlock: String)
    suspend fun unblock(uid: String, uidToUnblock: String)
    suspend fun updateToken(uid: String, token: String, deviceDto: UserDto.DeviceDto)
    suspend fun fetchDevices(uid: String): List<UserDto.DeviceDto>
}

class KtorRemoteApi(private val httpClient: HttpClient, private val baseUrl: String) : RemoteApi {

    override suspend fun sendMessage(body: MessageDto): Int {
        val response: HttpResponse = httpClient.post("$baseUrl/messaging/send") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return response.status.value
    }

    override suspend fun searchUsers(name: String, from: String): List<UserDto> {
        return httpClient.get("$baseUrl/user/search") {
            url {
                parameters.append("name", name)
                parameters.append("from", from)
            }
        }.body()
    }

    override suspend fun updateStatus(uid: String, status: User.Status) {
        httpClient.put("$baseUrl/user/update-status") {
            contentType(ContentType.Application.Json)
            url {
                parameters.append("uid", uid)
                parameters.append("status", status.name)
            }
        }
    }

    override suspend fun getConnections(uid: String): List<UserDto> {
        return httpClient.get("$baseUrl/social/get-connections") {
            url {
                parameters.append("uid", uid)
            }
        }.body()
    }

    override suspend fun fetchBlocklist(uid: String): List<UserDto> {
        return httpClient.get("$baseUrl/social/get-blocked-users") {
            url {
                parameters.append("uid", uid)
            }
        }.body()
    }

    override suspend fun block(uid: String, uidToBlock: String) {
        httpClient.post("$baseUrl/social/block") {
            contentType(ContentType.Application.Json)
            url {
                parameters.append("uid", uid)
                parameters.append("uid_to_block", uidToBlock)
            }
        }
    }

    override suspend fun unblock(uid: String, uidToUnblock: String) {
        httpClient.post("$baseUrl/social/unblock") {
            contentType(ContentType.Application.Json)
            url {
                parameters.append("uid", uid)
                parameters.append("uid_to_unblock", uidToUnblock)
            }
        }
    }

    override suspend fun updateToken(uid: String, token: String, deviceDto: UserDto.DeviceDto) {
        httpClient.post("$baseUrl/device/update-token") {
            contentType(ContentType.Application.Json)
            url {
                parameters.append("uid", uid)
                parameters.append("token", token)
            }
            setBody(deviceDto)
        }
    }

    override suspend fun fetchDevices(uid: String): List<UserDto.DeviceDto> {
        return httpClient.get("$baseUrl/device/my-devices") {
            url {
                parameters.append("uid", uid)
            }
        }.body()
    }
}