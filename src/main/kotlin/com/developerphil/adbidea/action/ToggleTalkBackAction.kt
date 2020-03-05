package com.developerphil.adbidea.action

import com.developerphil.adbidea.adb.AdbFacade.toggleTalkBack
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class ToggleTalkBackAction : AdbAction() {
    override fun actionPerformed(e: AnActionEvent, project: Project) {
        toggleTalkBack(project)
    }
}