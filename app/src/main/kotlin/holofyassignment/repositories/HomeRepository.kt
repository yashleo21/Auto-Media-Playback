package holofyassignment.repositories

import holofyassignment.models.HomeDataObject

interface HomeRepository {
    fun getHomeData(): ArrayList<HomeDataObject>
}