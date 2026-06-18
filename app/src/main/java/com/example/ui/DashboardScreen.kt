package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.R
import com.example.data.*
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Screen enumeration representing clicked Pillar Hub detail views
enum class VillagePillar {
    CONTROL_CENTRE,
    KRISHI,
    CITIZEN,
    HEALTH,
    EDUCATION,
    SECURITY,
    JAL,
    ENERGY,
    LIVELIHOOD,
    AI_ASSISTANT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: VillageViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedPillar by remember { mutableStateOf<VillagePillar?>(null) }
    
    // Live broad-casted public announcement ticker text
    var announcementText by remember { mutableStateOf("किसान ध्यान दें: पंचायत भवन में कल सुबह 10 बजे मृदा परीक्षण शिविर (Soil testing camp) आयोजित किया जाएगा।") }

    // Obversable database flows
    val complaints by viewModel.complaints.collectAsState()
    val krishiRecords by viewModel.krishiRecords.collectAsState()
    val healthRecords by viewModel.healthRecords.collectAsState()
    val waterTanks by viewModel.tanks.collectAsState()
    val solarGrids by viewModel.energyGrids.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .statusBarsPadding()
                    .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Gramexa One",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            letterSpacing = (-0.5).sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Digital Gram Swaraj • Mandwa Village",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    // Profile Avatar style indicator JD
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD3E4FF))
                            .clickable { selectedPillar = VillagePillar.AI_ASSISTANT },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "JD",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF001D35)
                        )
                    }
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .navigationBarsPadding()
                    .padding(start = 24.dp, end = 24.dp, bottom = 12.dp, top = 4.dp)
            ) {
                // Interactive Ask Gramexa AI Capsule Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color(0xFFF3F4F9))
                        .border(1.dp, Color(0xFFE1E2E9), RoundedCornerShape(28.dp))
                        .clickable { selectedPillar = VillagePillar.AI_ASSISTANT }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF004A77)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✨",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                        Text(
                            text = "Ask Gramexa AI...",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF44474E)
                        )
                    }
                    
                    Text(
                        text = "VOICE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF004A77),
                        modifier = Modifier.padding(end = 16.dp),
                        letterSpacing = 1.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(14.dp))
                
                // Pure Minimalist Aesthetic Tab Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dashboard Item (Active Selected)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable { /* Already on dashboard */ }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 64.dp, height = 30.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFD3E4FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Dashboard,
                                contentDescription = "Dashboard",
                                tint = Color(0xFF001D35),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "Dashboard",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF001D35)
                        )
                    }
                    
                    // GIS Map Item (Disabled opacity)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .graphicsLayer(alpha = 0.5f)
                            .clickable { selectedPillar = VillagePillar.CONTROL_CENTRE }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Map,
                            contentDescription = "GIS Map",
                            tint = Color(0xFF1D1B20),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "GIS Map",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1D1B20)
                        )
                    }
                    
                    // Public Item (Disabled opacity)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .graphicsLayer(alpha = 0.5f)
                            .clickable { selectedPillar = VillagePillar.CITIZEN }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Campaign,
                            contentDescription = "Public",
                            tint = Color(0xFF1D1B20),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Public",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1D1B20)
                        )
                    }

                    // Settings Item (Disabled opacity)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .graphicsLayer(alpha = 0.5f)
                            .clickable { selectedPillar = VillagePillar.CONTROL_CENTRE }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = Color(0xFF1D1B20),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Settings",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1D1B20)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Command Center Status Ribbon from Mockup
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // System Normal Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFE8DEF8))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                    val alpha by infiniteTransition.animateFloat(
                        initialValue = 0.4f,
                        targetValue = 1.0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "alpha"
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .graphicsLayer(alpha = alpha)
                            .clip(CircleShape)
                            .background(Color(0xFF6750A4))
                    )
                    Text(
                        text = "System Normal",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF21005D)
                    )
                }

                // Temperature Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFC2E7FF))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "☀️ 32°C Sunny",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF001D35)
                    )
                }

                // AQI Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFD7E3BD))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🍃 AQI 45 (Good)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D1C16)
                    )
                }
            }

            // 1. Dashboard Header Banner with adaptive green/terracotta glassmorphism overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                // Background generated cinematic village hero image representation
                Image(
                    painter = painterResource(id = R.drawable.village_hero_banner_1781763823281),
                    contentDescription = "Smart Village Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Atmospheric dark forest overlay gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.82f))
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "ग्राम कल्याण डिजिटल मंच",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD54F) // Warm Marigold Amber
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Smart Village Operating System • आत्मनिर्भर भारत",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            // 2. Interactive Announcements marquee ticker tape
            AnnouncementTicker(announcement = announcementText)

            // 3. Quick Multi-Sector Realtime Status Indicators Row
            QuickIndicatorRow(
                activeComplaintsCount = complaints.filter { it.status == "Pending" }.size,
                waterTanks = waterTanks,
                solarGrids = solarGrids
            )

            // 4. Main Category Hub (10 Pillars grid) styled elegantly in a grid
            Text(
                text = "ग्राम सेवाएं और ऑपरेटिंग मॉड्यूल्स",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 12.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .height(550.dp)
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                userScrollEnabled = false // scrolling parent handles it
            ) {
                item {
                    HubItemCard(
                        title = "कंट्रोल सेंटर",
                        sub = "Digital Control",
                        icon = Icons.Filled.LiveTv,
                        badge = "CCTV • Live",
                        accentColor = MaterialTheme.colorScheme.primary,
                        testTag = "hub_control_centre",
                        onClick = { selectedPillar = VillagePillar.CONTROL_CENTRE }
                    )
                }
                item {
                    HubItemCard(
                        title = "ग्रेमेक्स कृषि",
                        sub = "Soil & AI Advisor",
                        icon = Icons.Filled.Agriculture,
                        badge = "Smart Farm",
                        accentColor = MaterialTheme.colorScheme.secondary,
                        testTag = "hub_krishi",
                        onClick = { selectedPillar = VillagePillar.KRISHI }
                    )
                }
                item {
                    HubItemCard(
                        title = "नागरिक सेवाएं",
                        sub = "Citizen Desk",
                        icon = Icons.Filled.RateReview,
                        badge = "${complaints.size} Grievance",
                        accentColor = MaterialTheme.colorScheme.tertiary,
                        testTag = "hub_citizen",
                        onClick = { selectedPillar = VillagePillar.CITIZEN }
                    )
                }
                item {
                    HubItemCard(
                        title = "ग्रेमेक्स स्वास्थ्य",
                        sub = "AI Telemedicine",
                        icon = Icons.Filled.LocalHospital,
                        badge = "Meds Alert",
                        accentColor = Color(0xFFC62828),
                        testTag = "hub_health",
                        onClick = { selectedPillar = VillagePillar.HEALTH }
                    )
                }
                item {
                    HubItemCard(
                        title = "ग्रेमेक्स शिक्षा",
                        sub = "Digital Pathshala",
                        icon = Icons.Filled.School,
                        badge = "Coding & AI",
                        accentColor = Color(0xFF1565C0),
                        testTag = "hub_education",
                        onClick = { selectedPillar = VillagePillar.EDUCATION }
                    )
                }
                item {
                    HubItemCard(
                        title = "सुरक्षा चौकसी",
                        sub = "Emergency SOS",
                        icon = Icons.Filled.Shield,
                        badge = "AI CCTV",
                        accentColor = Color(0xFFE65100),
                        testTag = "hub_security",
                        onClick = { selectedPillar = VillagePillar.SECURITY }
                    )
                }
                item {
                    HubItemCard(
                        title = "ग्रेमेक्स जल",
                        sub = "Water Grid",
                        icon = Icons.Filled.Opacity,
                        badge = if (waterTanks.any { it.isPumpRunning }) "Pumps Active" else "Stable",
                        accentColor = Color(0xFF00838F),
                        testTag = "hub_jal",
                        onClick = { selectedPillar = VillagePillar.JAL }
                    )
                }
                item {
                    HubItemCard(
                        title = "सौर ऊर्जा",
                        sub = "Solar Microgrid",
                        icon = Icons.Filled.WbSunny,
                        badge = "${solarGrids.sumOf { it.generationKW.toInt() }} kW Generated",
                        accentColor = Color(0xFFF9A825),
                        testTag = "hub_energy",
                        onClick = { selectedPillar = VillagePillar.ENERGY }
                    )
                }
                item {
                    HubItemCard(
                        title = "महिला सुदृढ़ीकरण",
                        sub = "SHG Marketplace",
                        icon = Icons.Filled.Storefront,
                        badge = "Hathkargha",
                        accentColor = Color(0xFFAD1457),
                        testTag = "hub_livelihood",
                        onClick = { selectedPillar = VillagePillar.LIVELIHOOD }
                    )
                }
                item {
                    HubItemCard(
                        title = "विकास AI",
                        sub = "Gemini Assistant",
                        icon = Icons.Filled.SmartToy,
                        badge = "Bilingual",
                        accentColor = MaterialTheme.colorScheme.primary,
                        testTag = "hub_ai_assistant",
                        onClick = { selectedPillar = VillagePillar.AI_ASSISTANT }
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Modal dialogue handlers representing active module scopes
    val activePillar = selectedPillar
    if (activePillar != null) {
        Dialog(
            onDismissRequest = { selectedPillar = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Modal Header
                    TopAppBar(
                        title = {
                            Text(
                                text = when (activePillar) {
                                    VillagePillar.CONTROL_CENTRE -> "कंट्रोल सेंटर (Control Centre)"
                                    VillagePillar.KRISHI -> "ग्रेमेक्स कृषि (Smart Agriculture)"
                                    VillagePillar.CITIZEN -> "नागरिक शिकायत समाधान (Grievances)"
                                    VillagePillar.HEALTH -> "ग्रेमेक्स टेलीमेडिसिन और स्वास्थ्य"
                                    VillagePillar.EDUCATION -> "डिजिटल पाठशाला (E-Learning)"
                                    VillagePillar.SECURITY -> "सुरक्षा और SOS विंग"
                                    VillagePillar.JAL -> "जल ग्रिड प्रबंधन (Jal Grid)"
                                    VillagePillar.ENERGY -> "सौर ग्रिड मॉनिटरिंग (Solar)"
                                    VillagePillar.LIVELIHOOD -> "लघु उद्योग और स्वंय सहायता समूह"
                                    VillagePillar.AI_ASSISTANT -> "विकास एआई ग्राम सहायक"
                                },
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { selectedPillar = null }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "मुख्य पृष्ठ"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )

                    // Render matching modal contents beautifully
                    Box(modifier = Modifier.fillMaxSize()) {
                        when (activePillar) {
                            VillagePillar.CONTROL_CENTRE -> ControlCentreWidget(
                                currentAnnounce = announcementText,
                                onUpdateAnnounce = { announcementText = it }
                            )
                            VillagePillar.KRISHI -> KrishiWidget(
                                records = krishiRecords,
                                onAddRecord = { n, c, sn, sp, sk, ph, pst ->
                                    viewModel.addKrishiRecord(n, c, sn, sp, sk, ph, pst)
                                }
                            )
                            VillagePillar.CITIZEN -> CitizenWidget(
                                complaints = complaints,
                                onSubmitGrievance = { t, d, l -> viewModel.fileComplaint(t, d, l) },
                                onSimulateFix = { id -> viewModel.resolveComplaintSimulated(id) }
                            )
                            VillagePillar.HEALTH -> HealthWidget(
                                records = healthRecords,
                                onAddPatient = { n, a, sy, dg, md ->
                                    viewModel.addPatientRecord(n, a, sy, dg, md)
                                }
                            )
                            VillagePillar.EDUCATION -> EducationWidget()
                            VillagePillar.SECURITY -> SecurityWidget()
                            VillagePillar.JAL -> JalWidget(
                                tanks = waterTanks,
                                onPumpToggle = { id -> viewModel.toggleWaterPump(id) }
                            )
                            VillagePillar.ENERGY -> EnergyWidget(
                                grids = solarGrids,
                                onToggleLights = { id -> viewModel.toggleStreetlights(id) }
                            )
                            VillagePillar.LIVELIHOOD -> LivelihoodWidget()
                            VillagePillar.AI_ASSISTANT -> AiAssistantWidget(
                                chatMessages = viewModel.chatMessages.collectAsState().value,
                                isLoading = viewModel.isChatLoading.collectAsState().value,
                                onSendMessage = { viewModel.sendChatMessage(it) },
                                onClear = { viewModel.clearChat() }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------------- SHARED DASHBOARD SUB-COMPONENTS ----------------------

@Composable
fun AnnouncementTicker(announcement: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Campaign,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .size(26.dp)
                    .animateContentSize()
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "पंचायत घोषणा (Public Announcement):",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = announcement,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
fun QuickIndicatorRow(
    activeComplaintsCount: Int,
    waterTanks: List<TankMonitoring>,
    solarGrids: List<EnergyGrid>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val averageLevel = if (waterTanks.isNotEmpty()) {
            waterTanks.sumOf { it.percentageLevel } / waterTanks.size
        } else 65

        val totalSolarKW = solarGrids.sumOf { it.generationKW.toDouble() }

        IndicatorCard(
            title = "सुलझाव आवश्यक",
            valText = "$activeComplaintsCount शिकायतें",
            accent = MaterialTheme.colorScheme.tertiary,
            icon = Icons.Filled.Warning,
            modifier = Modifier.weight(1f)
        )
        IndicatorCard(
            title = "ग्राम जल स्तर",
            valText = "$averageLevel% संचित",
            accent = Color(0xFF006064),
            icon = Icons.Filled.WaterDrop,
            modifier = Modifier.weight(1f)
        )
        IndicatorCard(
            title = "सौर विद्युत जनरेशन",
            valText = String.format("%.1f kW", totalSolarKW),
            accent = Color(0xFFF57F17),
            icon = Icons.Filled.SolarPower,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun IndicatorCard(
    title: String,
    valText: String,
    accent: Color,
    imageVector: androidx.compose.ui.graphics.vector.ImageVector? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.61f)
            )
            Text(
                text = valText,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun HubItemCard(
    title: String,
    sub: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    badge: String,
    accentColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = sub,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .background(accentColor.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = badge,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }
        }
    }
}

// ---------------------- 1. CONTROL CENTRE WORKSPACE ----------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlCentreWidget(
    currentAnnounce: String,
    onUpdateAnnounce: (String) -> Unit
) {
    var editAnnouncement by remember { mutableStateOf(currentAnnounce) }
    var selectedCam by remember { mutableStateOf("चौपाल मुख्य चौक (Chopal Square)") }
    var isRecording by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "डिजिटल कमांड सेंटर (Digital Command Center)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Live CCTV Simulator Panel
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black)
            ) {
                Column {
                    // Scanning visual container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            // Draw ambient technical nodes lines
                            drawRect(color = Color(0xFF071F11))
                        }
                        
                        // Fake stream text
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(if (isRecording) Color.Red else Color(0xFF00FF00))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isRecording) "REC • FEED LIVE" else "FEED LIVE",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = selectedCam,
                                color = Color(0xFF4AF2A1),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Analytics: AI Missing Person Scan ACTIVE • High Flow",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp
                            )
                        }

                        // Watermark timestamp
                        Text(
                            text = SimpleDateFormat("HH:mm:ss yyyy-MM-dd", Locale.getDefault()).format(Date()),
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                        )
                    }

                    // CCTV Channel Selector Swiper
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.DarkGray)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val cams = listOf(
                            "चौपाल मुख्य चौक (Chopal Square)",
                            "कृषि फार्म मार्ग 1 (Krishi Farm Row 1)",
                            "सोलर माइक्रोग्रिड हब (Solar Station)"
                        )
                        cams.forEach { cam ->
                            Button(
                                onClick = { selectedCam = cam },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedCam == cam) MaterialTheme.colorScheme.primary else Color.Gray,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(30.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = cam.substringBefore(" "), fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // Live Public Announcement Management Desk
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "पब्लिक लाउडस्पीकर उद्घोषणा (PA Broadcasting Desk)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = editAnnouncement,
                        onValueChange = { editAnnouncement = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("उद्घोषणा का विवरण (Announcement text)") },
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            onUpdateAnnounce(editAnnouncement)
                        },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Filled.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("सारे ग्राम में लाउडस्पीकर पर ब्रॉडकास्ट करें")
                    }
                }
            }
        }

        // Village General Analytics Summary Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ग्राम सांख्यिकी रिकॉर्ड (Village Master Records)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    VillageRecordRow(label = "कुल जनसंख्या (Total Population)", valStr = "1,420")
                    VillageRecordRow(label = "कृषि भूमि (Total Arable Land)", valStr = "280 हेक्टेयर (hectares)")
                    VillageRecordRow(label = "सौर स्ट्रीटलाइट कार्यशील (Solar Streetlights)", valStr = "22 / 24 कार्यशील")
                    VillageRecordRow(label = "स्वयं सहायता समूह (Active SHG Guilds)", valStr = "8 कार्यरत टीमें")
                    VillageRecordRow(label = "जल कनेक्शन (Tap Connections)", valStr = "100% परिवारों में")
                }
            }
        }
    }
}

