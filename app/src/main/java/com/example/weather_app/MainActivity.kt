package com.example.weather_app

import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weather_app.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//0b8da469f27381cbd9666c53c61ee80e

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy{

        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        fetchWeatherData("")
        SearchCity()
    }

    private fun SearchCity() {
        val searchView=binding.search
        searchView.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true           }


        })
    }

    fun fetchWeatherData(cityName:String){
            val retrofit= Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .build().create(APIinterface::class.java)
            val response= retrofit.getWeatherData(cityName ,"0b8da469f27381cbd9666c53c61ee80e" , "metric" )
            response.enqueue(object :Callback<WeatherApp>{
                override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                    val responseBody=response.body()
                    if (response.isSuccessful && responseBody!= null)
                    {
                     val temperature= responseBody.main.temp.toString()

                        val humidity=responseBody.main.humidity
                        val windSpeed=responseBody.wind.speed
                        val sunRise=responseBody.sys.sunrise.toLong()
                        val sunSet=responseBody.sys.sunset.toLong()
                        val seaLevel=responseBody.main.pressure
                        val condition=responseBody.weather.firstOrNull()?.main?:"unknown"
                        val maxTemp=responseBody.main.temp_max
                        val minTemp=responseBody.main.temp_min


                        binding.temp1.text="$temperature °C"
                        binding.temp.text=condition
                        binding.max.text="Max Temp:$maxTemp °C"
                        binding.min.text="Min Temp:$minTemp °C"
                        binding.humidity.text="$humidity %"
                        binding.wind.text="$windSpeed m/s"
                        binding.sunrise.text="${time(sunRise)}"
                        binding.sunset.text="${time(sunSet)}"
                        binding.sea.text="$seaLevel hPa"
                        binding.condition.text=condition
                        binding.day.text=dayName(System.currentTimeMillis())
                        binding.date.text= date()
                       binding.location.text="$cityName"


                    //Log.d("TAG", "onResponse: $temperature")
                        changeImage(condition)

                    }
                }

                override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

        }

    private fun changeImage(conditions:String) {
        when(conditions)
        {
            "Haze"->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView2.setAnimation(R.raw.cloud)

            }
            "Clear Sky", "Sunny", "Clear"->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView2.setAnimation(R.raw.sun)

            }
            "Partly Clouds","Clouds","Overcast","Mist"->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView2.setAnimation(R.raw.cloud)

            }
            "Light Rain","Drizzle","Heavy Rain"->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView2.setAnimation(R.raw.rain)

            }
            "Haze"->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView2.setAnimation(R.raw.cloud)

            }
            else->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView2.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView2.playAnimation()

    }

    private fun date(): String? {
        var sdf=SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))

    }

    fun dayName(timestamp: Long):String{
        var sdf=SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }

  private  fun time(timestamp: Long):String{
        val sdf=SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }



    }
