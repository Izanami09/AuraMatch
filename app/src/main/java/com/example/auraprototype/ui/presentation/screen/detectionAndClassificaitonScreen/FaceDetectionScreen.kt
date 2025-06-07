package com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.example.auraprototype.R
import com.example.auraprototype.ui.theme.BentosFontFamily
import com.example.auraprototype.ui.theme.SuccessColor


import kotlinx.coroutines.launch

private const val OVAL_WIDTH_DP = 250
private const val OVAL_HEIGHT_DP = 300
private const val OVAL_LEFT_OFFSET_RATIO = 2
private const val OVAL_TOP_OFFSET_RATIO = 3


private val topBarButtonBackgroundColor = Color.White.copy(alpha = 0.1f)
private val topBarTextColor = Color.White
private val cameraOverlayGradientColors = listOf(
    Color.Black.copy(alpha = 0.2f),
    Color.Transparent,
    Color.Black.copy(alpha = 0.6f)
)
private val bottomSheetBackgroundColor = Color.White.copy(alpha = 0.1f)
private val bottomSheetBorderColor = Color.White.copy(alpha = 0.2f)
private val bottomSheetIconBackgroundColor = Color.White.copy(alpha = 0.1f)
private val bottomSheetTextColor = Color.White
private val captureButtonOuterRingColor = Color.White.copy(alpha = 0.3f)
private val captureButtonInnerCircleColor = Color.White.copy(alpha = 0.3f)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType")
@Composable
fun FaceDetectionScreen(
    navController: NavController,
    @SuppressLint("UnrememberedGetBackStackEntry") navBackStackEntry: NavBackStackEntry = remember { navController.getBackStackEntry("sharedGraph") }
) {
    val viewModel: CameraViewModel = hiltViewModel(navBackStackEntry)
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    var isCameraShown by remember { mutableStateOf(true) }
    var isFaceDetected by remember { mutableStateOf(false) }
    var capturedPhoto by remember { mutableStateOf<ImageBitmap?>(null) }
    var ovalCenter by remember { mutableStateOf<Offset?>(null) }
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_FRONT) } // Track lens facing
    val coroutineScope = rememberCoroutineScope() // For launching coroutines


    val cameraController: LifecycleCameraController = remember {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()
        }
    }

    val cameraPreviewView = remember {
        mutableStateOf(PreviewView(context))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Camera Preview
        if (isCameraShown) {
            Box(modifier = Modifier.fillMaxSize()) {
                CameraView(PaddingValues(), cameraPreviewView)
                // Elegant gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(colors = cameraOverlayGradientColors)
                        )
                )
            }
        }

        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .size(40.dp)
                    .background(topBarButtonBackgroundColor, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = topBarTextColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "Face Shape Analysis",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = BentosFontFamily,
                    fontWeight = FontWeight.Medium
                ),
                color = topBarTextColor
            )

            // Balancing spacer
            Spacer(modifier = Modifier.size(40.dp))
        }

        // Enhanced Oval Overlay
        OvalOverlay(
            modifier = Modifier.fillMaxSize(),
            isFaceDetected = isFaceDetected,
            onCenterCalculated = { ovalCenter = it }
        )

        // Face Detection Logic
        ovalCenter?.let {
            startFaceDetection(
                context = context,
                cameraController = cameraController,
                lifecycleOwner = lifecycleOwner,
                previewView = cameraPreviewView.value,
                onFaceDetected = { detected -> isFaceDetected = detected },
                it,
            )
        }

        // Bottom Content with Glass Effect
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (isCameraShown) {

            }
            // Status Card with Glass Effect
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        bottomSheetBackgroundColor,
                        RoundedCornerShape(28.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = bottomSheetBorderColor,
                        shape = RoundedCornerShape(28.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Animated Status Icon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                if (isFaceDetected) SuccessColor.copy(alpha = 0.1f)
                                else bottomSheetIconBackgroundColor,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isFaceDetected) Icons.Outlined.Face
                            else Icons.Outlined.AccountBox,
                            contentDescription = null,
                            tint = if (isFaceDetected) SuccessColor else bottomSheetTextColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Text(
                        text = if (isFaceDetected) "Face Detected" else "Position Your Face",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = bottomSheetTextColor,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = if (isFaceDetected)
                            "Perfect! Tap the button to capture"
                        else
                            "Center your face within the oval frame",
                        textAlign = TextAlign.Center,
                        color = bottomSheetTextColor.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(modifier = Modifier.weight(3f))
                // Modern Capture Button
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clickable(enabled = isFaceDetected) {
                            viewModel.capturePhotoAndReplaceBackground(
                                context,
                                cameraController,
                                isFaceDetected,
                                onBackgroundReplaced = { capturedBitmap ->
                                    capturedPhoto = capturedBitmap.asImageBitmap()
                                    if (isFaceDetected) isCameraShown = false
                                },
                                toNavigate = {
                                    if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                                        viewModel.toCropImage(bitmap = it, navController)
                                    } else {
                                        val matrix = Matrix()
                                        matrix.preScale(-1f, 1f) // Flip horizontally
                                        val rotatedBitmap = Bitmap.createBitmap(
                                            it,
                                            0,
                                            0,
                                            it.width,
                                            it.height,
                                            matrix,
                                            true
                                        )

                                        viewModel.toCropImage(bitmap = rotatedBitmap, navController)
                                    }
                                }
                            )
                        }
                ) {
                    // Outer ring
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 3.dp,
                                color = if (isFaceDetected) SuccessColor else captureButtonOuterRingColor,
                                shape = CircleShape
                            )
                    )

                    // Inner circle

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center)
                            .background(
                                if (isFaceDetected) SuccessColor else captureButtonInnerCircleColor,
                                CircleShape
                            )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_camera),
                            contentDescription = "Capture",
                            tint = topBarTextColor,
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.Center)
                        )

                    }
                }
                Spacer(modifier = Modifier.weight(2f))
                Box(
                    modifier = Modifier.weight(1f)
                ){
                    IconButton(
                        onClick = {
                            coroutineScope.launch { // Launch in a coroutine
                                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                                    CameraSelector.LENS_FACING_BACK
                                } else {
                                    CameraSelector.LENS_FACING_FRONT
                                }

                                cameraController.cameraSelector = CameraSelector.Builder()
                                    .requireLensFacing(lensFacing)
                                    .build()

                                cameraController.unbind() // Important: Unbind before rebinding
                                cameraController.bindToLifecycle(lifecycleOwner) // Rebind with new lens facing
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .background(topBarButtonBackgroundColor, CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.switch_camera),
                            contentDescription = "Switch Camera",
                            tint = topBarTextColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

        }
        }
    }


