package ru.ought.viewmodels

import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import ru.ought.utils.jfx


class EnchantmentsViewModel {
    @Suppress("MemberVisibilityCanBePrivate")
    val nameTextProperty = SimpleStringProperty("")
    var nameText: String by jfx(nameTextProperty)

    @FXML
    fun onSave() {
        println(nameText)
    }
}