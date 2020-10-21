package com.malinskiy.marathon.extensions

import com.android.build.gradle.api.ApkVariantOutput
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.LibraryVariant
import com.android.build.gradle.api.LibraryVariantOutput
import com.malinskiy.marathon.log.MarathonLogging
import java.io.File
import java.lang.IllegalStateException

private val log = MarathonLogging.logger {}

fun BaseVariant.extractApplication(): File? =
    executeGradleCompat(
        exec = {
            extractApplicationBefore3_3(this)
        },
        fallbacks = listOf {
            extractApplicationBefore3_3(this)
        }
    )

private fun extractApplication3_3_plus(output: BaseVariant): File? {
    log.error("outputs first = ${output.outputs.first().outputFile.path}")
    return output.outputs.first().outputFile
/*
    val applicationProvider = when (output) {
        is ApplicationVariant -> {
            output.packageApplicationProvider
        }
        is LibraryVariant -> {
            null
        }
        else -> {
            throw RuntimeException("Can't find application provider. Output is ${output.javaClass.canonicalName}")
        }
    }

    return applicationProvider?.let {
        log.error("outputs first = ${output.outputs.first().outputFile.path}")
        return output.outputs.first().outputFile
        //File(apppackageAndroidArtifact.outputDirectory.asFile.get(), apppackageAndroidArtifact.apkNames.first())
    }
    */
}

@Suppress("DEPRECATION")
private fun extractApplicationBefore3_3(output: BaseVariant): File? {
    val variantOutput = output.outputs.first()
    return when (variantOutput) {
        is ApkVariantOutput -> {
            log.error("outputs first = ${variantOutput.outputFileName}")

            File(variantOutput.packageApplication.outputDirectory.asFile.get(), variantOutput.outputFileName)
        }
        is LibraryVariantOutput -> {
            null
        }
        else -> {
            throw RuntimeException("Can't find apk")
        }
    }
}
