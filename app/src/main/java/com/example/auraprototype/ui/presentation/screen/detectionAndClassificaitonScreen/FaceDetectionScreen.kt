package com.example.auraprototype.ui.presentation.screen.detectionAndClassificaitonScreen

import com.example.auraprototype.R


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix


import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path

import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.dotlottie.dlplayer.Mode
import com.example.auraprototype.ui.theme.ErrorColor
import com.example.auraprototype.ui.theme.PrimaryColor
import com.example.auraprototype.ui.theme.SecondaryColor
import com.example.auraprototype.ui.theme.SuccessColor
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import java.util.concurrent.Executor

private const val OVAL_WIDTH_DP = 250
private const val OVAL_HEIGHT_DP = 300
private const val OVAL_LEFT_OFFSET_RATIO = 2
private const val OVAL_TOP_OFFSET_RATIO = 3

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

    val cameraController: LifecycleCameraController =
        remember { LifecycleCameraController(context) }
    cameraController.cameraSelector =
        CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

    val cameraPreviewView = remember {
        mutableStateOf(PreviewView(context))
    }

    Scaffold(
        topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = "Face Shape Detection",
                                style = MaterialTheme.typography.titleLarge,
                                fontStyle = FontStyle.Normal,
                                fontFamily = FontFamily.Monospace

                            )
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PrimaryColor),
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Outlined.ArrowBack,
                                    contentDescription = "Back Arrow",
                                    tint = Color.White
                                )
                            }
                        }

                    )


            },
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding(),
    ) { paddingValues: PaddingValues ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isCameraShown) {
                CameraView(paddingValues, cameraPreviewView)
            }

            OvalOverlay(
                modifier = Modifier.fillMaxSize(),
                isFaceDetected = isFaceDetected,
                onCenterCalculated = { ovalCenter = it }
            )
            ovalCenter?.let {
                startFaceDetection(
                    context = context,
                    cameraController = cameraController,
                    lifecycleOwner = lifecycleOwner,
                    previewView = cameraPreviewView.value,
                    onFaceDetected = { detected ->
                        isFaceDetected = detected
                    },
                    it,
                )
            }
            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                if (isCameraShown) {
                    Text(
                        text = "Analyzing Face Shape",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Text(
                        text = "Hold still we are currently detecting your unique face shape, so please be patient and press the capture button as soon as the oval turns green" ,
                        textAlign = TextAlign.Center
                    )
                }

                if (isCameraShown) {
                    CapturePhotoButton(
                        modifier = Modifier
                            .padding(bottom = 50.dp),
                        isFaceDetected = isFaceDetected,
                        onButtonClicked = {
                            viewModel.capturePhotoAndReplaceBackground(
                                context,
                                cameraController,
                                isFaceDetected,
                                onBackgroundReplaced = { capturedBitmap ->
                                    capturedPhoto = capturedBitmap.asImageBitmap()
                                    if (isFaceDetected)
                                        isCameraShown = false
                                },
                                toNavigate = {
                                    viewModel.processImage(it, navController)
                                }
                            )
                        },
                    )
                }
            }
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
    ovalRect: Offset,
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


@Composable
private fun OvalOverlay(
    modifier: Modifier = Modifier,
    isFaceDetected: Boolean,
    onCenterCalculated: (Offset) -> Unit = {},
) {
    val ovalColor =
        if (isFaceDetected) SuccessColor else ErrorColor
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
    ) {

        val ovalCenterOffset = remember {
            mutableStateOf<Offset?>(null)
        }
        LaunchedEffect(ovalCenterOffset) {
            ovalCenterOffset.value?.let { onCenterCalculated(it) }
        }
        Canvas(modifier = modifier) {
            val ovalSize = Size(OVAL_WIDTH_DP.dp.toPx(), OVAL_HEIGHT_DP.dp.toPx())
            val ovalLeftOffset = (size.width - ovalSize.width) / OVAL_LEFT_OFFSET_RATIO
            val ovalTopOffset = (size.height - ovalSize.height) / OVAL_TOP_OFFSET_RATIO

            ovalCenterOffset.value =
                Offset(
                    (ovalLeftOffset + OVAL_WIDTH_DP / OVAL_LEFT_OFFSET_RATIO),
                    (ovalTopOffset - OVAL_HEIGHT_DP / OVAL_TOP_OFFSET_RATIO)
                )

            val ovalRect =
                Rect(
                    ovalLeftOffset,
                    ovalTopOffset,
                    ovalLeftOffset + ovalSize.width,
                    ovalTopOffset + ovalSize.height
                )
            val ovalPath = Path().apply {
                addOval(ovalRect)
            }
            clipPath(ovalPath, clipOp = ClipOp.Difference) {
                drawRect(SolidColor(SecondaryColor))
            }
        }


        Canvas(
            modifier = modifier,
        ) {
            val ovalSize = Size(OVAL_WIDTH_DP.dp.toPx(), OVAL_HEIGHT_DP.dp.toPx())
            val ovalLeft = (size.width - ovalSize.width) / OVAL_LEFT_OFFSET_RATIO
            val ovalTop =
                (size.height - ovalSize.height) / OVAL_TOP_OFFSET_RATIO - ovalSize.height
            drawOval(
                color = ovalColor,
                style = Stroke(width = 20f),
                topLeft = Offset(ovalLeft, ovalTop + ovalSize.height),
                size = ovalSize,
                alpha = 0.8f
            )
        }

    }
}



