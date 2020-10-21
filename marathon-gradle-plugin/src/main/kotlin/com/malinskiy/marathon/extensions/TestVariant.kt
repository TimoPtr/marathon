package com.malinskiy.marathon.extensions

import com.android.build.gradle.api.ApkVariantOutput
import com.android.build.gradle.api.LibraryVariant
import com.android.build.gradle.api.LibraryVariantOutput
import com.android.build.gradle.api.TestVariant
import com.android.build.gradle.tasks.PackageAndroidArtifact
import org.apache.tools.ant.taskdefs.Zip
import org.gradle.api.GradleException
import java.io.File
import java.lang.IllegalStateException

fun TestVariant.extractTestApplication() = executeGradleCompat(
    exec = {
        extractTestApplication3_6_plus(this)
    },
    fallbacks = listOf {
        extractTestApplicationBefore3_3(this)
    }
)

fun extractTestApplication3_6_plus(variant: TestVariant): File {
    val output = variant.outputs.first()

    return File(
        when (output) {
            is ApkVariantOutput -> {
                val packageTask =
                    variant.packageApplicationProvider.orNull ?: throw IllegalArgumentException("Can't find package application provider")
                File(packageTask.outputDirectory.asFile.get(), output.outputFileName).path
            }
            is LibraryVariantOutput -> {
                output.outputFile.path
            }
            else -> {
                throw RuntimeException("Can't find instrumentationApk")
            }
        }
    )
}

private fun extractTestApplicationBefore3_3(variant: TestVariant): File {
    val output = variant.outputs.first()

    return File(
        when (output) {
            is ApkVariantOutput -> {
                variant.packageApplicationProvider
                File(variant.packageApplication.outputDirectory.asFile.get(), output.outputFileName).path
            }
            is LibraryVariantOutput -> {
                output.outputFile.path
            }
            else -> {
                throw RuntimeException("Can't find instrumentationApk")
            }
        }
    )
}
