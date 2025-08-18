package com.example.handyman.domain.repository

import com.example.handyman.domain.model.ServiceProvider
import kotlinx.coroutines.flow.Flow

interface HandymanRepository {
    fun getProviders(): Flow<List<ServiceProvider>>
}
