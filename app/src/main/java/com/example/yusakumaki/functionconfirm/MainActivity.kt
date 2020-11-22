package com.example.yusakumaki.functionconfirm

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.main_navigation_host)
        NavigationUI.setupWithNavController(bottom_navigation, navController)
    }
}

@Navigator.Name("custom_fragment")
class CustomNavigator(
        private val context: Context,
        private val manager: FragmentManager,
        private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    @Suppress("DEPRECATION")
    override fun navigate(
            destination: Destination,
            args: Bundle?,
            navOptions: NavOptions?,
            navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (manager.isStateSaved) {
            return null
        }

        var className = destination.className
        if (className[0] == '.') {
            className = context.packageName + className
        }

        val tag = destination.id.toString()
        val transaction = manager.beginTransaction()

        val currentFragment = manager.primaryNavigationFragment
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }

        var fragment = manager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = instantiateFragment(context, manager, className, args)
            transaction.add(containerId, fragment, tag)
        }
        fragment.arguments = args

        transaction.show(fragment)
        transaction.setPrimaryNavigationFragment(fragment)
        transaction.commit()

        return destination
    }
}

class CustomNavHostFragment : NavHostFragment() {
    override fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination> {
        return CustomNavigator(requireContext(), childFragmentManager, id)
    }
}