plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.dokka)
    id("maven-publish")
    id("signing")
}

group = "io.github.pablichjenkov"
version = libs.versions.macaoComponentToolkit.get()
val mavenCentralUser = (findProperty("mavenCentral.user") as? String).orEmpty()
val mavenCentralPass = (findProperty("mavenCentral.pass") as? String).orEmpty()

/*tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("dokka"))
    moduleName.set("component")
    cacheRoot.set(file("default"))
    suppressObviousFunctions.set(false)
    offlineMode.set(true)
}*/

// Configure Dokka
tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    // custom output directory
    outputDirectory.set(buildDir.resolve("dokka"))
    moduleName.set("component")
    suppressObviousFunctions.set(false)
    offlineMode.set(true)
    /*dokkaSourceSets {
        named("customNameMain") { // The same name as in Kotlin Multiplatform plugin, so the sources are fetched automatically
            includes.from("packages.md", "extra.md")
            samples.from("samples/basic.kt", "samples/advanced.kt")
        }

        register("differentName") { // Different name, so source roots must be passed explicitly
            displayName.set("JVM")
            platform.set(org.jetbrains.dokka.Platform.jvm)
            sourceRoots.from(kotlin.sourceSets.getByName("jvmMain").kotlin.srcDirs)
            sourceRoots.from(kotlin.sourceSets.getByName("commonMain").kotlin.srcDirs)
        }
    }*/
}

val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}

signing {
    sign(publishing.publications)
}

publishing {
    repositories {
        maven {
            name = "Central"
            // setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            // setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            setUrl("https://s01.oss.sonatype.org/content/repositories/releases/")
            credentials {
                username = mavenCentralUser
                password = mavenCentralPass
            }
        }
    }
    publications {
        withType<MavenPublication> {
            groupId = group as String
            artifactId = "component-toolkit" // makeArtifactId(name)
            version
            artifact(javadocJar)
            pom {
                val projectGitUrl = "https://github.com/pablichjenkov/component-toolkit"
                name.set(rootProject.name)
                description.set(
                    "Compose multiplatform reusable components."
                )
                url.set(projectGitUrl)
                inceptionYear.set("2023")
                licenses {
                    license {
                        name.set("The Unlicense")
                        url.set("https://unlicense.org")
                    }
                }
                developers {
                    developer {
                        id.set("pablichjenkov")
                    }
                }
                issueManagement {
                    system.set("GitHub")
                    url.set("$projectGitUrl/issues")
                }
                scm {
                    connection.set("scm:git:$projectGitUrl")
                    developerConnection.set("scm:git:$projectGitUrl")
                    url.set(projectGitUrl)
                }
            }
        }
    }
}

// Workaround for gradle issue: https://youtrack.jetbrains.com/issue/KT-46466
val signingTasks = tasks.withType<Sign>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(signingTasks)
}

/*compose {
    // Sets a specific JetBrains Compose Compiler version
    kotlinCompilerPlugin.set("1.4.7")
    // Sets a specific Google Compose Compiler version
    // kotlinCompilerPlugin.set("androidx.compose.compiler:compiler:1.4.2")
    kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=1.8.21")
}*/

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    // ANDROID
    androidTarget {
        publishLibraryVariants("release", "debug")
    }

    // IOS
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { target ->
        target.binaries.framework {
            baseName = "ComponentToolkitKt"
        }
    }

    // Browser
    js(IR) {
        browser()
    }

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "ComponentToolkitKt"
        browser()
        binaries.library()
    }

    // JVM
    jvm()

    sourceSets {
        // COMMON
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.animation)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // Lifecycle, used for components lifecycle
            implementation(libs.lifecycle.viewmodel.compose)

        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        // ANDROID
        androidMain.dependencies {
            implementation(libs.activity.compose)
            api(compose.uiTooling)
            api(compose.preview)
        }
        val androidUnitTest by getting {
            dependencies {
                // Android Junit
                implementation(libs.junit)
            }
        }
        val androidInstrumentedTest by getting

        // IOS
        iosMain.dependencies {
        }

        // WASM
        /*val wasmMain by getting
        val wasmTest by getting
        */

        // JVM
        jvmMain.dependencies {
            api(compose.uiTooling)
            api(compose.preview)
        }
    }

}

android {
    namespace = "com.macaosoftware.component"
    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res", "src/commonMain/resources")
        }
    }
    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
        compileSdk = libs.versions.androidCompileSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    composeCompiler {
        enableStrongSkippingMode = true
    }
}
