package io.github.xfy9326.glance.ui.common

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toRect
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.customview.widget.ExploreByTouchHelper
import io.github.xfy9326.glance.ui.data.*
import io.github.xfy9326.glance.ui.theme.AppTheme


val PreviewImageObjectInfo = ImageObjectInfo(
    size = Size(500f, 500f),
    objects = listOf(
        ImageObject(0, "class_1", 100, Offset(0f, 0f), Size(50f, 50f)),
        ImageObject(0, "class_1", 90, Offset(450f, 0f), Size(50f, 50f)),
        ImageObject(2, "class_3", 80, Offset(450f, 450f), Size(50f, 50f)),
        ImageObject(2, "class_3", 70, Offset(0f, 450f), Size(50f, 50f)),
        ImageObject(4, "class_5", 60, Offset(100f, 100f), Size(300f, 300f)),
        ImageObject(5, "class_6", 50, Offset(0f, 200f), Size(500f, 100f)),
        ImageObject(6, "class_7", 40, Offset(200f, 0f), Size(100f, 500f))
    )
)

@Preview(showBackground = true, widthDp = 500, heightDp = 500)
@Composable
private fun PreviewImageObjectBoxLayer() {
    AppTheme {
        ImageObjectBoxLayer(
            imageObjectInfo = PreviewImageObjectInfo,
            modifier = Modifier.size(500.dp),
        )
    }
}

@Composable
fun ImageObjectBoxLayer(
    imageObjectInfo: ImageObjectInfo,
    modifier: Modifier,
    boxStroke: Dp = 2.dp,
    boxStrokeColor: Color = Color.Red,
    cornerRadius: Dp = 2.dp,
    accessibilityInteractive: Boolean = true
) {
    val targetBoxStroke = if (boxStroke == Dp.Hairline) {
        (1f / LocalDensity.current.density).dp
    } else {
        boxStroke
    }
    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                val stroke = Stroke(width = targetBoxStroke.toPx())
                val radius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
                imageObjectInfo.calculateLayout(size.width, size.height).forEach {
                    drawRoundRect(
                        color = boxStrokeColor,
                        topLeft = it.boxOffset,
                        size = it.boxSize,
                        style = stroke,
                        cornerRadius = radius
                    )
                }
            }
        )
        if (accessibilityInteractive) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    ImageObjectBoxAccessibilityLayer(context).also {
                        it.layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        it.setImageObjects(imageObjectInfo)
                    }
                }
            )
        }
    }
}

private class ImageObjectBoxAccessibilityLayer(context: Context) : View(context) {
    private val touchHelper = ImageObjectBoxTouchHelper()
    private var imageObjectInfo: ImageObjectInfo? = null
    private val imageObjectLayoutInfoList = ArrayList<ImageObjectLayoutInfo>(0)

    init {
        ViewCompat.setAccessibilityDelegate(this, touchHelper)
    }

    fun setImageObjects(imageObjectInfo: ImageObjectInfo) {
        this.imageObjectInfo = imageObjectInfo
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        imageObjectInfo?.calculateLayout(measuredWidth.toFloat(), measuredHeight.toFloat())?.also {
            imageObjectLayoutInfoList.clear()
            imageObjectLayoutInfoList.addAll(it)
        }
    }

    override fun dispatchHoverEvent(event: MotionEvent): Boolean {
        return (touchHelper.dispatchHoverEvent(event) || super.dispatchHoverEvent(event))
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return (touchHelper.dispatchKeyEvent(event) || super.dispatchKeyEvent(event))
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        touchHelper.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
    }

    private inner class ImageObjectBoxTouchHelper : ExploreByTouchHelper(this) {

        override fun getVirtualViewAt(x: Float, y: Float): Int {
            for ((i, it) in imageObjectLayoutInfoList.withIndex()) {
                if (it.getAndroidRectF().contains(x, y)) {
                    return i
                }
            }
            return HOST_ID
        }

        override fun getVisibleVirtualViews(virtualViewIds: MutableList<Int>) {
            virtualViewIds.addAll(imageObjectLayoutInfoList.indices)
        }

        override fun onPopulateNodeForVirtualView(virtualViewId: Int, node: AccessibilityNodeInfoCompat) {
            if (virtualViewId in imageObjectLayoutInfoList.indices) {
                val data = imageObjectLayoutInfoList[virtualViewId]
                val screenOffset = IntArray(2).also { getLocationOnScreen(it) }
                val parentBounds = data.getAndroidRectF().toRect()
                @Suppress("DEPRECATION")
                node.setBoundsInParent(parentBounds)
                val screenBounds = parentBounds.also {
                    it.offset(screenOffset[0], screenOffset[1])
                }
                node.setBoundsInScreen(screenBounds)
                node.contentDescription = data.imageObject.classText
            } else {
                error("Invalid virtual view id $virtualViewId!")
            }
        }

        override fun onPerformActionForVirtualView(virtualViewId: Int, action: Int, arguments: Bundle?): Boolean {
            return false
        }
    }
}
