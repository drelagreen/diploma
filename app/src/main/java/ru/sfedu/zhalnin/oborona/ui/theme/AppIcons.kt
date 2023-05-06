package ru.sfedu.zhalnin.oborona.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import ru.sfedu.zhalnin.oborona.R

interface AppIcons {
    val PasswordToggleVisible: ImageVector @Composable get
    val PasswordToggleInvisible: ImageVector @Composable get
    val CalendarEmtry: ImageVector @Composable get
    val CalendarFilled: ImageVector @Composable get
    val UserEmpty: ImageVector @Composable get
    val UserFilled: ImageVector @Composable get
    val MapEmpty: ImageVector @Composable get
    val MapFilled: ImageVector @Composable get
    val HomeEmpty: ImageVector @Composable get
    val HomeFilled: ImageVector @Composable get
    val Close: ImageVector @Composable get
    val Search: ImageVector @Composable get
    val Back: ImageVector @Composable get
    val Menu: ImageVector @Composable get
    val CommonUser: ImageVector @Composable get
    val VipUser: ImageVector @Composable get
    val SponsorUser: ImageVector @Composable get
    val Location: ImageVector @Composable get
    val Telephone: ImageVector @Composable get
    val Calendar: ImageVector @Composable get
    val HelpMenu: ImageVector @Composable get
    val Profile: ImageVector @Composable get
    val AboutFestival: ImageVector @Composable get
    val AboutApp: ImageVector @Composable get
    val Volunteer: ImageVector @Composable get
    val Sponsor: ImageVector @Composable get
    val Donate: ImageVector @Composable get
    val TechSupport: ImageVector @Composable get
    val Obslug: ImageVector @Composable get
    val Tvorch: ImageVector @Composable get
    val Vip: ImageVector @Composable get
    val Logout: ImageVector @Composable get
    val ArrowRight: ImageVector @Composable get
    val RedMapPoint: ImageVector @Composable get
    val BlueMapPoint: ImageVector @Composable get
    val ArrowDown: ImageVector @Composable get
    val Maps: ImageVector @Composable get
    val Mail: ImageVector @Composable get
    val VK: Painter @Composable get
    val Unsubscribe: ImageVector @Composable get
}

internal val LocalIcons = staticCompositionLocalOf<AppIcons> { DefaultAppIcons }

object DefaultAppIcons : AppIcons {
    private lateinit var passwordToggleVisible: ImageVector
    private lateinit var passwordToggleInvisible: ImageVector
    private lateinit var calendarEmtry: ImageVector
    private lateinit var calendarFilled: ImageVector
    private lateinit var userEmpty: ImageVector
    private lateinit var userFilled: ImageVector
    private lateinit var mapEmpty: ImageVector
    private lateinit var mapFilled: ImageVector
    private lateinit var homeEmpty: ImageVector
    private lateinit var homeFilled: ImageVector
    private lateinit var search: ImageVector
    private lateinit var close: ImageVector
    private lateinit var back: ImageVector
    private lateinit var menu: ImageVector
    private lateinit var commonUser: ImageVector
    private lateinit var vipUser: ImageVector
    private lateinit var sponsorUser: ImageVector
    private lateinit var location: ImageVector
    private lateinit var telephone: ImageVector
    private lateinit var calendar: ImageVector
    private lateinit var helpMenu: ImageVector
    private lateinit var profile: ImageVector
    private lateinit var aboutFest: ImageVector
    private lateinit var aboutApp: ImageVector
    private lateinit var volunteer: ImageVector
    private lateinit var sponsor: ImageVector
    private lateinit var donate: ImageVector
    private lateinit var techSupport: ImageVector
    private lateinit var obslug: ImageVector
    private lateinit var tvorch: ImageVector
    private lateinit var vip: ImageVector
    private lateinit var logout: ImageVector
    private lateinit var arrowRight: ImageVector
    private lateinit var redMapPoint: ImageVector
    private lateinit var blueMapPoint: ImageVector
    private lateinit var arrowDown: ImageVector
    private lateinit var maps: ImageVector
    private lateinit var mail: ImageVector
    private lateinit var vk: Painter
    private lateinit var unsubsctibe: ImageVector