@Composable
fun VillageRecordRow(label: String, valStr: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        Text(text = valStr, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}

// ---------------------- 2. SMART KRISHI (FARM ADVISORY) ----------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KrishiWidget(
    records: List<KrishiRecord>,
    onAddRecord: (String, String, Int, Int, Int, Float, String) -> Unit
) {
    var farmerName by remember { mutableStateOf("") }
    var cropType by remember { mutableStateOf("Wheat (गेहूँ)") }
    var soilN by remember { mutableStateOf("50") }
    var soilP by remember { mutableStateOf("30") }
    var soilK by remember { mutableStateOf("35") }
    var soilPh by remember { mutableStateOf("6.5") }
    var pestDetected by remember { mutableStateOf("None") }

    var expandedForm by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "मृदा परीक्षण एवं एआई कृषि सलाहकार",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(
                    onClick = { expandedForm = !expandedForm },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (expandedForm) Color.Gray else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(if (expandedForm) "Form बंद करें" else "नया कार्ड जोड़ें")
                }
            }
        }

        // Collapsible Farm Soil Card Addition Form
        if (expandedForm) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "नया मृदा परीक्षण रिपोर्ट दर्ज करें (Register Soil Card)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        OutlinedTextField(
                            value = farmerName,
                            onValueChange = { farmerName = it },
                            label = { Text("किसान का नाम (Farmer Name)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Crop selection dropdown mockup
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val crops = listOf("Wheat (गेहूँ)", "Mustard (सरसों)", "Rice (धान)", "Sugarcane (गन्ना)")
                            crops.forEach { crop ->
                                AssistChip(
                                    onClick = { cropType = crop },
                                    label = { Text(crop.substringBefore(" ")) },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = if (cropType == crop) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent
                                    )
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = soilN,
                                onValueChange = { soilN = it },
                                label = { Text("N") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = soilP,
                                onValueChange = { soilP = it },
                                label = { Text("P") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = soilK,
                                onValueChange = { soilK = it },
                                label = { Text("K") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = soilPh,
                                onValueChange = { soilPh = it },
                                label = { Text("pH") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1.2f)
                            )
                        }

                        OutlinedTextField(
                            value = pestDetected,
                            onValueChange = { pestDetected = it },
                            label = { Text("पहचाना गया कीट (Pest Detected: None/Aphids/Rust)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (farmerName.trim().isNotEmpty()) {
                                    onAddRecord(
                                        farmerName,
                                        cropType,
                                        soilN.toIntOrNull() ?: 40,
                                        soilP.toIntOrNull() ?: 30,
                                        soilK.toIntOrNull() ?: 30,
                                        soilPh.toFloatOrNull() ?: 6.5f,
                                        pestDetected
                                    )
                                    farmerName = ""
                                    pestDetected = "None"
                                    expandedForm = false
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("रिपोर्ट सेव करें और एआई सुझाव बनाएं")
                        }
                    }
                }
            }
        }

        // Soil Reports History Header
        item {
            Text(
                text = "ग्राम मृदा स्वास्थ्य रजिस्टर (Soil Health Cards)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (records.isEmpty()) {
            item {
                Text(
                    text = "कोई मृदा रिपोर्ट मौजूद नहीं है। जोड़ने के लिए ऊपर बटन दबाएं।",
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp)
                )
            }
        }

        items(records) { record ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = record.farmerName,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "फसल: ${record.cropType}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "सर्वे नंबर: ${record.lotNumber}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        SoilMetricElem(label = "नाइट्रोजन (N)", value = "${record.soilN} mg/kg")
                        SoilMetricElem(label = "फास्फोरस (P)", value = "${record.soilP} mg/kg")
                        SoilMetricElem(label = "पोटैशियम (K)", value = "${record.soilK} mg/kg")
                        SoilMetricElem(label = "पीएच (pH)", value = "${record.soilPh}")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.BugReport,
                            contentDescription = null,
                            tint = if (record.pestDetected != "None") Color(0xFFC62828) else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "कीट पहचान: " + if(record.pestDetected != "None") record.pestDetected else "कोई कीट दोष नहीं",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (record.pestDetected != "None") Color(0xFFC62828) else Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.07f), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Psychology,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "एआई विकास सलाह (AI Smart Advisory):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = record.recommendations,
                                fontSize = 12.sp,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SoilMetricElem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 10.sp, color = Color.Gray)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

// ---------------------- 3. CITIZEN WIDGET (GRIEVANCE DESK) ----------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitizenWidget(
    complaints: List<Complaint>,
    onSubmitGrievance: (String, String, String) -> Unit,
    onSimulateFix: (Int) -> Unit
) {
    var desc by remember { mutableStateOf("") }
    var loc by remember { mutableStateOf("") }
    var catType by remember { mutableStateOf("Electricity (बिजली समस्या)") }

    var openForm by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "पंचायत जन-शिकायत निवारण",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(
                    onClick = { openForm = !openForm },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (openForm) Color.Gray else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(if (openForm) "रद्द करें" else "नई शिकायत दर्ज करें")
                }
            }
        }

        // Submitting a public grievance form
        if (openForm) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "शिकायत पंजीकरण आवेदन (Grievance Form)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // Selector
                        Text(text = "समस्या श्रेणी (Grievance Category):", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        val types = listOf("Electricity", "Roads", "Water Supply", "Panchayat Scheme")
                        Row(
                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            types.forEach { t ->
                                ChoiceChip(
                                    selected = catType == t,
                                    label = t,
                                    onClick = { catType = t }
                                )
                            }
                        }

                        OutlinedTextField(
                            value = desc,
                            onValueChange = { desc = it },
                            label = { Text("शिकायत का पूर्ण विवरण (Describe the issue)") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )

                        OutlinedTextField(
                            value = loc,
                            onValueChange = { loc = it },
                            label = { Text("स्थान / वार्ड नंबर (Location / Ward No)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (desc.trim().isNotEmpty() && loc.trim().isNotEmpty()) {
                                    onSubmitGrievance(catType, desc, loc)
                                    desc = ""
                                    loc = ""
                                    openForm = false
                                }
                            },
                            modifier = Modifier.align(Alignment.End),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("पंजीकृत करें")
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "पंजीकृत शिकायतों की वर्तमान स्थिति (Grievance Status Desk)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        if (complaints.isEmpty()) {
            item {
                Text(
                    text = "कोई शिकायत मौजूद नहीं है।",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp)
                )
            }
        }

        items(complaints) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.type,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        // Status badge colored dynamically
                        val statusCol = when(item.status) {
                            "Resolved" -> Color(0xFF2E7D32)
                            "In Progress" -> Color(0xFF1565C0)
                            else -> Color(0xFFEF6C00)
                        }

                        Box(
                            modifier = Modifier
                                .background(statusCol.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = item.status,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = statusCol
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "स्थान: ${item.location}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.description,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = Color.LightGray.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Gray.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "ग्राम सचिव की टिपण्णी:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color.DarkGray
                        )
                        Text(
                            text = item.comment,
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Simulated technician resolution triggers in preview!
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { onSimulateFix(item.id) },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.Build, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("स्थिति अद्यतन (Simulate Resolution)", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChoiceChip(
    selected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.15f))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) Color.White else Color.Black
        )
    }
}

