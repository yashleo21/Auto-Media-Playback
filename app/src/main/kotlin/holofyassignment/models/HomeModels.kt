package holofyassignment.models

data class HomeDataObject(var description: String?,
                          var sources: ArrayList<String>?,
                          var subtitle: String?,
                          var thumb: String?,
                          var title: String?)

data class HomeResponseContainer(var categories: ArrayList<HomeResponseObject>) {
    data class HomeResponseObject(var name: String?,
                                  var videos: ArrayList<HomeDataObject>?)
}