package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiClient
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatMessage(
    val message: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class VillageViewModel(application: Application) : AndroidViewModel(application) {

    private val db = VillageDatabase.getDatabase(application, viewModelScope)
    private val repository = VillageRepository(db.villageDao())

    // UI States observed reactively in Jetpack Compose
    val complaints: StateFlow<List<Complaint>> = repository.allComplaints
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val krishiRecords: StateFlow<List<KrishiRecord>> = repository.allKrishiRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val healthRecords: StateFlow<List<HealthRecord>> = repository.allHealthRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tanks: StateFlow<List<TankMonitoring>> = repository.allTanks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val energyGrids: StateFlow<List<EnergyGrid>> = repository.allEnergyGrids
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Live AI Chat State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                message = "राम-राम जी! 🙏 मैं हूँ विकास एआई (Vikas AI) - आपका ग्राम सहायक। " +
                        "आप मुझसे कृषि समस्याओं, सरकारी योजनाओं, स्वास्थ्य जानकारी, या शिकायत स्थिति के बारे में कुछ भी पूछ सकते हैं!",
                isUser = false
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    // Citizen Complaint Functions
    fun fileComplaint(type: String, description: String, location: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val complaint = Complaint(
                type = type,
                description = description,
                location = location,
                status = "Pending",
                comment = "दर्ज हुई। ग्राम पंचायत सचिव को प्रेषित की गई।"
            )
            repository.insertComplaint(complaint)
        }
    }

    fun updateComplaint(id: Int, status: String, comment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateComplaintStatus(id, status, comment)
        }
    }

    fun resolveComplaintSimulated(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val messages = listOf(
                "Resolved" to "समस्या का समाधान हुआ। लोक निर्माण दल (Plumbing/Electric) द्वारा कार्य पूरा किया गया।",
                "In Progress" to "कार्य आरंभ हुआ। तकनीकी कर्मचारी घटनास्थल पर कार्यरत हैं।"
            )
            val selected = messages.random()
            repository.updateComplaintStatus(id, selected.first, selected.second)
        }
    }

    // Krishi Record Functions
    fun addKrishiRecord(farmerName: String, cropType: String, soilN: Int, soilP: Int, soilK: Int, soilPh: Float, pest: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Generate customized diagnostics advice based on soil levels and target pest
            val advisory = StringBuilder()
            advisory.append("N:$soilN, P:$soilP, K:$soilK, pH:$soilPh. ")
            if (soilPh < 6.0f) {
                advisory.append("मिट्टी अम्लीय (Acidic) है। चूना (Lime) या जैविक खाद डालें। ")
            } else if (soilPh > 7.5f) {
                advisory.append("मिट्टी क्षारीय (Alkaline) है। जिप्सम या कम्पोस्ट का उपयोग उपयुक्त है। ")
            } else {
                advisory.append("मिट्टी का पीएच (pH) संतुलित है। ")
            }

            if (soilN < 40) {
            advisory.append("नाइट्रोजन स्तर कम है; यूरिया या वर्मीकम्पोस्ट डालें। ")
            }
            if (pest != "None" && pest.isNotEmpty()) {
                advisory.append("नीम का तेल (5% घोल) स्प्रे करें या कीटनाशक का उपयोग करें। ")
            } else {
                advisory.append("कोई कीट नहीं पाया गया। जैविक समृद्धि उत्कृष्ट है।")
            }

            val record = KrishiRecord(
                farmerName = farmerName,
                lotNumber = "Lot #${(10..99).random()}",
                cropType = cropType,
                soilN = soilN,
                soilP = soilP,
                soilK = soilK,
                soilPh = soilPh,
                pestDetected = pest.ifEmpty { "None" },
                recommendations = advisory.toString()
            )
            repository.insertKrishiRecord(record)
        }
    }

    // Health Record functions
    fun addPatientRecord(name: String, age: Int, symptoms: String, diagnosis: String, meds: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val record = HealthRecord(
                patientName = name,
                age = age,
                symptoms = symptoms,
                diagnosis = diagnosis,
                medsReminderJson = meds
            )
            repository.insertHealthRecord(record)
        }
    }

    // Water Pump Toggle simulation
    fun toggleWaterPump(tankId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            tanks.value.find { it.id == tankId }?.let { original ->
                val newPumpState = !original.isPumpRunning
                val newLevel = if (newPumpState) {
                    (original.percentageLevel + 12).coerceAtMost(100)
                } else {
                    original.percentageLevel
                }
                val updated = original.copy(
                    isPumpRunning = newPumpState,
                    percentageLevel = newLevel,
                    healthStatus = if (newPumpState) "Filling water..." else "Clean - Chlorine Added",
                    lastChecked = System.currentTimeMillis()
                )
                repository.insertTank(updated)
            }
        }
    }

    // Solar Streetlight simulated toggle
    fun toggleStreetlights(gridId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            energyGrids.value.find { it.id == gridId }?.let { original ->
                val originalLights = original.activeLights
                val updatedLights = if (originalLights > 0) 0 else (12..18).random()
                val updated = original.copy(
                    activeLights = updatedLights,
                    storageUsagePercentage = if (updatedLights > 0) {
                        (original.storageUsagePercentage - 8).coerceAtLeast(10)
                    } else {
                        (original.storageUsagePercentage + 14).coerceAtMost(100)
                    },
                    lastChecked = System.currentTimeMillis()
                )
                repository.insertEnergyGrid(updated)
            }
        }
    }

    // Send chat prompt to Gemini API
    fun sendChatMessage(phrase: String) {
        if (phrase.trim().isEmpty()) return

        // 1. Add user message
        val userMsg = ChatMessage(message = phrase, isUser = true)
        _chatMessages.update { it + userMsg }

        _isChatLoading.value = true

        viewModelScope.launch {
            val systemPrompt = "You are Vikas AI (विकास एआई), the friendly rural assistant of Gramexa One. " +
                    "Your personality is warm, informative, helpful, and deeply rooted in Indian village lifestyle and Gram Swaraj. " +
                    "You will respond in a very helpful bilingual format - Hindi (using actual Devanagari script) and English. " +
                    "If asked about crops, soils, weather, medicines, schemes, or village complaint status, answer with supportive " +
                    "rural practical insights. Use bullet points. Keep response concise, under 180 words, and encourage the farmer/citizen."

            // 2. Call Gemini Api
            val response = GeminiClient.askGemini(phrase, systemPrompt)

            // 3. Update chat history
            val aiMsg = ChatMessage(message = response, isUser = false)
            _chatMessages.update { it + aiMsg }
            _isChatLoading.value = false
        }
    }

    // Clear live chat messages for clean start
    fun clearChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                message = "राम-राम जी! 🙏 मैं विकास एआई हूँ। नया सत्र शुरू। कृषि, जल प्रबंधन, या नागरिक समस्याओं के बारे में प्रश्न पूछें!",
                isUser = false
            )
        )
    }
}
