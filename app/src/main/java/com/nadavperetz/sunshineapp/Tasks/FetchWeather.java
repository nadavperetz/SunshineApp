package com.nadavperetz.sunshineapp.Tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nadavperetz on 22/09/16.
 */

public class FetchWeather extends AsyncTask<String, Void, Void> {
    /*private final static String WEATHER_DATA_MTV_JUN_4 = "{\"cod\":\"200\",\"message\":4.2116,\"city\":{\"id\":\"5375480\",\"name\":\"Mountain View\",\"coord\":{\"lon\":-122.075,\"lat\":37.4103},\"country\":\"United States of America\",\"population\":0},\"cnt\":7,\"list\":[{\"dt\":1401912000,\"temp\":{\"day\":20.17,\"min\":12.3,\"max\":20.17,\"night\":12.3,\"eve\":17.74,\"morn\":14.05},\"pressure\":1012.43,\"humidity\":77,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":1.67,\"deg\":253,\"clouds\":0},{\"dt\":1401998400,\"temp\":{\"day\":18.9,\"min\":10.74,\"max\":18.9,\"night\":10.74,\"eve\":15.54,\"morn\":14.02},\"pressure\":1009.89,\"humidity\":76,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":1.51,\"deg\":225,\"clouds\":0},{\"dt\":1402084800,\"temp\":{\"day\":13.59,\"min\":13.57,\"max\":14.1,\"night\":14.1,\"eve\":14.04,\"morn\":13.57},\"pressure\":1022.58,\"humidity\":0,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":8.92,\"deg\":325,\"clouds\":0},{\"dt\":1402171200,\"temp\":{\"day\":13.71,\"min\":13.71,\"max\":13.93,\"night\":13.93,\"eve\":13.73,\"morn\":13.93},\"pressure\":1021.29,\"humidity\":0,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":6.41,\"deg\":326,\"clouds\":0},{\"dt\":1402257600,\"temp\":{\"day\":13.55,\"min\":13.52,\"max\":13.72,\"night\":13.52,\"eve\":13.72,\"morn\":13.62},\"pressure\":1022.14,\"humidity\":0,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":9.72,\"deg\":320,\"clouds\":0},{\"dt\":1402344000,\"temp\":{\"day\":12.72,\"min\":12.72,\"max\":13.22,\"night\":13.22,\"eve\":13.19,\"morn\":13.06},\"pressure\":1027.87,\"humidity\":0,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":7.85,\"deg\":322,\"clouds\":7},{\"dt\":1402430400,\"temp\":{\"day\":13.11,\"min\":12.89,\"max\":13.35,\"night\":13.26,\"eve\":13.35,\"morn\":12.89},\"pressure\":1029.35,\"humidity\":0,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01d\"}],\"speed\":11.01,\"deg\":330,\"clouds\":0}]}";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            JSONObject text = new JSONObject(WEATHER_DATA_MTV_JUN_4);
            JSONArray array = (JSONArray) text.get("list");
            JSONObject temp = array.getJSONObject(2).getJSONObject("temp");
            Log.v("FetchWeather", temp.get("max").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }*/

    @Override
    protected Void doInBackground(String... strings) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String post_code = strings[0];
        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;
        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are available at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            Uri.Builder url_path = new Uri.Builder();
            url_path.scheme("http");
            url_path.authority("api.openweathermap.org");
            url_path.path("data/2.5/forecast/daily");
            url_path.appendQueryParameter("q", post_code);
            url_path.appendQueryParameter("mode", "json");
            url_path.appendQueryParameter("units", "metric");
            url_path.appendQueryParameter("cnt", "7");
            url_path.appendQueryParameter("APPID", "a65fb759cecf02de6be4949f649bed95");

            URL url = new URL(url_path.toString());
            //"http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&APPID=a65fb759cecf02de6be4949f649bed95"
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                forecastJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                forecastJsonStr = null;
            }
            forecastJsonStr = buffer.toString();
            Log.v("FetchWeather", forecastJsonStr);
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            forecastJsonStr = null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return null;
    }
}
