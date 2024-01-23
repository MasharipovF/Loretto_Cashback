package com.example.lorettocashback.domain.interactor

import com.example.lorettocashback.data.entity.businesspartners.*
import com.example.lorettocashback.data.repository.BpRepository
import com.example.lorettocashback.data.repository.BpRepositoryImpl
import com.example.lorettocashback.data.repository.MasterDataRepository
import com.example.lorettocashback.data.repository.MasterDataRepositoryImpl
import com.example.lorettocashback.domain.dto.error.ErrorResponse

interface BpInteractor {



    suspend fun getUserdata(login: String, password: String): BusinessPartners?
    val errorMessage: String?
}

class BpInteractorImpl : BpInteractor {

    private val repository: BpRepository by lazy { BpRepositoryImpl() }
    private val masterDataRepo: MasterDataRepository by lazy { MasterDataRepositoryImpl() }

    override var errorMessage: String? = null



    override suspend fun getUserdata(login: String, password: String): BusinessPartners? {
        val response = repository.getUserData(login, password)

        return if (response is BusinessPartnersVal) {

            if (!response.value.isNullOrEmpty()){
                response.value[0]
            } else {
                errorMessage = "Бизнес партнер с кодом $login не найден!"
                null
            }

        } else {
            errorMessage = (response as ErrorResponse).error.message.value
            null
        }

    }


}