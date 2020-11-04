package ru.ought.binding

@Target(AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class DoubleBinding(val property: String = "", val id: String = "") {
    companion object {
        data class DoubleBindingData(val propertyName: String = "", val id: String = "")
        fun parse(annotation: DoubleBinding, annotatedProperty: String): DoubleBindingData {
            val annotatedPropertyTrimmed = annotatedProperty.substringBefore("Property")
            var property = annotation.property
            var id = annotation.id

            if(property.isEmpty() && id.isEmpty()) {
                val regex = """(^[a-z0-9]+)([A-Z]\w+)""".toRegex()
                val result = regex.matchEntire(annotatedPropertyTrimmed) ?: error("Cannot get binding property name and id from annotated property!")
                id = result.groupValues[1]
                property = result.groupValues[2].decapitalize()
            } else if (property.isEmpty()) {
                property = annotatedPropertyTrimmed.substringAfter(id)
            } else if (id.isEmpty()) {
                id = annotatedPropertyTrimmed.substringBefore(property)
            }
            return DoubleBindingData(property + "Property", id)
        }
    }
}