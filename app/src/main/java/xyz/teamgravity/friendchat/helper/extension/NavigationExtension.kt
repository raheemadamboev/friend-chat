package xyz.teamgravity.friendchat.helper.extension

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

fun NavController.navigateSafely(
    direction: NavDirections,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
) {
    val action = currentDestination?.getAction(direction.actionId) ?: graph.getAction(direction.actionId)
    if (action != null && currentDestination?.id != action.destinationId) {
        navigate(direction.actionId, direction.arguments, navOptions, navExtras)
    }
}