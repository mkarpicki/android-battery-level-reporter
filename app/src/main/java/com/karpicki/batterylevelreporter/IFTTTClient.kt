package com.karpicki.batterylevelreporter

import android.util.Log
import android.util.NoSuchPropertyException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*

class IFTTTClient {

    companion object {
        suspend fun send(eventName: String, value: String) : Int =

            withContext(Dispatchers.IO) {

                var responseCode: Int
                val mediaType: MediaType? = MediaType.parse("application/json; charset=utf-8");
                val json = "{\"value1\":\"$value\"}"

                val apiKey = BuildConfig.IFTTT_API_KEY
                val body: RequestBody = RequestBody.create(mediaType, json)

                if (apiKey == "") {
                    // @readme remember to set thing-speak.api-key in local.properties and
                    // let BuildConfig.java regenerate
                    throw NoSuchPropertyException("local.properties > ifttt.api-key")
                }

                try {
                    val client = OkHttpClient();

                    val request: Request = Request.Builder()
                        .url("https://maker.ifttt.com/trigger/$eventName/with/key/$apiKey")
                        .post(body)
                        .build()

                    val response: Response = client.newCall(request).execute()

                    Log.d("IFTTT", "response.code():" + response.code())
                    //response.body()?.string()
                    responseCode = response.code()

                } catch (e: Exception) {
                    Log.e("IFTTT", e.message.toString())
                    responseCode = 500
                }
                responseCode
            }
    }
}