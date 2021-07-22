package holofyassignment.repositories

import holofyassignment.datasource.HomeLocalDataSource
import holofyassignment.models.HomeDataObject
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    val localDataSource: HomeLocalDataSource
) : HomeRepository {

    override fun getHomeData(): ArrayList<HomeDataObject> {
        return localDataSource.getData()
    }
}