@Composable
private fun OvalOverlay(
    modifier: Modifier = Modifier,
    isFaceDetected: Boolean,
    onCenterCalculated: (Offset) -> Unit = {}
) {
    val ovalColor = if (isFaceDetected) {
        SuccessColor.copy(alpha = 0.8f)
    } else {
        Color.White.copy(alpha = 0.5f)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
    ) {
        val ovalCenterOffset = remember { mutableStateOf<Offset?>(null) }

        LaunchedEffect(ovalCenterOffset) {
            ovalCenterOffset.value?.let { onCenterCalculated(it) }
        }

        Canvas(modifier = modifier) {
            val ovalSize = Size(OVAL_WIDTH_DP.dp.toPx(), OVAL_HEIGHT_DP.dp.toPx())
            val ovalLeftOffset = (size.width - ovalSize.width) / OVAL_LEFT_OFFSET_RATIO
            val ovalTopOffset = (size.height - ovalSize.height) / OVAL_TOP_OFFSET_RATIO

            ovalCenterOffset.value = Offset(
                (ovalLeftOffset + OVAL_WIDTH_DP / OVAL_LEFT_OFFSET_RATIO),
                (ovalTopOffset - OVAL_HEIGHT_DP / OVAL_TOP_OFFSET_RATIO)
            )

            val ovalRect = Rect(
                ovalLeftOffset,
                ovalTopOffset,
                ovalLeftOffset + ovalSize.width,
                ovalTopOffset + ovalSize.height
            )

            val ovalPath = Path().apply {
                addOval(ovalRect)
            }

            // Draw blur overlay
            clipPath(ovalPath, clipOp = ClipOp.Difference) {
                drawRect(Color.Black.copy(alpha = 0.6f))
            }

            // Draw oval outline with glow effect
            for (i in 3 downTo 0) {
                drawOval(
                    color = ovalColor.copy(alpha = 0.15f * (4-i)),
                    style = Stroke(width = (20 + i*2).toFloat()),
                    topLeft = Offset(ovalLeftOffset, ovalTopOffset),
                    size = ovalSize
                )
            }

            // Main oval outline
            drawOval(
                color = ovalColor,
                style = Stroke(width = 20f),
                topLeft = Offset(ovalLeftOffset, ovalTopOffset),
                size = ovalSize
            )
        }
    }
}

@Composable
private fun CameraView(
    paddingValues: PaddingValues,
    cameraPreviewView: MutableState<PreviewView>,
) {
    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        factory = { context ->
            PreviewView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT,
                )
                setBackgroundColor(android.graphics.Color.BLACK)
                implementationMode = PreviewView.ImplementationMode.PERFORMANCE
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }.also {
                cameraPreviewView.value = it
            }
        },
    )
}

@Composable
private fun CapturePhotoButton(
    modifier: Modifier,
    isFaceDetected: Boolean,
    onButtonClicked: ()->Unit
) {

    Image(
        modifier = modifier
            .padding(top = 20.dp)
            .size(92.dp)
            .clickable {
                onButtonClicked()

            },
        painter = painterResource(
            id =
            if (isFaceDetected)
                R.drawable.camera_button_enabled
            else
                R.drawable.camera_button_disabled,
        ),
        contentDescription = null,
    )

}





private fun startFaceDetection(
    context: Context,
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onFaceDetected: (Boolean) -> Unit,
    ovalRect: Offset
) {

    cameraController.isPinchToZoomEnabled = true
    cameraController.isTapToFocusEnabled = true
    cameraController.imageAnalysisTargetSize = CameraController.OutputSize(AspectRatio.RATIO_16_9)
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        com.example.auraprototype.data.FaceDetector(
            onFaceDetected = onFaceDetected,
            ovalCenter = ovalRect,
            ovalRadiusX = OVAL_WIDTH_DP / 2f,
            ovalRadiusY = OVAL_HEIGHT_DP / 2f,
        ),
    )

    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
}