// ---------------------- 4. HEALTH DESK widget ----------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthWidget(
    records: List<HealthRecord>,
    onAddPatient: (String, Int, String, String, String) -> Unit
) {
    var patientName by remember { mutableStateOf("") }
    var patientAge by remember { mutableStateOf("") }
    var symptoms by remember { mutableStateOf("") }
    var diagnosis by remember { mutableStateOf("") }
    var medsDesc by remember { mutableStateOf("") }

    var showForm by remember { mutableStateOf(false) }

    // Interacting checklists for medicine compliance
    val medsReminders = remember {
        mutableStateListOf(
            "Lilavati Bai: Ayush Kwath - सुबह 7:00 बजे" to false,
            "Ramesh Kumar: B-Complex Capsule - दोपहर 1:30 बजे" to false,
            "Savitri Devi: Multivitamin Syrup - रात 8:00 बजे" to false
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "टेलीमेडिसिन एवं स्वास्थ्य निगरानी",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC62828)
                )
                Button(
                    onClick = { showForm = !showForm },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                ) {
                    Text(if (showForm) "बंद करें" else "नया मरीज एंट्री")
                }
            }
        }

        if (showForm) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "नया चिकित्सा पंजीकरण ब्यौरा दर्ज करें",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = patientName,
                                onValueChange = { patientName = it },
                                label = { Text("मरीज का नाम (Name)") },
                                modifier = Modifier.weight(2.5f)
                            )
                            OutlinedTextField(
                                value = patientAge,
                                onValueChange = { patientAge = it },
                                label = { Text("उम्र (Age)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        OutlinedTextField(
                            value = symptoms,
                            onValueChange = { symptoms = it },
                            label = { Text("लक्षण (Symptoms: Fever/Cough)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = diagnosis,
                            onValueChange = { diagnosis = it },
                            label = { Text("चिकित्सा परामर्श / बीमारी (Diagnosis)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = medsDesc,
                            onValueChange = { medsDesc = it },
                            label = { Text("दवाई एवं समय ब्यौरा (Medications slot)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (patientName.trim().isNotEmpty()) {
                                    onAddPatient(
                                        patientName,
                                        patientAge.toIntOrNull() ?: 45,
                                        symptoms,
                                        diagnosis,
                                        medsDesc
                                    )
                                    patientName = ""
                                    patientAge = ""
                                    symptoms = ""
                                    diagnosis = ""
                                    medsDesc = ""
                                    showForm = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("दर्ज करें")
                        }
                    }
                }
            }
        }

        // Compliant Checklist Desk (Medicine reminder tracker)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Filled.NotificationsActive, contentDescription = null, tint = Color(0xFFC62828))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "दवाई स्लॉट अलर्ट (Daily Medicine Reminders - Taken Tracker)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFFC62828)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    medsReminders.forEachIndexed { i, pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = pair.second,
                                onCheckedChange = { isChecked ->
                                    medsReminders[i] = pair.first to isChecked
                                }
                            )
                            Text(
                                text = pair.first,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (pair.second) Color.Gray else Color.Black,
                                style = if (pair.second) MaterialTheme.typography.bodyMedium.copy(
                                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                ) else MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "ग्राम आरोग्य रजिस्टर (Argya Health Records)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(records) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${item.patientName} (${item.age} वर्ष)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFFC62828)
                        )
                        Text(
                            text = "पंजीकृत: " + SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(item.timestamp)),
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "लक्षण / समस्या: ${item.symptoms}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "बीमारी निदान: ${item.diagnosis}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = "निर्धारित दवाईयां (Meds Prescription):",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            Text(
                                text = item.medsReminderJson,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------------- 5. DIGITAL EDUCATION PATHSHALA ----------------------
@Composable
fun EducationWidget() {
    var expandedCourse by remember { mutableStateOf<String?>(null) }

    val courses = listOf(
        Triple(
            "Agritech Basics & Organic Cultivation 🌱",
            "सीखें जैविक खेती के तरीके, फसल चक्र (crop cycle) और केंचुआ खाद जैविक निर्माण तकनीक।",
            listOf(
                "अध्याय 1: जैविक खाद की अवधारणा (Concept of Organic Compost) - 14 Mins",
                "अध्याय 2: ड्रिप सिंचाई होम-प्रणाली (DIY Drip Irrigation setup) - 22 Mins",
                "अध्याय 3: फसलों में जैविक कीट-बचाव रोग निवारण - 18 Mins"
            )
        ),
        Triple(
            "Coding and Robotics Academy for Kids 🤖",
            "ग्रामीण बच्चों के लिए कोडिंग और रोबोटिक्स का बुनियादी ज्ञान। स्क्रैच ब्लॉक-कोडिंग की शुरुआत।",
            listOf(
                "Lesson 1: Introduction to Block Languages (Scratch) - 15 Mins",
                "Lesson 2: Simple Loops and Logic Blocks - 25 Mins",
                "Lesson 3: Connecting Sensors to Arduino units - 30 Mins"
            )
        ),
        Triple(
            "Financial Literacy & Digital Payments 💳",
            "किसान क्रेडिट कार्ड, बैंक ट्रांसफर सुविधा, तथा यूपीआई पेमेंट्स धोखाधड़ी से बचाव के गुर।",
            listOf(
                "अध्याय 1: प्रधानमंत्री सम्मान निधि में अपना खाता जोड़ें - 10 Mins",
                "अध्याय 2: सुरक्षित यूपीआई डिजिटल लेन-देन कैसे करें - 15 Mins",
                "अध्याय 3: स्वयं सहायता समूह के ब्याज हिसाब-किताब जर्नल - 20 Mins"
            )
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "ग्रेमेक्स डिजिटल पाठशाला (E-Learning Pathshala)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "ग्राम वासियों और बच्चों के लिए कौशल विकास एवं मुफ़्त डिजिटल पुस्तकालय",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        items(courses) { course ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expandedCourse = if (expandedCourse == course.first) null else course.first
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = course.first,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1565C0),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (expandedCourse == course.first) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = null,
                            tint = Color(0xFF1565C0)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = course.second,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    // Expand chapters
                    AnimatedVisibility(visible = expandedCourse == course.first) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                                .background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "पाठ्यक्रम की कक्षाएं (Active Chapters):",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color(0xFF1565C0)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            course.third.forEach { chapter ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.PlayCircleFilled,
                                        contentDescription = null,
                                        tint = Color(0xFF1565C0),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = chapter, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------------- 6. VILLAGE SECURITY & SOS WIDGET ----------------------
@Composable
fun SecurityWidget() {
    var emergencyTriggered by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(3) }

    LaunchedEffect(emergencyTriggered) {
        if (emergencyTriggered && countdown > 0) {
            delay(1000)
            countdown--
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "मृदा सुरक्षा एवं आपातकालीन आपदा चौकसी",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE65100),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Emergency SOS Circle Key Catalyst
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0B2)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "आपातकाल संकट अलार्म (EMERGENCY SOS)",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFD84315),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "दबाने पर पुलिस, प्रधान तथा पंचायत सचिव को जीपीएस लोकेशन प्रेषित हो जाएगी",
                        fontSize = 11.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = if (emergencyTriggered) listOf(Color.Red, Color(0xFF880E4F))
                                    else listOf(Color(0xFFE65100), Color(0xFFBF360C))
                                )
                            )
                            .clickable {
                                emergencyTriggered = true
                                countdown = 3
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Filled.Emergency,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(34.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (emergencyTriggered && countdown > 0) "WAITING $countdown"
                                else if (emergencyTriggered) "SOS FIRED!"
                                else "TAP & HOLD",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    if (emergencyTriggered) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (countdown > 0) "संकट अलर्ट 3 सेकंड में फायर हो रहा है..."
                            else "सफलतापूर्वक फायर किया गया! जीपीएस: 26.85°N, 80.94°E दर्ज हुआ।",
                            color = Color(0xFFBF360C),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = { emergencyTriggered = false }
                        ) {
                            Text("अलर्ट रद्द करें (Deactivate SOS)")
                        }
                    }
                }
            }
        }

        // Active Alert Tracker Panel
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ग्राम एलर्ट बुलेटिन (Alert Warnings Tracker)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFFD84315)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    ActiveAlertItem(
                        title = "[खोया हुआ बालक सन्देश] - समाधान अपडेट",
                        desc = "दिनांक 16 जून को खोया हुआ बालक सुरेश कुमार का 8 वर्षीय पुत्र 'अमन' समीप खण्ड से सुरक्षित ढूंढ कर पंचायत लाया गया।"
                    )
                    ActiveAlertItem(
                        title = "[घाघरा नदी जल स्तर]",
                        desc = "पहाड़ी नदी घाघरा के जल स्तर में वृद्धि। वर्तमान स्तर खतरे के स्तर से 2 मीटर नीचे। घबराने की आवश्यक्ता नहीं है।"
                    )
                }
            }
        }
    }
}

