package org.benherbst.aumerge

import javafx.stage.Stage
import tornadofx.*

class AuMergeApp: App(AuMergeView::class, Styles::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.width = 350.0
        stage.height = 600.0
        stage.isResizable = false
        stage.isAlwaysOnTop = true
    }

}

fun main(args: Array<String>) {
    launch<AuMergeApp>(args)
}