package com.thisthatdc.charting.extensions

import com.thisthatdc.charting.models.IndicatorCount

internal fun split(
    count: IndicatorCount,
    minValue: Double,
    maxValue: Double,
):List<Double>{
    return when (count) {
        is IndicatorCount.CountBased -> {
            val step = (maxValue - minValue) / (count.count - 1)
            val result = (0 until count.count).map { (maxValue - it * step) }
            result
        }

        is IndicatorCount.StepBased -> {
            val result = mutableListOf<Double>()
            var cache = maxValue
            while (cache > minValue - count.stepBy) {
                result.add(cache.coerceAtLeast(minValue))
                cache -= count.stepBy
            }
            result
        }
    }
}
