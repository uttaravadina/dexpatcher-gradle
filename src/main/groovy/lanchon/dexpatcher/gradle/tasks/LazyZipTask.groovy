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

import lanchon.dexpatcher.gradle.Utils

import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.bundling.Zip

// FIXME: This hack apparently does not work on Gradle 5.1 or later.
// (See: https://github.com/gradle/gradle/issues/8401)

@CompileStatic
class LazyZipTask extends Zip {

    @OutputFile final RegularFileProperty archiveFile

    LazyZipTask() {
        archiveFile = project.layout.fileProperty()
        archiveFile.set project.<RegularFile>provider {
            Utils.getRegularFile(project, super.getArchivePath())
        }
    }

    @Internal @Override public File getArchivePath() {
        return archiveFile.get().asFile;
    }

}