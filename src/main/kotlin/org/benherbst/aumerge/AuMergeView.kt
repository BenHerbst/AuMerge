package org.benherbst.aumerge

import javafx.application.Platform
import javafx.scene.input.KeyCode
import javafx.scene.input.TransferMode
import javafx.scene.layout.Priority
import tornadofx.*
import javafx.stage.FileChooser
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.concurrent.thread

class AuMergeView : View("AuMerge") {
    var filePaths = SortedFilteredList<String>()

    override val root = vbox {
        label("Drop your audio files here:")
        listview<String>(filePaths) {
            // The list of audio files that will be merged
            vboxConstraints {
                vGrow = Priority.ALWAYS
            }
            setOnDragOver { event ->
                // Accept audio files get dropped
                if (event.dragboard.hasFiles()) {
                    if (event.dragboard.files[0].toString().endsWith(".mp3"))
                    event.acceptTransferModes(TransferMode.COPY, TransferMode.MOVE)
                }
                event.consume()
            }
            setOnDragDropped { event ->
                if (event.dragboard.hasFiles()) {
                    // Add dropped file to list
                    filePaths.add(event.dragboard.files[0].toString())
                }
            }
            setOnKeyPressed { event ->
                if (event.code == KeyCode.BACK_SPACE || event.code == KeyCode.DELETE) {
                    // Remove currently selected audio file from list
                    if (selectedItem != null) {
                        filePaths.remove(selectedItem.toString())
                    }
                }
            }
        }
        button("Merge") {
            minWidth = 346.0
            action {
                if (filePaths.size > 1) {
                    // Ask where to save
                    val outputLocationArray = chooseFile(
                        "Select output",
                        arrayOf(
                            FileChooser.ExtensionFilter("All filetypes", "*"),
                            FileChooser.ExtensionFilter("Mp3-Audio", "*.mp3")
                        ),
                        null,
                        null,
                        FileChooserMode.Save
                    )
                    if (outputLocationArray.isNotEmpty()) {
                        // Merge audio files
                        var outputLocation = outputLocationArray[0].toString()
                        // Add .mp3 to output file if needed
                        if (!outputLocation.endsWith(".mp3")) {
                            outputLocation += ".mp3"
                        }
                        val fileOutputStream = FileOutputStream(outputLocation)
                        var currentIndex = 0
                        while (currentIndex < filePaths.size) {
                            // Write each to the output file
                            val inputStream = FileInputStream(filePaths[currentIndex])
                            var temp: Int
                            temp = inputStream.read()
                            while (temp != -1) {
                                fileOutputStream.write(temp)
                                temp = inputStream.read()
                            }
                            currentIndex++
                        }
                    }
                }
            }
        }
        progressbar {
            minWidth = 350.0
            thread {
                for (i in 1..100) {
                    Platform.runLater { progress = i.toDouble() / 100.0 }
                    Thread.sleep(100)
                }
            }
        }
    }

}