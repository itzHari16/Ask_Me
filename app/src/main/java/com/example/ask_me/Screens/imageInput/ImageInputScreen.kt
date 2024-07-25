package com.example.ask_me.Screens.imageInput

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.impl.utils.MatrixExt.postRotate
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ask_me.components.CameraPreview
import com.example.ask_me.navigation.Screens

@Composable
fun ImageInputScreen(
    navController: NavController, imageInputViewModel: ImageInputViewModel = viewModel()
) {
    CameraPermission(navController = navController, imageInputViewModel = imageInputViewModel)

}

@Composable
fun CameraPermission(navController: NavController, imageInputViewModel: ImageInputViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    val cameraController = remember {
        LifecycleCameraController(context)
    }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val PermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            PermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            cameraProviderFuture.addListener({
                // cameraController.cameraProvider = cameraProviderFuture.get()
                cameraController.bindToLifecycle(lifecycleOwner)
            }, ContextCompat.getMainExecutor(context))
        }

    }

    if (hasCameraPermission) {
        CameraPreviewScreen(
            cameraController,
            navController = navController,
            imageInputViewModel = imageInputViewModel
        )
    } else {
        PermissionRequestScreen(onRequestPermission = {
            PermissionLauncher.launch(Manifest.permission.CAMERA)
        })
    }

}


@Composable
fun PermissionRequestScreen(onRequestPermission: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(onClick = onRequestPermission) {
            Text("Request Camera Permission")
        }
    }
}

@Composable
fun CameraPreviewScreen(
    cameraController: LifecycleCameraController,
    navController: NavController,
    imageInputViewModel: ImageInputViewModel
) {
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(controller = controller, modifier = Modifier.fillMaxSize())
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = {
                takePhoto(controller = controller, context = context, onImageCaptured = { bitmap ->
                    imageInputViewModel.sendImageAndText(bitmap, "Hello")
                    navController.navigate(Screens.ImageChatScreen.name)
                }
                )
            }) {
                Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Camera")
            }
        }
    }
}


fun takePhoto(
    controller: LifecycleCameraController,
    onImageCaptured: (Bitmap) -> Unit,
    context: Context
) {
    //  val context = LocalContext.current
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val matrix = android.graphics.Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(), 0, 0, image.width, image.height, matrix, true
                )
                image.close()
                onImageCaptured(rotatedBitmap)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)

            }

        }
    )

}

fun ImageProxy.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

////@Composable
//fun takePhoto(
//    controller: LifecycleCameraController,
//    imageInputViewModel: ImageInputViewModel,
//    navController: NavController,
//    context:Context
//) {
//    // val context = LocalContext.current
//    controller.takePicture(
//        ContextCompat.getMainExecutor(context),
//        object : ImageCapture.OnImageCapturedCallback() {
//            override fun onCaptureSuccess(image: ImageProxy) {
//                super.onCaptureSuccess(image)
////                val matrix = Matrix().apply {
////                    postRotate(image.imageInfo.rotationDegrees.toFloat())
////                }
//                val rotatedBitmap = BitmapFactory.decodeByteArray(
//                    imageProxyToBytes(image),
//                    0,
//                    imageProxyToBytes(image).size,
//                    BitmapFactory.Options().apply {
//                        inMutable = true
//                    }
//                )
//                image.close()
//                imageInputViewModel.sendImageAndText(rotatedBitmap, "Hello")
//                navController.navigate(Screens.ImageChatScreen.name)
//            }
//
//            override fun onError(exception: ImageCaptureException) {
//                super.onError(exception)
//                // Handle error
//            }
//        }
//    )
//}
//
//fun imageProxyToBytes(image: ImageProxy): ByteArray {
//    val buffer = image.planes[0].buffer
//    val bytes = ByteArray(buffer.capacity())
//    buffer.get(bytes)
//    return bytes
//}

