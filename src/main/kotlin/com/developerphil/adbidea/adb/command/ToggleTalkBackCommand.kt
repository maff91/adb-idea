package com.developerphil.adbidea.adb.command

import com.android.ddmlib.IDevice
import com.android.ddmlib.MultiLineReceiver
import com.developerphil.adbidea.adb.command.receiver.GenericReceiver
import com.developerphil.adbidea.ui.NotificationHelper.info
import com.google.common.base.Joiner
import com.google.common.base.Strings
import com.intellij.openapi.project.Project
import org.jetbrains.android.facet.AndroidFacet
import java.util.*
import java.util.concurrent.TimeUnit


class ToggleTalkBackCommand : Command {
    override fun run(project: Project, device: IDevice, facet: AndroidFacet, packageName: String): Boolean {
        return try {
            val receiver = ToggleTalkBackReceiver()
            device.executeShellCommand("settings get secure enabled_accessibility_services", receiver, 15L, TimeUnit.SECONDS)
            // Sometimes it might happen that the current setting is empty but we can still try to turn TalkBack on
            if (TALK_BACK_OFF_SETTING == receiver.message || receiver.message.isEmpty()) {
                device.executeShellCommand("settings put secure enabled_accessibility_services " +
                        TALK_BACK_ON_SETTING, GenericReceiver(), 15L, TimeUnit.SECONDS)
                info(String.format("TalkBack turned on on %s", device.name))
            } else if (TALK_BACK_ON_SETTING == receiver.message) {
                device.executeShellCommand("settings put secure enabled_accessibility_services " +
                        TALK_BACK_OFF_SETTING, GenericReceiver(), 15L, TimeUnit.SECONDS)
                info(String.format("TalkBack turned off on %s", device.name))
            } else {
                error("TalkBack toggle failed due to unknown current setting: " + receiver.message)
            }
            true
        } catch (e1: Exception) {
            error("TalkBack toggle failed... " + e1.message)
        }
    }

    private inner class ToggleTalkBackReceiver : MultiLineReceiver() {
        var message = ""
            private set
        private val currentLines: MutableList<String?> = ArrayList()
        override fun processNewLines(strings: Array<String>) {
            for (s in strings) {
                if (!Strings.isNullOrEmpty(s)) {
                    currentLines.add(s)
                }
            }
            computeMessage()
        }

        private fun computeMessage() {
            message = Joiner.on("\n").join(currentLines)
        }

        override fun isCancelled(): Boolean {
            return false
        }

        val isSuccess: Boolean
            get() = currentLines.size in 1..2
    }

    companion object {
        private const val TALK_BACK_OFF_SETTING = "com.android.talkback/com.google.android.marvin.talkback.TalkBackService"
        private const val TALK_BACK_ON_SETTING = "com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService"
    }
}