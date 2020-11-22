package ru.ought.viewmodels

import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.SingleSelectionModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import ru.ought.jsonEncoder
import ru.ought.kfx_binding.binding.annotations.AfterBinding
import ru.ought.kfx_binding.fx_properties.fx
import ru.ought.kfx_binding.fx_properties.fxView
import ru.ought.kfx_binding.fx_properties.getFxProperty
import ru.ought.models.CostType
import ru.ought.models.Enchantment
import java.io.File


class EnchantmentsViewModel {
    private var nameText: String by fx()
    private var costBuyText: String by fx()
    private var requirementText: String by fx()
    private var summaryText: String by fx()
    private var descriptionText: String by fx()
    private var costCraftText: String by fx()
    private var slotText: String by fx()
    private var clText: String by fx()
    private var spellsText: String by fx()
    private var featsText: String by fx()

    private val costTypeList = FXCollections.observableArrayList(CostType.values().map { it.name.toLowerCase() })
    private var costBuyTypeItems: ObservableList<String> by fx(costTypeList)
    private var costCraftTypeItems: ObservableList<String> by fx(costTypeList)
    private var costBuyModel: SingleSelectionModel<String> by fxView(id = "costBuyType", prop = "selectionModel")
    private var costCraftModel: SingleSelectionModel<String> by fxView(id = "costCraftType", prop = "selectionModel")
    private var costTypeIndex: Int
        get() = costBuyModel.selectedIndex
        set(value) = costBuyModel.select(value)

    @AfterBinding
    fun initializeAfterBinding() {
        bindComboBox()
        costTypeIndex = 0
        loadData()
    }

    private fun bindComboBox() {
        Bindings.bindBidirectional(getFxProperty(this, ::costBuyTypeItems), getFxProperty(this, ::costCraftTypeItems))
        Bindings.bindBidirectional(
            getFxProperty(this, ::costBuyModel),
            getFxProperty(this, ::costCraftModel)
        )
    }

    private fun loadData() {
        val enchantmentFile = File("enchantment.json")
        if (enchantmentFile.canRead()) {
            val enchantment = jsonEncoder.decodeFromString<Enchantment>(enchantmentFile.readText())
            with(enchantment) {
                nameText = name
                requirementText = requirement
                summaryText = summary
                descriptionText = description
                costBuyText = costBuy.toString()
                costCraftText = costCraft.toString()
                costTypeIndex = costType.ordinal
                slotText = slot.toString()
                clText = cl.toString()
                spellsText = spells
                featsText = feats
            }
        }
    }

    @FXML
    fun onSave() {
        val enchantment = Enchantment(
            name = nameText,
            requirement = requirementText,
            summary = summaryText,
            description = descriptionText,
            costBuy = costBuyText.toInt(),
            costCraft = costCraftText.toInt(),
            costType = CostType.values()[costTypeIndex],
            slot = slotText.toInt(),
            cl = clText.toInt(),
            spells = spellsText,
            feats = featsText
        )

        File("enchantment.json").writeText(jsonEncoder.encodeToString(enchantment))
    }
}