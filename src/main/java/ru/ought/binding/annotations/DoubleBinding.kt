package ru.ought.binding.annotations


enum class DoubleBindingDirection {
    ControllerToView, ViewToController
}

@Target(AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class DoubleBinding(
    val property: String = "",
    val id: String = "",
    val direction: DoubleBindingDirection = DoubleBindingDirection.ViewToController
) {
    companion object {
        data class DoubleBindingData(val propertyName: String, val id: String) {
            companion object {
                fun of(propertyWithoutSuffix: String, id: String) =
                    DoubleBindingData(propertyWithoutSuffix.decapitalize() + "Property", id)
            }
        }

        fun parse(annotation: DoubleBinding, annotatedProperty: String): List<DoubleBindingData> {
            val annotatedPropertyTrimmed = annotatedProperty.substringBefore("Property")
            var property = annotation.property
            var id = annotation.id

            if (property.isEmpty() && id.isEmpty()) {
                val capitalizedPositions =
                    annotatedPropertyTrimmed.withIndex().filter { it.value.isUpperCase() }.map { it.index }
                check(capitalizedPositions.any()) { "Cannot get binding property name and id from annotated property!" }

                return capitalizedPositions.map { pos ->
                    val capitalizedId = annotatedPropertyTrimmed.substring(0 until pos)
                    val capitalizedProperty = annotatedPropertyTrimmed.substring(pos)
                    DoubleBindingData.of(capitalizedProperty, capitalizedId)
                }.toList()
            } else {
                if (property.isEmpty()) {
                    property = annotatedPropertyTrimmed.substringAfter(id)
                } else if (id.isEmpty()) {
                    id = annotatedPropertyTrimmed.substringBefore(property)
                }
                return listOf(DoubleBindingData.of(property, id))
            }
        }
    }
}