    override val PasswordToggleVisible: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::passwordToggleVisible.isInitialized) {
                passwordToggleVisible =
                    ImageVector.vectorResource(id = R.drawable.ic_password_visible)
            }

            return passwordToggleVisible
        }

    override val PasswordToggleInvisible: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::passwordToggleInvisible.isInitialized) {
                passwordToggleVisible =
                    ImageVector.vectorResource(id = R.drawable.ic_password_invisible)
            }

            return passwordToggleVisible
        }

    override val CalendarEmtry: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::calendarEmtry.isInitialized) {
                calendarEmtry = ImageVector.vectorResource(id = R.drawable.ic_calendar_empty)
            }

            return calendarEmtry
        }

    override val CalendarFilled: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::calendarFilled.isInitialized) {
                calendarFilled = ImageVector.vectorResource(id = R.drawable.ic_calendar_filled)
            }

            return calendarFilled
        }

    override val UserEmpty: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::userEmpty.isInitialized) {
                userEmpty = ImageVector.vectorResource(id = R.drawable.ic_user_empty)
            }

            return userEmpty
        }

    override val UserFilled: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::userFilled.isInitialized) {
                userFilled = ImageVector.vectorResource(id = R.drawable.ic_user_filled)
            }

            return userFilled
        }

    override val MapEmpty: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::mapEmpty.isInitialized) {
                mapEmpty = ImageVector.vectorResource(id = R.drawable.ic_map_empty)
            }

            return mapEmpty
        }

    override val MapFilled: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::mapFilled.isInitialized) {
                mapFilled = ImageVector.vectorResource(id = R.drawable.ic_map_filled)
            }

            return mapFilled
        }

    override val HomeEmpty: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::homeEmpty.isInitialized) {
                homeEmpty = ImageVector.vectorResource(id = R.drawable.ic_home_empty)
            }

            return homeEmpty
        }

    override val HomeFilled: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::homeFilled.isInitialized) {
                homeFilled = ImageVector.vectorResource(id = R.drawable.ic_home_filled)
            }

            return homeFilled
        }

    override val Search: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::search.isInitialized) {
                search = ImageVector.vectorResource(id = R.drawable.ic_search)
            }

            return search
        }

    override val Close: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::close.isInitialized) {
                close = ImageVector.vectorResource(id = R.drawable.ic_close)
            }

            return close
        }

    override val Back: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::back.isInitialized) {
                back = ImageVector.vectorResource(id = R.drawable.ic_back)
            }

            return back
        }

    override val Menu: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::menu.isInitialized) {
                menu = ImageVector.vectorResource(id = R.drawable.ic_menu)
            }

            return menu
        }

    override val CommonUser: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::commonUser.isInitialized) {
                commonUser = ImageVector.vectorResource(id = R.drawable.ic_common_user)
            }

            return commonUser
        }

    override val VipUser: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::vipUser.isInitialized) {
                vipUser = ImageVector.vectorResource(id = R.drawable.ic_user_vip)
            }

            return vipUser
        }

    override val SponsorUser: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::sponsorUser.isInitialized) {
                sponsorUser = ImageVector.vectorResource(id = R.drawable.ic_user_sponsor)
            }

            return sponsorUser
        }

    override val Location: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::location.isInitialized) {
                location = ImageVector.vectorResource(id = R.drawable.ic_location)
            }

            return location
        }

    override val Telephone: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::telephone.isInitialized) {
                telephone = ImageVector.vectorResource(id = R.drawable.ic_telephone)
            }

            return telephone
        }

    override val Calendar: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::calendar.isInitialized) {
                calendar = ImageVector.vectorResource(id = R.drawable.ic_calendar)
            }

            return calendar
        }

    override val HelpMenu: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::helpMenu.isInitialized) {
                helpMenu = ImageVector.vectorResource(id = R.drawable.ic_help)
            }

            return helpMenu
        }

    override val Profile: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::profile.isInitialized) {
                profile = ImageVector.vectorResource(id = R.drawable.ic_profile)
            }

            return profile
        }

    override val AboutFestival: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::aboutFest.isInitialized) {
                aboutFest = ImageVector.vectorResource(id = R.drawable.ic_festival)
            }

            return aboutFest
        }

    override val AboutApp: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::aboutApp.isInitialized) {
                aboutApp = ImageVector.vectorResource(id = R.drawable.ic_info)
            }

            return aboutApp
        }

    override val Logout: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::logout.isInitialized) {
                logout = ImageVector.vectorResource(id = R.drawable.ic_sign_out)
            }

            return logout
        }


    override val Vip: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::vip.isInitialized) {
                vip = ImageVector.vectorResource(id = R.drawable.ic_ticket)
            }

            return vip
        }

    override val Volunteer: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::volunteer.isInitialized) {
                volunteer = ImageVector.vectorResource(id = R.drawable.ic_volunteer)
            }

            return volunteer
        }

    override val Sponsor: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::sponsor.isInitialized) {
                sponsor = ImageVector.vectorResource(id = R.drawable.ic_sponsor)
            }

            return sponsor
        }

    override val TechSupport: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::techSupport.isInitialized) {
                techSupport = ImageVector.vectorResource(id = R.drawable.ic_tech)
            }

            return techSupport
        }

    override val Tvorch: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::tvorch.isInitialized) {
                tvorch = ImageVector.vectorResource(id = R.drawable.ic_tvorch)
            }

            return tvorch
        }

    override val Donate: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::donate.isInitialized) {
                donate = ImageVector.vectorResource(id = R.drawable.ic_donate)
            }

            return donate
        }

    override val Obslug: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::obslug.isInitialized) {
                obslug = ImageVector.vectorResource(id = R.drawable.ic_service)
            }

            return obslug
        }

    override val ArrowRight: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::arrowRight.isInitialized) {
                arrowRight = ImageVector.vectorResource(id = R.drawable.ic_expand_right)
            }

            return arrowRight
        }

    override val RedMapPoint: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::redMapPoint.isInitialized) {
                redMapPoint = ImageVector.vectorResource(id = R.drawable.ic_position_red)
            }

            return redMapPoint
        }

    override val BlueMapPoint: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::blueMapPoint.isInitialized) {
                blueMapPoint = ImageVector.vectorResource(id = R.drawable.ic_position_blue)
            }

            return blueMapPoint
        }

    override val ArrowDown: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::arrowDown.isInitialized) {
                arrowDown = ImageVector.vectorResource(id = R.drawable.ic_arrow_down)
            }

            return arrowDown
        }

    override val Maps: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::maps.isInitialized) {
                maps = ImageVector.vectorResource(id = R.drawable.ic_maps)
            }

            return maps
        }

    override val Mail: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::mail.isInitialized) {
                mail = ImageVector.vectorResource(id = R.drawable.ic_mail)
            }

            return mail
        }

    override val VK: Painter
        @Composable get() {
            if (!DefaultAppIcons::vk.isInitialized) {
                vk = painterResource(id = R.drawable.ic_vk_png)
            }

            return vk
        }

    override val Unsubscribe: ImageVector
        @Composable get() {
            if (!DefaultAppIcons::unsubsctibe.isInitialized) {
                unsubsctibe = ImageVector.vectorResource(id = R.drawable.ic_unsubscribe)
            }

            return unsubsctibe
        }
}