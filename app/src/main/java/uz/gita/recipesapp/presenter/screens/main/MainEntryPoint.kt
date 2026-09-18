package uz.gita.recipesapp.presenter.screens.main

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.recipesapp.presenter.ui.state.TabSwitcher

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MainEntryPoint {
    fun tabSwitcher(): TabSwitcher
}
