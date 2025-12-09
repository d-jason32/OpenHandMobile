package com.example.openhandmobile

import android.Manifest
import android.view.ViewGroup
import android.view.Surface
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.openhandmobile.ui.theme.Raleway
import com.example.squares.Squares
import java.util.concurrent.Executors
import com.example.openhandmobile.InferenceWs
import com.example.openhandmobile.FrameSender
import android.util.Size


@Composable
fun ModelTest(nav: NavHostController, modifier: Modifier = Modifier) {

    Scaffold(
        containerColor = Color(0xFF1A1A1A),
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(nav) },


        ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {

            var selection by remember { mutableStateOf("letters") }

            // Pick server and mode based on selection (phrases use gestures model, no mode)
            val serverUrl = "ws://10.0.2.2:8000/ws" // always base; select model via message
            val initialMode = when (selection) {
                "letters" -> "letters"
                "numbers" -> "numbers"
                else -> null // gesture model ignores mode
            }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Mode chooser
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("letters" to "Letters", "numbers" to "Numbers", "phrases" to "Phrases").forEach { (key, label) ->
                        val selected = selection == key
                        OutlinedButton(
                            onClick = { selection = key },
                            border = BorderStroke(2.dp, if (selected) Color(0xFF00A6FF) else Color(0xFFFFFFFF)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = if (selected) Color(0xFF00A6FF) else Color.White
                            )
                        ) {
                            Text(label, fontSize = 14.sp)
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                CameraStreamScreen(
                        serverUrl = serverUrl,
                        initialMode = initialMode,
                        selectedModel = selection
                    )
                }

                Spacer(Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { nav.navigate("home") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    border = BorderStroke(2.dp, Color(0xFFFFFFFF)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Text(
                        "Go Back",
                        color = Color(0xFFFFFFFF),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
@Composable
fun CameraStreamScreen(
    serverUrl: String = "ws://10.0.2.2:8000/ws",
    initialMode: String? = null,
    selectedModel: String? = null,
    onPrediction: ((String, Float) -> Unit)? = null
) {
    val ctx = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    var label by remember { mutableStateOf<String>("…") }
    var prob by remember { mutableStateOf<Float>(0f) }

    // WebSocket
    val ws = remember(serverUrl, selectedModel) {
        InferenceWs(serverUrl) { l, p ->
            label = l
            prob = p
            onPrediction?.invoke(l, p)
        }.also { it.connect() }
    }
    LaunchedEffect(initialMode, selectedModel, ws) {
        when (selectedModel) {
            "phrases", "gestures" -> ws.setModel("gestures")
            "letters" -> {
                ws.setModel("letters")
                ws.setMode("letters")
            }
            "numbers" -> {
                ws.setModel("letters")
                ws.setMode("numbers")
            }
            null -> {
                // Only apply mode if provided; leave model as-is
                initialMode?.let { ws.setMode(it) }
            }
            else -> {
                ws.setModel(selectedModel)
                initialMode?.let { ws.setMode(it) }
            }
        }
    }
    DisposableEffect(ws) {
        onDispose { ws.close() }
    }

    // Permission
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* no-op */ }
    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    // Camera preview + analyzer
    var previewView: PreviewView? by remember { mutableStateOf(null) }
    LaunchedEffect(previewView) {
        val pv = previewView ?: return@LaunchedEffect
        val provider = ProcessCameraProvider.getInstance(ctx).get()
        provider.unbindAll()

        val rotation = pv.display?.rotation ?: Surface.ROTATION_0

        val preview = Preview.Builder()
            .setTargetRotation(rotation)
            .build().also {
            it.setSurfaceProvider(pv.surfaceProvider)
        }

        val analyzer = ImageAnalysis.Builder()
            .setTargetResolution(Size(640, 480))
            .setTargetRotation(rotation)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().also {
                it.setAnalyzer(cameraExecutor, FrameSender(ws) { ctx })
            }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        provider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, analyzer)
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Curved blue camera container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(Color(0xFF00A6FF))
        ) {
            // actual camera preview
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)),
                factory = { context ->
                    PreviewView(context).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    }.also { previewView = it }
                }
            )

            // Bottom overlay for prediction text
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color(0xFF000000).copy(alpha = 0.25f))
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$label  ${(prob * 100).toInt()}%",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Streaming to Python server… (show your hand to test)",
                        color = Color.White.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // Mode buttons removed; mode is set programmatically by callers (e.g., GradingScreen initialMode)
    }
}