package com.mercadopago.android.px.internal.viewmodel.mappers

import com.meli.android.carddrawer.model.State
import com.meli.android.carddrawer.model.SwitchModel
import com.meli.android.carddrawer.model.SwitchOption
import com.meli.android.carddrawer.model.SwitchStates
import com.mercadopago.android.px.model.carddrawer.CardDrawerSwitch
import com.mercadopago.android.px.model.carddrawer.CardDrawerSwitchOption
import com.mercadopago.android.px.model.carddrawer.CardDrawerSwitchState

object CardDrawerCustomViewModelMapper {

    fun mapToSwitchModel(cardDrawerSwitch: CardDrawerSwitch?) = cardDrawerSwitch?.run {
        val states = SwitchStates(
            getStateForCardDrawer(states.checkedState),
            getStateForCardDrawer(states.uncheckedState),
            getStateForCardDrawer(states.disabledState)
        )
        SwitchModel(states, getOptionsForCardDrawer(options), backgroundColor, default)
    }

    private fun getStateForCardDrawer(state: CardDrawerSwitchState) = state.run { State(textColor, backgroundColor, weight) }
    private fun getOptionsForCardDrawer(options: List<CardDrawerSwitchOption>) = options.map { SwitchOption(it.id, it.name) }
}