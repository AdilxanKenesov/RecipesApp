package uz.gita.recipesapp.presenter.screens.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import dagger.hilt.android.EntryPointAccessors
import uz.gita.recipesapp.presenter.ui.state.TabSwitcher
import uz.gita.recipesapp.presenter.ui.theme.Sizes
import uz.gita.recipesapp.presenter.ui.theme.Spacing
import uz.gita.recipesapp.presenter.ui.theme.oshxona
import uz.gita.recipesapp.presenter.ui.util.scaleClickable

class MainScreen : Screen {

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val tabSwitcher = remember(context) {
            EntryPointAccessors.fromApplication(
                context.applicationContext,
                MainEntryPoint::class.java
            ).tabSwitcher()
        }

        TabNavigator(HomeTab) { tabNavigator ->
            LaunchedEffect(tabNavigator) {
                tabSwitcher.target.collect { target ->
                    tabNavigator.current = tabOf(target)
                    tabSwitcher.consumeTarget()
                }
            }

            BackHandler(enabled = tabNavigator.current.key != HomeTab.key) {
                tabNavigator.current = HomeTab
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.oshxona.ground)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    CurrentTab()
                }
                MainBottomBar(
                    current = tabNavigator.current,
                    onSelect = { tabNavigator.current = it }
                )
            }
        }
    }

    @Composable
    private fun MainBottomBar(
        current: Tab,
        onSelect: (Tab) -> Unit
    ) {
        val colors = MaterialTheme.oshxona
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.surface)
        ) {
            HorizontalDivider(color = colors.hairlineSoft)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .height(Sizes.bottomBar),
                verticalAlignment = Alignment.CenterVertically
            ) {
                mainTabs.forEach { (tab, icon) ->
                    MainTabItem(
                        tab = tab,
                        icon = icon,
                        selected = current.key == tab.key,
                        onClick = { onSelect(tab) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    @Composable
    private fun MainTabItem(
        tab: Tab,
        icon: ImageVector,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val colors = MaterialTheme.oshxona
        val title = tab.options.title
        Column(
            modifier = modifier
                .height(Sizes.bottomBar)
                .scaleClickable(role = Role.Tab, onClick = onClick)
                .padding(vertical = Spacing.xxs),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (selected) colors.primary else colors.inkFaint,
                modifier = Modifier.size(Sizes.icon)
            )
            Spacer(Modifier.size(2.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = if (selected) colors.primary else colors.inkFaint
            )
        }
    }
}
