package ru.ought.fx_properties

import javafx.beans.property.Property

enum class DoubleBindingDirection {
    ControllerToView, ViewToController
}

class DoubleBindingData(propertyWithoutSuffix: String, val targetId: String) {
    val targetPropertyName = propertyWithoutSuffix.decapitalize() + "Property"
}

data class FXPropertyInfo<T>(
    val fxProperty: Property<T>,
    val direction: DoubleBindingDirection,
    val targetId: String,
    val targetPropertyName: String
) {
    fun parse(kProperty: String): List<DoubleBindingData> {
        val id: String = targetId
        val property: String = targetPropertyName
        return if (id.isEmpty() && property.isEmpty()) {
            val capitalizedPositions = kProperty.camelPositions()
            check(capitalizedPositions.any()) { "Cannot get target property name and id from controller property $kProperty!" }

            capitalizedPositions.map { pos ->
                val substringId = kProperty.substring(0 until pos)
                val substringProperty = kProperty.substring(pos)
                DoubleBindingData(substringProperty, substringId)
            }.toList()
        } else {
            if (id.isEmpty() || property.isEmpty()) {
                TODO("Add ability to extract names from one argument")
            }
            listOf(DoubleBindingData(property, id))
        }
    }

    private fun String.camelPositions(): List<Int> = withIndex().filter { it.value.isUpperCase() }.map { it.index }
}

