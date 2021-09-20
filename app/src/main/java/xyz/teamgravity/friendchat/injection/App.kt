package xyz.teamgravity.friendchat.injection

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.livedata.ChatDomain
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var client: ChatClient

    override fun onCreate() {
        super.onCreate()

        // initialize steam sdk
        ChatDomain.Builder(client, applicationContext).build()
    }
}