package ru.ought.utils

import org.dom4j.Attribute
import org.dom4j.DocumentHelper
import org.dom4j.Element
import java.io.InputStream

class BindingParser(data: InputStream) {
    constructor(resourceName: String): this(BindingParser::class.java.getResourceAsStream(resourceName))
    private val dataString: String = data.reader().readText()

    fun parse(): String {
        val fxmlData = extractMetadata(dataString)
        val document = DocumentHelper.parseText(fxmlData.xml)

        val rootNode = document.rootElement
        rootNode.addAttribute("BindC.controller", "\$controller")

        val bindingNodes = document.selectNodes("//*[@*='~']")
        for (node in bindingNodes) {
            if (node is Element) {
                val propertiesBinding = node.attributes()
                    .filter { it.value == "~" }
                node.addAttribute(
                    "BindC.propertyNames",
                    propertiesBinding.joinToString(",", transform = Attribute::getName)
                )
                propertiesBinding.forEach { node.remove(it) }
            }
        }

        val replaceNodes = document.selectNodes("//*[@*[starts-with(.,'~~')]]")
        for (node in replaceNodes) {
            if (node is Element) {
                val propertiesBinding = node.attributes()
                    .filter { it.value.toHashSet() == hashSetOf('~') }
                propertiesBinding.forEach { it.value = it.value.dropLast(1) }
            }
        }

        return insertMetadata(document.asXML(), fxmlData.metadata)
    }

    private data class FxmlData(val xml: String, val metadata: String)
    private val regexpMetadata = """<\?import.*\?>""".toRegex()
    private fun extractMetadata(dataString: String): FxmlData {
        val metadatas = mutableListOf<String>()
        val xml = regexpMetadata.replace(dataString) { result ->
            metadatas.add(result.value)
            ""
        }
        return FxmlData(xml, metadatas.joinToString("\n"))
    }

    private val bindingMetadata = "<?import ru.ought.utils.BindC?>"
    private fun insertMetadata(xml: String, metadata: String): String {
        val headerEnd = xml.indexOf("?>") + 2
        return buildString {
            appendLine(xml.substring(0, headerEnd))
            appendLine(metadata)
            appendLine(bindingMetadata)
            append(xml.substring(headerEnd + 1))
        }
    }
}