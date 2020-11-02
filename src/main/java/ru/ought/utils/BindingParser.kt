package ru.ought.utils

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import org.dom4j.Attribute
import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import java.io.InputStream

class BindingParser(data: InputStream) {
    constructor(resourceName: String) : this(BindingParser::class.java.getResourceAsStream(resourceName))

    private val dataString: String = data.reader().readText()

    fun <T : Parent> load(): T {
        val root = FXMLLoader().load<T>(parse().byteInputStream())
        BindingManager.performBinding(root)
        return root
    }

    private fun parse(): String {
        val fxmlData = extractMetadata(dataString)
        val document = DocumentHelper.parseText(fxmlData.xml)

        addController(document)
        addBindingProperties(document)
        addPlaceholderSymbols(document)

        return insertMetadata(document.asXML(), fxmlData.metadata)
    }

    private fun addController(document: Document) {
        val rootNode = document.rootElement
        rootNode.addAttribute("BindingManager.controller", "\$controller")
    }

    private fun addBindingProperties(document: Document) {
        val bindingNodes = document.selectNodes("//*[@*='~']")
        for (node in bindingNodes) {
            if (node is Element) {
                val propertiesBinding = node.attributes()
                    .filter { it.value == "~" }
                node.addAttribute(
                    "BindingManager.propertyNames",
                    propertiesBinding.joinToString(",", transform = Attribute::getName)
                )
                propertiesBinding.forEach { node.remove(it) }
            }
        }
    }

    private fun addPlaceholderSymbols(document: Document) {
        val replaceNodes = document.selectNodes("//*[@*[starts-with(.,'~~')]]")
        for (node in replaceNodes) {
            if (node is Element) {
                val propertiesBinding = node.attributes()
                    .filter { it.value.toHashSet() == hashSetOf('~') }
                propertiesBinding.forEach { it.value = it.value.dropLast(1) }
            }
        }
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

    private val bindingMetadata = "<?import ru.ought.utils.BindingManager?>"
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