package com.b_labs.permissionmanager

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference

class PermissionManager(private val activity: WeakReference<ComponentActivity>) {

    private val requiredPermission = mutableListOf<Permission>()
    private var detailsCallbacks: (Map<Permission, Boolean>) -> Unit = {}

    private val permissionCheck = activity.get()
        ?.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { grantedResults ->
            sendResultAndCleanUp(grantedResults)
        }

    fun request(vararg permission: Permission): PermissionManager {
        requiredPermission.addAll(permission)
        return this
    }

    fun checkPermission(callbacks: (Map<Permission, Boolean>) -> Unit) {
        this.detailsCallbacks = callbacks

        handlePermissionRequest()
    }

    private fun handlePermissionRequest() {
        activity.get()?.let {
            when {
                areAllPermissionGranted(it) -> sendPositiveResult()

                showPermissionRationale(it) -> showRationaleMessage()

                else -> requestPermissions()
            }
        }
    }

    private fun sendResultAndCleanUp(grantedResult: Map<String, Boolean>) {
        detailsCallbacks(grantedResult.mapKeys { Permission.from(it.key) })
        cleanUp()
    }

    private fun cleanUp() {
        requiredPermission.clear()
        detailsCallbacks = {}
    }

    private fun showRationaleMessage() {
        Log.d("TAG", "showRationaleMessage")
    }

    private fun showPermissionRationale(activity: ComponentActivity): Boolean {
        return requiredPermission.any { it.requiredRationale(activity) }
    }

    private fun sendPositiveResult() =
        sendResultAndCleanUp(getPermissionList().associateWith { true })

    private fun requestPermissions() {
        permissionCheck?.launch(getPermissionList())
    }

    private fun getPermissionList() =
        requiredPermission.flatMap { it.permission.toList() }.toTypedArray()

    private fun Permission.requiredRationale(activity: ComponentActivity) =
        permission.any { activity.shouldShowRequestPermissionRationale(it) }

    private fun hasPermission(activity: ComponentActivity, permission: String) =
        ContextCompat.checkSelfPermission(
            activity,
            permission,
        ) == PackageManager.PERMISSION_GRANTED

    private fun areAllPermissionGranted(activity: ComponentActivity) =
        requiredPermission.all { it.isGranted(activity) }

    private fun Permission.isGranted(activity: ComponentActivity) =
        permission.all { hasPermission(activity, it) }

    companion object {
        fun from(activity: ComponentActivity) = PermissionManager(WeakReference(activity))
    }
}
