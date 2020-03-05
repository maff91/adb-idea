package com.developerphil.adbidea.adb.command

import com.android.ddmlib.IDevice
import com.developerphil.adbidea.adb.command.receiver.GenericReceiver
import com.developerphil.adbidea.ui.NotificationHelper.error
import com.intellij.openapi.project.Project
import org.apache.http.util.TextUtils
import org.jetbrains.android.facet.AndroidFacet
import java.util.concurrent.TimeUnit

class OpenDeepLinkCommand(private val deepLink: String) : Command {
    override fun run(project: Project, device: IDevice, facet: AndroidFacet, packageName: String): Boolean {
        try {
            if (TextUtils.isEmpty(deepLink)) {
                error("Deep Link was empty...")
                return false
            }
            device.executeShellCommand("am start -a android.intent.action.VIEW -d \"$deepLink\"",
                    GenericReceiver(), 15L, TimeUnit.SECONDS)
            return true
        } catch (e1: Exception) {
            error("Opening Deep Link failed... " + e1.message)
        }
        return false
    }
}