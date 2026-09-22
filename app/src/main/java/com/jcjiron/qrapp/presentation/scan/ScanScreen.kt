package com.jcjiron.qrapp.presentation.scan

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jcjiron.qrapp.R
import com.jcjiron.qrapp.domain.model.ScannedCode
import java.util.concurrent.Executors

private const val TAG = "ScanScreen"

@Composable
fun ScanScreen(
    modifier: Modifier = Modifier,
    viewModel: ScanViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // The permission is a UI/platform concern, so it stays in the composable.
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Box(modifier) {
        val state = uiState
        when {
            !hasCameraPermission -> PermissionRequest(
                onRequest = { permissionLauncher.launch(Manifest.permission.CAMERA) },
            )
            state is ScanUiState.Scanned -> ScanResult(
                code = state.code,
                onScanAgain = viewModel::onScanAgain,
            )
            else -> CameraPreview(analyzer = viewModel.analyzer)
        }
    }
}

@Composable
private fun PermissionRequest(onRequest: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.camera_permission_rationale),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onRequest, modifier = Modifier.padding(top = 16.dp)) {
            Text(stringResource(R.string.camera_permission_grant))
        }
    }
}

@Composable
private fun CameraPreview(analyzer: ImageAnalysis.Analyzer) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    DisposableEffect(lifecycleOwner, analyzer) {
        val analysisExecutor = Executors.newSingleThreadExecutor()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }
        val analysis = ImageAnalysis.Builder()
            // If analysis falls behind, drop stale frames instead of queueing them.
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { it.setAnalyzer(analysisExecutor, analyzer) }

        var cameraProvider: ProcessCameraProvider? = null
        var disposed = false
        val providerFuture = ProcessCameraProvider.getInstance(context)
        providerFuture.addListener({
            if (disposed) return@addListener
            val provider = providerFuture.get()
            cameraProvider = provider
            try {
                provider.unbindAll()
                provider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    analysis,
                )
            } catch (e: Exception) {
                Log.e(TAG, "Could not start the camera", e)
            }
        }, ContextCompat.getMainExecutor(context))

        onDispose {
            disposed = true
            cameraProvider?.unbind(preview, analysis)
            analysis.clearAnalyzer()
            analysisExecutor.shutdown()
        }
    }

    Box(Modifier.fillMaxSize()) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
        Surface(
            color = MaterialTheme.colorScheme.inverseSurface,
            contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp),
        ) {
            Text(
                text = stringResource(R.string.scan_hint),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun ScanResult(code: ScannedCode, onScanAgain: () -> Unit) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(stringResource(R.string.scan_result_title), style = MaterialTheme.typography.titleLarge)
        Card(Modifier.fillMaxWidth()) {
            SelectionContainer {
                Text(
                    text = code.content,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = {
                clipboard.setText(AnnotatedString(code.content))
                Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show()
            }) {
                Text(stringResource(R.string.action_copy))
            }
            if (code.isLink) {
                OutlinedButton(onClick = {
                    try {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(code.content)))
                    } catch (e: ActivityNotFoundException) {
                        Log.w(TAG, "No app can open ${code.content}", e)
                    }
                }) {
                    Text(stringResource(R.string.action_open))
                }
            }
        }
        Button(onClick = onScanAgain, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.scan_again))
        }
    }
}
