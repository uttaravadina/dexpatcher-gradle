/*
 * DexPatcher - Copyright 2015-2019 Rodrigo Balerdi
 * (GNU General Public License version 3 or later)
 *
 * DexPatcher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 */

package lanchon.dexpatcher.gradle.tasks

import groovy.transform.CompileStatic

import lanchon.dexpatcher.gradle.Constants
import lanchon.dexpatcher.gradle.Utils

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

@CompileStatic
class DecodedSourceAppTask extends DefaultTask {

    @Internal final ConfigurableFileCollection sourceApp
    @OutputDirectory final DirectoryProperty outputDir
    @Internal final Provider<RegularFile> sourceAppFile
    @Internal final Provider<RegularFile> apktoolYmlFile

    DecodedSourceAppTask() {
        sourceApp = project.files()
        outputDir = project.layout.directoryProperty()
        sourceAppFile = project.<RegularFile>provider {
            def files = sourceApp.files
            def n = files.size()
            if (n != 1) {
                if (!n) throw new RuntimeException('No source application found')
                else throw new RuntimeException('Multiple source applications found')
            }
            return Utils.getRegularFile(project, files[0])
        }
        apktoolYmlFile = outputDir.file(Constants.ApkLib.FILE_APKTOOL_YML)
        outputs.upToDateWhen {
            false
        }
    }

    @TaskAction
    void check() {
        sourceAppFile.get()
        if (!apktoolYmlFile.get().asFile.isFile()) {
            throw new RuntimeException("Cannot find '$Constants.ApkLib.FILE_APKTOOL_YML' file in decoded application")
        }
    }

}
