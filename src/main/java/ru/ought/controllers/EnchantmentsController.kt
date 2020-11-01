package ru.ought.controllers

import javafx.beans.property.SimpleStringProperty
import javafx.event.ActionEvent
import javafx.fxml.FXML
import ru.ought.utils.jfx


class EnchantmentsController {
    @Suppress("MemberVisibilityCanBePrivate")
    val nameTextProperty = SimpleStringProperty("")
    var nameText: String by jfx(nameTextProperty)

    @FXML
    fun onSave(event: ActionEvent) {
        println(nameText)
    }
}