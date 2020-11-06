package ru.ought.viewmodels

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.SingleSelectionModel
import ru.ought.binding.annotations.AfterBinding
import ru.ought.binding.annotations.DoubleBinding
import ru.ought.binding.annotations.DoubleBindingDirection
import ru.ought.binding.utils.jfx


class EnchantmentsViewModel {
    @DoubleBinding
    val nameTextProperty = SimpleStringProperty("")
    private var nameText: String by jfx(nameTextProperty)

    @DoubleBinding
    val costBuyTextProperty = SimpleStringProperty("")
    private var costBuyText: String by jfx(costBuyTextProperty)

    @DoubleBinding
    val costBuyTypeItemsProperty = SimpleObjectProperty<ObservableList<String>>()
    private var costBuyTypeItems: ObservableList<String> by jfx(costBuyTypeItemsProperty)

    @DoubleBinding(direction = DoubleBindingDirection.ControllerToView)
    val costBuyTypeSelectionModelProperty = SimpleObjectProperty<SingleSelectionModel<String>>()
    private var costBuyTypeSelectionModel: SingleSelectionModel<String> by jfx(costBuyTypeSelectionModelProperty)
    private var costBuyTypeIndex: Int
        get() = costBuyTypeSelectionModel.selectedIndex
        set(value) = costBuyTypeSelectionModel.select(value)


    @FXML
    private fun initialize() {
        costBuyTypeItems = FXCollections.observableList(listOf("bonus", "gp"))
    }

    @FXML
    fun onSave() {
        println(nameText)
        println(costBuyText)
        println(costBuyTypeItems[costBuyTypeIndex])
        costBuyTypeIndex = (costBuyTypeIndex + 1).rem(costBuyTypeItems.size)
    }

    @AfterBinding
    fun initializeAfterBinding() {
        costBuyTypeIndex = 0
    }
}