//abstract class can't go in to the manifest
//Had to initial Location with an empty string.
//object : Callback() = new Callback()
//private  var mMap: GoogleMap? = null to initialize in kotlin
//use Terminal rm --cached gradle.properties to exclude it after you upload it
//put gradle.properties in .gitignore to ignore it.
//reference api key in gradle.properties
// put the following build gradle module to access api from anywhere
/*resValue 'string', "api_key", MyAwesomeApp_ApiKey
buildConfigField 'String', "ApiKey", MyAwesomeApp_ApiKey*/
//To reference java classes APIService::class.java