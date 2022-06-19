package io.github.xfy9326.glance.activity

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.xfy9326.glance.tools.startActivity
import io.github.xfy9326.glance.ui.activity.AnalysisActivityContent

class AnalysisActivity : ComponentActivity() {
    companion object {
        private const val EXTRA_IMAGE_URI = "IMAGE_URI"

        fun launch(context: Context, imageUri: Uri) {
            context.startActivity<AnalysisActivity> {
                putExtra(EXTRA_IMAGE_URI, imageUri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnalysisActivityContent()
        }
    }
}
