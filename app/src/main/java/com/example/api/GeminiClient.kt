package com.example.api

import android.util.Log
import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// Model definitions matching the Gemini REST API structure
data class Part(val text: String? = null)
data class Content(val parts: List<Part>)
data class GenerateContentRequest(val contents: List<Content>)

data class Candidate(val content: Content?)
data class GenerateContentResponse(val candidates: List<Candidate>?)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val apiService: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    suspend fun askGemini(prompt: String, systemPrompt: String = ""): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "Gemini API key is unconfigured or a placeholder.")
            return "नमस्ते! मैं ग्रामीण विकास सहायक (Vikas AI) हूँ। कृपया गूगल एआई स्टूडियो के सेक्रेट्स पैनल (Secrets panel) में अपना GEMINI_API_KEY प्रविष्ट करें ताकि मैं लाइव जवाब दे सकूँ।\n\n💡 तब तक, यहाँ कुछ ग्राम-सलाह (Mock Advisory) प्रविष्टि है:\n\n🌾 **गेहूँ की सलाह:** वर्तमान मौसम में गेहूँ की फसल के लिए हल्की सिंचाई उपयुक्त है। प्रति एकड़ 40 किलो यूरिया का छिड़काव दोपहर के समय करें।\n⛈️ **मौसम पूर्वानुमान:** आज धुंध रहेगी। तापमान 24°C से 28°C के बीच रहेगा, बारिश की कोई संभावना नहीं है।"
        }

        // Format standard text request structure
        val contents = mutableListOf<Content>()
        if (systemPrompt.isNotEmpty()) {
            contents.add(Content(listOf(Part(text = "System Instruction: $systemPrompt"))))
        }
        contents.add(Content(listOf(Part(text = prompt))))

        val request = GenerateContentRequest(contents)
        return try {
            val response = apiService.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "कोई परिणाम नहीं मिला। (No response received from Gemini.)"
        } catch (e: Exception) {
            Log.e(TAG, "Error invoking Gemini API", e)
            "एरर: ${e.localizedMessage}\n\n(कृपया अपना इंटरनेट कनेक्शन और API Key की जाँच करें।)"
        }
    }
}
