package uz.gita.recipesapp.presenter.ui.components

import android.content.pm.ActivityInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import uz.gita.recipesapp.R
import uz.gita.recipesapp.presenter.ui.util.findActivity

@Composable
fun YouTubeVideoPlayer(
    videoId: String,
    onError: () -> Unit,
    onEnterFullscreen: (fullscreenView: View, exitFullscreen: () -> Unit) -> Unit,
    onExitFullscreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val currentOnError by rememberUpdatedState(onError)
    val currentOnEnterFullscreen by rememberUpdatedState(onEnterFullscreen)
    val currentOnExitFullscreen by rememberUpdatedState(onExitFullscreen)

    val playerView = remember(videoId) {
        val hostContext = context.findActivity() ?: context
        val view = LayoutInflater.from(hostContext)
            .inflate(R.layout.view_youtube_player, null, false) as YouTubePlayerView
        view.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                currentOnEnterFullscreen(fullscreenView, exitFullscreen)
            }

            override fun onExitFullscreen() {
                currentOnExitFullscreen()
            }
        })
        view.initialize(
            object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }

                override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                    currentOnError()
                }
            },
            false,
            IFramePlayerOptions.Builder(hostContext)
                .controls(1)
                .fullscreen(1)
                .rel(0)
                .ivLoadPolicy(3)
                .build()
        )
        view
    }

    DisposableEffect(lifecycle, playerView) {
        lifecycle.addObserver(playerView)
        onDispose {
            lifecycle.removeObserver(playerView)
            playerView.release()
        }
    }

    AndroidView(
        factory = { playerView },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun VideoFullscreenEffect(enabled: Boolean) {
    val activity = LocalContext.current.findActivity() ?: return
    DisposableEffect(enabled) {
        val controller = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        if (enabled) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.hide(WindowInsetsCompat.Type.systemBars())
        }
        onDispose {
            if (enabled) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                controller.show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }
}

@Composable
fun FullscreenVideoContainer(
    view: View,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        factory = { context -> FrameLayout(context) },
        update = { container ->
            if (view.parent !== container) {
                (view.parent as? ViewGroup)?.removeView(view)
                container.removeAllViews()
                container.addView(
                    view,
                    FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
            }
        },
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    )
}

