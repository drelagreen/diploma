package ru.sfedu.zhalnin.oborona.ui.menu

import androidx.lifecycle.ViewModel

class MenuViewModel : ViewModel() {
    sealed class Action {
        object HelpMenuClicked : Action()
        object ProfileClicked : Action()
        object AboutFestivalClicked : Action()
        object ContactsClicked : Action()
        object AboutAppClicked : Action()
        object LogoutClicked : Action()

        object TechClicked : Action()
        object VolunteerClicked : Action()
        object SponsorClicked : Action()
        object DonateClicked : Action()
        object TvorchClicked : Action()
        object ObslClicked : Action()
    }
}