@Composable
fun ActiveAlertItem(title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = null,
            tint = Color(0xFFD84315),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = desc, fontSize = 12.sp, color = Color.DarkGray)
        }
    }
}

// ---------------------- 7. JAL GRID WATER SYSTEM ----------------------
@Composable
fun JalWidget(
    tanks: List<TankMonitoring>,
    onPumpToggle: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "ग्रेमेक्स जल ग्रिड (Water Management System)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00838F)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "ग्राम पेय पेयजल आपूर्ति, टैंक भराव तथा सिंचाई पंप सेंसर नियंत्रण",
                fontSize = 11.sp,
                color = Color.Gray
            )
        }

        items(tanks) { tank ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tank.tankName,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = Color(0xFF00838F)
                        )
                        
                        Box(
                            modifier = Modifier
                                .background(if (tank.isPumpRunning) Color(0xFFE0F7FA) else Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (tank.isPumpRunning) "भर रहा है (PUMP ON)" else "OFF",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (tank.isPumpRunning) Color(0xFF00838F) else Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Liquid level slider metrics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Filled.WaterDrop, contentDescription = null, tint = Color(0xFF00838F), modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "टैंक जल स्तर: ${tank.percentageLevel}%", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(text = "10,000 लीटर कैपेसिटी", fontSize = 11.sp, color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = tank.percentageLevel.toFloat() / 100f,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = Color(0xFF00BCD4),
                                trackColor = Color(0xFFE0F7FA)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "स्वच्छता स्तर: ${tank.healthStatus}",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "अंतिम जांच: " + SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(tank.lastChecked)),
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "सिंचाई हेतु ऑपरेटर पंप स्विच",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Button(
                            onClick = { onPumpToggle(tank.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (tank.isPumpRunning) Color.Red else Color(0xFF00838F)
                            )
                        ) {
                            Text(if (tank.isPumpRunning) "पंप बंद करें (STOP)" else "पंप चालू करें (START)")
                        }
                    }
                }
            }
        }
    }
}

