package com.example.wewantour9;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class API_usage {

    private static final String geocoding_api_call_string = "https://dev.virtualearth.net/REST/v1/Locations?";
    private static final String weather_api_call_string = "https://api.openweathermap.org/data/2.5/onecall?";

    private static String str = "";
    private static String testStringToTest = "";

    //take the context and the place in the string form of firebase (City, Street, civic) and returns a list with two strings ["latitude", "longitude"]
    public static ArrayList<String> getCoordinates(Context context, String place){

        String city = place.split(",")[0], street = place.split(",")[1], number = place.split(",")[2];

        ArrayList<String> listCoordinates = new ArrayList<String>();

        try {

            Uri builtUri = Uri.parse(geocoding_api_call_string)
                    .buildUpon()
                    .appendQueryParameter("addressLine", street+number)
                    .appendQueryParameter("locality", city)
                    .appendQueryParameter("key", context.getString(R.string.geocoding_api_key))
                    .build();

            URL api_call_url = new URL(builtUri.toString());

            HttpURLConnection connection = (HttpURLConnection) api_call_url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json_response = new StringBuffer(1024);

            String buffer = "";

            while ((buffer = br.readLine()) != null) {
                json_response.append(buffer).append("\n");
            }

            br.close();

            JSONObject data = new JSONObject(json_response.toString());

            if(data.getInt("statusCode") != 200){
                listCoordinates.add(null);
                return listCoordinates;
            }

            JSONArray coordinates = data.getJSONArray("resourceSets").getJSONObject(0).getJSONArray("resources").getJSONObject(0).getJSONArray("geocodePoints").getJSONObject(0).getJSONArray("coordinates");

            String latitude = coordinates.getString(0);
            String longitude = coordinates.getString(1);
            listCoordinates.add(latitude);
            listCoordinates.add(longitude);

            return listCoordinates;

        }catch(Exception e){
            listCoordinates.add(null);
            return listCoordinates;
        }
    }

    //take as input the context, the list of coordinates returned by getCoordinates() function and the date in the firebase tour format (day/month/year) and return the weather for this day
    public static JSONObject getWeather(Context context, ArrayList<String> coordinates, String dateTime){

        String latitude = coordinates.get(0);
        String longitude = coordinates.get(1);
        String[] dateSplit = dateTime.split("/");   //[day,month,year]
        JSONObject selectedWeather = null;

        try {

            Uri builtUri = Uri.parse(weather_api_call_string)
                    .buildUpon()
                    .appendQueryParameter("lat", latitude)
                    .appendQueryParameter("lon", longitude)
                    .appendQueryParameter("units", "metric")
                    .appendQueryParameter("exclude", "minutely,hourly,alerts,current")
                    .appendQueryParameter("appid", context.getString(R.string.weather_api_key))
                    .build();

            URL api_call_url = new URL(builtUri.toString());

            HttpURLConnection connection = (HttpURLConnection)api_call_url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json_response = new StringBuffer(1024);

            String buffer="";

            while((buffer = br.readLine()) != null){
                json_response.append(buffer).append("\n");
            }

            br.close();

            JSONObject data = new JSONObject(json_response.toString());

            JSONArray arrayDailyData = data.getJSONArray("daily");

            for (int i = 0; i < arrayDailyData.length(); i++) {

                JSONObject oneDayData = arrayDailyData.getJSONObject(i);

                long time = Integer.parseInt(oneDayData.getString("dt")) * (long) 1000;
                Date date = new Date(time);

                Calendar dateCalendar = Calendar.getInstance();
                dateCalendar.setTime(date);

                int forecast_year = dateCalendar.get(Calendar.YEAR);
                int forecast_month = dateCalendar.get(Calendar.MONTH)+1;
                int forecast_day = dateCalendar.get(Calendar.DAY_OF_MONTH);

                if((forecast_day == Integer.parseInt(dateSplit[0])) && (forecast_month == Integer.parseInt(dateSplit[1])) && (forecast_year == Integer.parseInt(dateSplit[2]))){
                    selectedWeather = oneDayData;
                }

            }

            if(selectedWeather == null){
                return null;
            }

            return selectedWeather;

        }catch(Exception e){
            return null;
        }
    }

    public static Drawable getIconFromWeatherCode(Context context, int weatherCode, String sunrise, String sunset, String tourHour){
        Drawable selectedIcon = null;

        long sunsetTime = Integer.parseInt(sunset) * (long) 1000;
        Date sunsetDate = new Date(sunsetTime);
        long sunriseTime = Integer.parseInt(sunrise) * (long) 1000;
        Date sunriseDate = new Date(sunriseTime);

        Calendar sunsetDateCalendar = Calendar.getInstance();
        sunsetDateCalendar.setTime(sunsetDate);
        Calendar sunriseDateCalendar = Calendar.getInstance();
        sunriseDateCalendar.setTime(sunriseDate);

        int sunriseH = sunriseDateCalendar.get(Calendar.HOUR_OF_DAY);
        int sunriseM = sunriseDateCalendar.get(Calendar.MINUTE);
        int sunsetH = sunsetDateCalendar.get(Calendar.HOUR_OF_DAY);
        int sunsetM = sunsetDateCalendar.get(Calendar.MINUTE);

        int tourH = Integer.parseInt(tourHour.split(":")[0]), tourM = Integer.parseInt(tourHour.split(":")[1]);

        if( ((tourH < sunriseH) && (tourM < sunsetM)) && ((tourH>sunsetH) && (tourM > sunsetM))){   //is day if the time is before the sunset and after the sunrise
            if( (weatherCode >= 200) && (weatherCode <= 232) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_200_232);
            }else if( (weatherCode >= 300) && (weatherCode <= 321)){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_300_321);
            }else if( (weatherCode == 500) || (weatherCode == 501) || (weatherCode == 502) || (weatherCode == 511) || (weatherCode == 520) || (weatherCode == 521) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_500_501_502_511_520_521);
            }else if( (weatherCode == 503) || (weatherCode == 504) || (weatherCode == 522) || (weatherCode == 531) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_503_504_522_531);
            }else if( (weatherCode == 600) || (weatherCode == 611) || (weatherCode == 615) || (weatherCode == 616) || (weatherCode == 620) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_600_611_615_616_620);
            }else if( (weatherCode == 601) || (weatherCode == 612) || (weatherCode == 621) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_601_612_621);
            }else if( (weatherCode == 602) || (weatherCode == 613) || (weatherCode == 622) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_602_613_622);
            }else if( (weatherCode >= 701) && (weatherCode <= 762) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_701_711_721_731_741_751_761_762);
            }else if( (weatherCode == 771) || (weatherCode == 781) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_771_781);
            }else if( (weatherCode == 800) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_800);
            }else if( (weatherCode == 801) || (weatherCode == 802) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_801_802);
            }else if( (weatherCode == 803) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_803);
            }else if( (weatherCode == 804) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_804);
            }
        }else{
            if( (weatherCode >= 200) && (weatherCode <= 232) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_200_232);
            }else if( (weatherCode >= 300) && (weatherCode <= 321)){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_300_321_n);
            }else if( (weatherCode == 500) || (weatherCode == 501) || (weatherCode == 502) || (weatherCode == 511) || (weatherCode == 520) || (weatherCode == 521) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_500_501_502_511_520_521_n);
            }else if( (weatherCode == 503) || (weatherCode == 504) || (weatherCode == 522) || (weatherCode == 531) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_503_504_522_531_n);
            }else if( (weatherCode == 600) || (weatherCode == 611) || (weatherCode == 615) || (weatherCode == 616) || (weatherCode == 620) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_600_611_615_616_620_n);
            }else if( (weatherCode == 601) || (weatherCode == 612) || (weatherCode == 621) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_601_612_621_n);
            }else if( (weatherCode == 602) || (weatherCode == 613) || (weatherCode == 622) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_602_613_622_n);
            }else if( (weatherCode >= 701) && (weatherCode <= 762) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_701_711_721_731_741_751_761_762);
            }else if( (weatherCode == 771) || (weatherCode == 781) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_771_781);
            }else if( (weatherCode == 800) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_800_n);
            }else if( (weatherCode == 801) || (weatherCode == 802) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_801_802_n);
            }else if( (weatherCode == 803) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_803_n);
            }else if( (weatherCode == 804) ){
                selectedIcon = context.getResources().getDrawable(R.drawable.weather_804_n);
            }
        }
        return selectedIcon;
    }

}
