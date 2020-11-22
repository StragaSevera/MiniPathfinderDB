package ru.ought.fx_properties

import javafx.beans.property.Property

enum class DoubleBindingDirection {
    ControllerToView, ViewToController
}

class DoubleBindingData constructor(propertyWithoutSuffix: String, val targetId: String) {
    val targetPropertyName = propertyWithoutSuffix.decapitalize() + "Property"
}

data class FXPropertyInfo<T>(
    val fxProperty: Property<T>,
    val direction: DoubleBindingDirection,
    val targetId: String,
    val targetPropertyName: String
) {
    fun parse(kProperty: String): List<DoubleBindingData> {
        var id: String = targetId
        var property: String = targetPropertyName
        if (id.isEmpty() && property.isEmpty()) {
            val capitalizedPositions = kProperty.camelPositions()
            check(capitalizedPositions.any()) { "Cannot get target property name and id from controller property $kProperty!" }

            return capitalizedPositions.map { pos ->
                val substringId = kProperty.substring(0 until pos)
                val substringProperty = kProperty.substring(pos)
                DoubleBindingData(substringProperty, substringId)
            }.toList()
        } else {
            if (id.isEmpty()) {
                val matchingIndex =
                    kProperty.findStringCamelDiffIndex(property.capitalize(), Direction.FromEnd, Direction.FromEnd)
                property = matchingIndex?.let { kProperty.substring(0, it - 1) } ?: kProperty.substringBefore(property)
            } else if (property.isEmpty()) {
                val matchingIndex = kProperty.findStringCamelDiffIndex(id, Direction.FromStart, Direction.FromStart)
                property = matchingIndex?.let { kProperty.substring(it) } ?: kProperty.substringAfter(id)
            }
            return listOf(DoubleBindingData(property, id))
        }
    }

    private fun String.camelPositions(): List<Int> = withIndex().filter { it.value.isUpperCase() }.map { it.index }

    private enum class Direction { FromStart, FromEnd }

    private fun String.findStringCamelDiffIndex(
        diffStr: String,
        baseDirection: Direction,
        diffDirection: Direction
    ): Int? {
        val result: Int?
        val baseCamelPositions = camelPositions()
        val diffCamelPositions = diffStr.camelPositions()
        result = if (baseDirection == Direction.FromStart && diffDirection == Direction.FromStart) {
            baseCamelPositions.withIndex().zip(diffCamelPositions.withIndex())
                .find { (strDatum, diffDatum) -> strDatum.value != diffDatum.value }?.first?.index
        } else if (baseDirection == Direction.FromEnd && diffDirection == Direction.FromEnd) {
            withIndex().reversed().zip(diffStr.withIndex().reversed())
                .find { (strDatum, diffDatum) -> strDatum.value != diffDatum.value }?.first?.index
        } else {
            TODO("Add ability to match in different directions")
        }
        return result
    }
}

