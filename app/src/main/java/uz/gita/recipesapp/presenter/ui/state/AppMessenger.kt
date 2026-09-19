package uz.gita.recipesapp.presenter.ui.state

import androidx.annotation.StringRes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import uz.gita.recipesapp.R
import uz.gita.recipesapp.domain.exception.ApiException
import uz.gita.recipesapp.domain.exception.NetworkException
import uz.gita.recipesapp.domain.exception.NotFoundException
import uz.gita.recipesapp.domain.repository.NetworkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppMessenger @Inject constructor(
    private val networkRepository: NetworkRepository
) {

    private val channel = Channel<UiMessage>(Channel.BUFFERED)

    val messages: Flow<UiMessage> = channel.receiveAsFlow()

    fun show(@StringRes id: Int) {
        channel.trySend(UiMessage.Resource(id))
    }

    fun showError(error: Throwable) {
        val message = when (error) {
            is NetworkException -> {
                if (!networkRepository.isOnline().value) return
                UiMessage.Resource(R.string.error_server_unreachable)
            }

            is ApiException, is NotFoundException ->
                error.message?.let { UiMessage.Text(it) } ?: UiMessage.Resource(R.string.error_unknown)

            else -> UiMessage.Resource(R.string.error_unknown)
        }
        channel.trySend(message)
    }
}
