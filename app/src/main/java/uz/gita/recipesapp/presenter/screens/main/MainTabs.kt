package uz.gita.recipesapp.presenter.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import uz.gita.recipesapp.R
import uz.gita.recipesapp.presenter.screens.categories.CategoriesScreen
import uz.gita.recipesapp.presenter.screens.home.HomeScreen
import uz.gita.recipesapp.presenter.screens.saved.SavedScreen
import uz.gita.recipesapp.presenter.screens.search.SearchScreen
import uz.gita.recipesapp.presenter.ui.state.MainTab

object HomeTab : Tab {
    override val options: TabOptions
        @Composable get() = TabOptions(
            index = MainTab.HOME.index.toUShort(),
            title = stringResource(R.string.nav_home),
            icon = rememberVectorPainter(Icons.Rounded.Home)
        )

    @Composable
    override fun Content() {
        Navigator(HomeScreen())
    }
}

object CategoriesTab : Tab {
    override val options: TabOptions
        @Composable get() = TabOptions(
            index = MainTab.CATEGORIES.index.toUShort(),
            title = stringResource(R.string.nav_categories),
            icon = rememberVectorPainter(Icons.Rounded.GridView)
        )

    @Composable
    override fun Content() {
        Navigator(CategoriesScreen())
    }
}

object SearchTab : Tab {
    override val options: TabOptions
        @Composable get() = TabOptions(
            index = MainTab.SEARCH.index.toUShort(),
            title = stringResource(R.string.nav_search),
            icon = rememberVectorPainter(Icons.Rounded.Search)
        )

    @Composable
    override fun Content() {
        Navigator(SearchScreen())
    }
}

object SavedTab : Tab {
    override val options: TabOptions
        @Composable get() = TabOptions(
            index = MainTab.SAVED.index.toUShort(),
            title = stringResource(R.string.nav_saved),
            icon = rememberVectorPainter(Icons.Rounded.Bookmarks)
        )

    @Composable
    override fun Content() {
        Navigator(SavedScreen())
    }
}

internal val mainTabs: List<Pair<Tab, ImageVector>> = listOf(
    HomeTab to Icons.Rounded.Home,
    CategoriesTab to Icons.Rounded.GridView,
    SearchTab to Icons.Rounded.Search,
    SavedTab to Icons.Rounded.Bookmarks
)

internal fun tabOf(tab: MainTab): Tab = when (tab) {
    MainTab.HOME -> HomeTab
    MainTab.CATEGORIES -> CategoriesTab
    MainTab.SEARCH -> SearchTab
    MainTab.SAVED -> SavedTab
}
