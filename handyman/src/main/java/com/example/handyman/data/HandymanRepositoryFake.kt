package com.example.handyman.data

import com.example.handyman.domain.model.ServiceProvider
import com.example.handyman.domain.repository.HandymanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HandymanRepositoryFake : HandymanRepository {
    override fun getProviders(): Flow<List<ServiceProvider>> = flow {
        emit(
            listOf(
                ServiceProvider("1", "Alex", "Plumber", 4.7, 35),
                ServiceProvider("2", "Maria", "Electrician", 4.6, 40),
                ServiceProvider("3", "Sam", "Carpenter", 4.5, 30)
            )
        )
    }
}
