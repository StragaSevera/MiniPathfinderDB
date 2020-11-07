package ru.ought.fx_properties

object FXDelegatedPropertyManager{
    private val fxProperties: MutableMap<Any, MutableMap<String, FXPropertyInfo<*>>> = mutableMapOf()
    fun registerProperty(controller: Any, propertyName: String, fxPropertyInfo: FXPropertyInfo<*>) {
        val controllerFxProperties = fxProperties.getOrPut(controller) { mutableMapOf() }
        controllerFxProperties[propertyName] = fxPropertyInfo
    }

    fun getPropertiesFor(controller: Any): Map<String, FXPropertyInfo<*>> = fxProperties[controller]?.toMap() ?: mapOf()
}
