package uz.gita.recipesapp.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface NetworkRepository {

    fun isOnline(): StateFlow<Boolean>
}