// ---------------------- 8. SOLAR MICROGRID & SMART ENERGY ----------------------
@Composable
fun EnergyWidget(
    grids: List<EnergyGrid>,
    onToggleLights: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "सौर माइक्रोग्रिड प्रबंधन (Solar Microgrid Monitoring)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF9A825)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "ग्राम सोलर पैनल उत्पादन ग्रिड, पावर बैकअप तथा स्मार्ट स्ट्रीटलाइट ग्रिड",
                fontSize = 11.sp,
                color = Color.Gray
            )
        }

        items(grids) { grid ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = grid.sectionName,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = Color(0xFFF9A825)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("करंट जनरेशन (Power Output)", fontSize = 11.sp, color = Color.Gray)
                            Text("${grid.generationKW} kW", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF57F17))
                        }
                        Column {
                            Text("स्टोरेज बैटरी चार्ज", fontSize = 11.sp, color = Color.Gray)
                            Text("${grid.storageUsagePercentage}%", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        }
                        Column {
                            Text("सक्रिय स्ट्रीटलाइट्स", fontSize = 11.sp, color = Color.Gray)
                            Text("${grid.activeLights} बत्तियां ON", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "आटोमेटिक स्ट्रीटलाइट टॉगल",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Switch(
                            checked = grid.activeLights > 0,
                            onCheckedChange = { onToggleLights(grid.id) }
                        )
                    }
                }
            }
        }
    }
}

