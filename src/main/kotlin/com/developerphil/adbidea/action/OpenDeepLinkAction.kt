package com.developerphil.adbidea.action

import com.developerphil.adbidea.adb.AdbFacade.openDeepLink
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages

class OpenDeepLinkAction : AdbAction() {
    override fun actionPerformed(e: AnActionEvent, project: Project) {
        val deepLink = Messages.showInputDialog(project, "Insert the Deep Link you want to open",
                "ADB Open Deep Link", Messages.getInformationIcon(), "", object : InputValidator {

            override fun checkInput(s: String): Boolean {
                return s.isNotBlank() && s.contains("://")
            }

            override fun canClose(s: String): Boolean {
                return true
            }
        })
        if (!deepLink.isNullOrBlank()) {
            openDeepLink(project, deepLink)
        }
    }
}