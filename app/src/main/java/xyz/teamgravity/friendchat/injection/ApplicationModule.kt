package xyz.teamgravity.friendchat.injection

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory
import xyz.teamgravity.friendchat.R
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideChatClient(@ApplicationContext context: Context) =
        ChatClient.Builder(context.getString(R.string.api_key), context).build()

    @Singleton
    @Provides
    fun provideChannelListViewModelFactory() = ChannelListViewModelFactory(
        filter = Filters.and(Filters.eq("type", "messaging")),
        sort = ChannelListViewModel.DEFAULT_SORT,
        limit = 30
    )
}