// ---------------------- 9. LIVELIHOOD WOMEN SHG MARKETPLACE ----------------------
@Composable
fun LivelihoodWidget() {
    var checkoutProduct by remember { mutableStateOf<String?>(null) }
    var userTokens by remember { mutableStateOf(500) }

    val products = listOf(
        Triple("हस्तनिर्मित मिटटी के दीये (Diya Set)", "₹120 • आंचल SHG द्वारा", 120),
        Triple("शुद्ध कोल्ड-प्रेस्ड सरसों तेल (Mustard Oil)", "₹210 / लीटर • अन्नपूर्णा SHG", 210),
        Triple("हस्तशिल्प जूट फैला (Jute Basket)", "₹180 • सरगम लघु उद्योग", 180),
        Triple("घर का बना शुद्ध आम आचार (Mango Pickle)", "₹150 / 500g • नारी शक्ति", 150)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ग्राम कुटीर उद्योग एवं हाट बाजार",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFAD1457)
                    )
                    Text(
                        text = "स्वयं सहायता समूह (SHG) की दीदियों द्वारा निर्मित शुद्ध प्रामाणिक सामग्री",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFFFCE4EC), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text("ग्राम वॉलेट: ₹$userTokens", fontWeight = FontWeight.Bold, color = Color(0xFFAD1457))
                }
            }
        }

        items(products) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.first, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(text = item.second, fontSize = 12.sp, color = Color.Gray)
                    }
                    Button(
                        onClick = {
                            if (userTokens >= item.third) {
                                checkoutProduct = item.first
                                userTokens -= item.third
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAD1457))
                    ) {
                        Text("खरीदें (Buy)")
                    }
                }
            }
        }
    }

    checkoutProduct?.let { prod ->
        AlertDialog(
            onDismissRequest = { checkoutProduct = null },
            title = { Text("अंचल ग्रामीण हाट धन्यवाद (Purchase Complete)") },
            text = { Text("दीदी स्वयं सहायता समूह की ओर से आपको धन्यवाद! आपने सफलतापूर्वक '$prod' खरीद लिया है। आपकी सहायता महिला सशक्तिकरण में अमूल्य योगदान देती है।") },
            confirmButton = {
                Button(onClick = { checkoutProduct = null }) {
                    Text("राम-राम (OK)")
                }
            }
        )
    }
}

