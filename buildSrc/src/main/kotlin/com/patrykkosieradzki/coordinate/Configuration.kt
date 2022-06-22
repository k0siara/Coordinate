package com.patrykkosieradzki.coordinate

object Configuration {
    private const val majorVersion = 1
    private const val minorVersion = 0
    private const val patchVersion = 0

    const val compileSdk = 32
    const val targetSdk = 32
    const val minSdk = 17

    const val versionName = "$majorVersion.$minorVersion.$patchVersion"
    const val versionCode = majorVersion * 1000 + minorVersion * 100 + patchVersion
    const val snapshotVersionName = "$versionName-SNAPSHOT"

    const val artifactGroup = "com.patrykkosieradzki"
    const val artifactId = "coordinate"
}