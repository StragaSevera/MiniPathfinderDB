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
    fun parse(controllerPropertyName: String): List<DoubleBindingData> {
        var id: String = targetId
        var property: String = targetPropertyName
        if (id.isEmpty() && property.isEmpty()) {
            val capitalizedPositions =
                controllerPropertyName.withIndex().filter { it.value.isUpperCase() }.map { it.index }
            check(capitalizedPositions.any()) { "Cannot get target property name and id from controller property $controllerPropertyName!" }

            return capitalizedPositions.map { pos ->
                val substringId = controllerPropertyName.substring(0 until pos)
                val substringProperty = controllerPropertyName.substring(pos)
                DoubleBindingData(substringProperty, substringId)
            }.toList()
        } else {
            if (id.isEmpty()) {
                id = controllerPropertyName.substringBefore(property)
            } else if (property.isEmpty()) {
                property = controllerPropertyName.substringAfter(id)
            }
            return listOf(DoubleBindingData(property, id))
        }
    }
}