// ---------------------- 10. VIKAS AI CHATBOT (GEMINI API) ----------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantWidget(
    chatMessages: List<ChatMessage>,
    isLoading: Boolean,
    onSendMessage: (String) -> Unit,
    onClear: () -> Unit
) {
    var queryText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        // Chat Header with Quick Action Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "विकास ग्राम एआई सहायक (Gemini 3.5 Flash)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            TextButton(onClick = { onClear() }) {
                Text("चैट साफ़ करें")
            }
        }

        // Quick prompts suggestions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val suggestions = listOf(
                "आज मौसम कैसा रहेगा?",
                "गेहूँ में कौन सी खाद डालें?",
                "पीएम किसान योजना के लाभार्थी पात्रता?",
                "ग्रेमेक्स ऑपरेटिंग सिस्टम में शिकायत ब्यौरा?"
            )
            suggestions.forEach { sug ->
                AssistChip(
                    onClick = { onSendMessage(sug) },
                    label = { Text(sug, fontSize = 11.sp) }
                )
            }
        }

        // Chat conversation body
        val chatScrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(chatScrollState)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                chatMessages.forEach { msg ->
                    ChatBubble(message = msg)
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }
            }

            // Scroll helper
            LaunchedEffect(chatMessages.size, isLoading) {
                chatScrollState.animateScrollTo(chatScrollState.maxValue)
            }
        }

        // Input bottom bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = queryText,
                onValueChange = { queryText = it },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_text"),
                placeholder = { Text("सवाल पूछें... (Ask a question)") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (queryText.trim().isNotEmpty()) {
                            onSendMessage(queryText)
                            queryText = ""
                            keyboardController?.hide()
                        }
                    }
                )
            )
            IconButton(
                onClick = {
                    if (queryText.trim().isNotEmpty()) {
                        onSendMessage(queryText)
                        queryText = ""
                        keyboardController?.hide()
                    }
                },
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .testTag("chat_send_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "पूछें",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val bubbleColor = if (message.isUser) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    else Color(0xFFE8F5E9)

    val align = if (message.isUser) Alignment.End else Alignment.Start

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = align
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 290.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isUser) 16.dp else 4.dp,
                        bottomEnd = if (message.isUser) 4.dp else 16.dp
                    )
                )
                .background(bubbleColor)
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = if (message.isUser) "आप (You):" else "विकास AI (Gram Assistant):",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp,
                    color = if (message.isUser) MaterialTheme.colorScheme.primary else Color(0xFF1B5E20)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message.message,
                    fontSize = 13.sp,
                    color = Color.Black
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(message.timestamp)),
            fontSize = 9.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
