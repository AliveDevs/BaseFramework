package com.akashic.framework.utils.treatment

import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.ShapePath

class ArgEdgeTreatment(private val size: Float,private val inside: Boolean = true) : EdgeTreatment() {
    override fun getEdgePath(length: Float, center: Float, f: Float, shapePath: ShapePath) {
        val radius = size * f
        shapePath.lineTo(center - radius, 0f)
        shapePath.addArc(
            center - radius, -radius,
            center + radius, radius,
            180f,
            if (inside) -180f else 180f
        )
        shapePath.lineTo(length, 0f)
    }
}