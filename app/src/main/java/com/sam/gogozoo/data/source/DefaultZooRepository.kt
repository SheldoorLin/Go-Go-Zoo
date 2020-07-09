package com.sam.gogozoo.data.source

import com.sam.gogozoo.data.animal.AnimalData
import com.sam.gogozoo.data.animal.FireAnimal
import com.sam.gogozoo.data.Result
import com.sam.gogozoo.data.area.AreaData
import com.sam.gogozoo.data.area.FireArea
import com.sam.gogozoo.data.facility.FacilityData
import com.sam.gogozoo.data.facility.FireFacility
import com.sam.gogozoo.data.model.DirectionResponses
import retrofit2.Call


/**
 * Created by Wayne Chen on 2020-01-15.
 *
 * Concrete implementation to load Publisher sources.
 */
class DefaultZooRepository(private val remoteDataSource: ZooDataSource,
                           private val localDataSource: ZooDataSource
) : ZooRepository {

    override fun getDirection(
        origin: String,
        destination: String,
        apiKey: String,
        mode: String
    ): Call<DirectionResponses> {
        return remoteDataSource.getDirection(origin, destination, apiKey)
    }

    override suspend fun getApiAnimals(): Result<AnimalData> {
        return remoteDataSource.getApiAnimals()
    }

    override suspend fun getApiAreas(): Result<AreaData> {
        return remoteDataSource.getApiAreas()
    }

    override suspend fun getApiFacility(): Result<FacilityData> {
        return remoteDataSource.getApiFacility()
    }

    override suspend fun publishAnimal(fireAnimal: FireAnimal): Result<Boolean> {
        return remoteDataSource.publishAnimal(fireAnimal)
    }

    override suspend fun publishArea(fireArea: FireArea): Result<Boolean> {
        return remoteDataSource.publishArea(fireArea)
    }

    override suspend fun publishFacility(fireFacility: FireFacility): Result<Boolean> {
        return remoteDataSource.publishFacility(fireFacility)
    }
}