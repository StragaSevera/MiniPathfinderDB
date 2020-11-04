package ru.ought.viewmodels

import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import ru.ought.binding.DoubleBinding
import ru.ought.binding.utils.jfx


@Suppress("MemberVisibilityCanBePrivate")
class EnchantmentsViewModel {
    @DoubleBinding
    val nameTextProperty = SimpleStringProperty("")
    var nameText: String by jfx(nameTextProperty)

    @FXML
    fun onSave() {
        println(nameText)
    }
}