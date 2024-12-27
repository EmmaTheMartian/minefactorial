import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.nio.charset.StandardCharsets

plugins {
    id("java-library")
    id("maven-publish")
    id("dev.architectury.loom") version "1.7-SNAPSHOT"
    id("me.fallenbreath.yamlang") version "1.4.0"
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("me.modmuss50.mod-publish-plugin") version "0.8.3"
    id("co.uzzu.dotenv.gradle") version "4.0.0"
}

fun prop(key: String) = property(key) as String

version = prop("mod_version")
group = prop("mod_group_id")

repositories {
    mavenLocal()

    maven("https://maven.neoforged.net")
    maven("https://maven.parchmentmc.org")

    // Regolith
    maven("https://codeberg.org/EmmaTheMartian/regolith/raw/branch/main/repo/")

    // Dapper
    maven("https://codeberg.org/EmmaTheMartian/dapper/raw/branch/main/repo/")

    // Kotlin For Forge
    maven("https://thedarkcolour.github.io/KotlinForForge/")

    // EMI
    maven("https://maven.terraformersmc.com/")

    // Legacy Landscape
    maven("https://maven.muonmc.org/releases/")

    // CC Tweaked
    maven("https://maven.squiddev.cc") {
        content {
            includeGroup("cc.tweaked")
        }
    }

    // Jade, AE2, Mekanism, Cable Facades, Fusion
    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven")
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
}

base {
    archivesName = prop("mod_id")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
    withSourcesJar()
    withJavadocJar()
}

kotlin.compilerOptions.jvmTarget = JvmTarget.JVM_21

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")
            exclude("*.cache/")
        }
    }

    create("data") {
        kotlin {
            srcDirs("src/data/kotlin")
        }
        compileClasspath += main.get().compileClasspath + main.get().output
        runtimeClasspath += main.get().runtimeClasspath + main.get().output
    }
}

tasks.compileKotlin {
    source(sourceSets["data"].kotlin)
}

loom {
    runs {
        all {
            vmArg("-XX:+AllowEnhancedClassRedefinition")
        }

        register("data") {
            data()

            source(sourceSets["data"])

            programArgs("--all", "--mod", prop("mod_id"),
                    "--output", file("src/generated/resources/").absolutePath,
                    "--existing", file("src/main/resources/").absolutePath)
        }
    }
}

yamlang {
    targetSourceSets = listOf(sourceSets.main.get())
    inputDir = "assets/${prop("mod_id")}/lang"
}

dependencies {
    minecraft("com.mojang:minecraft:${prop("minecraft_version")}")

    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${prop("parchment_minecraft_version")}:${prop("parchment_mappings_version")}@zip")
    })

    neoForge("net.neoforged:neoforge:${prop("neo_version")}")

    // Regolith
    include(implementation("martian:regolith-neoforge:${prop("regolith_version")}")!!)

    // Dapper (for datagen)
    implementation("martian:dapper:${prop("dapper_version")}")

    // KotlinForForge (for datagen)
    implementation("thedarkcolour:kotlinforforge-neoforge:${prop("kff_version")}") {
        // https://github.com/thedarkcolour/KotlinForForge/issues/103
        exclude(group = "net.neoforged.fancymodloader", module = "loader")
    }

    // EMI
    compileOnly("dev.emi:emi-neoforge:${prop("emi_version")}:api")
    runtimeOnly("dev.emi:emi-neoforge:${prop("emi_version")}")

    // Legacy Landscape
    modLocalRuntime("gay.sylv.legacy_landscape:legacy_landscape:${prop("legacy_landscape_version")}") { isTransitive = false }

    // ComputerCraft
    forgeRuntimeLibrary("cc.tweaked:cobalt:0.9.3") // Gradle doesn't get this automatically from the CC Tweaked dependency, for whatever reason
    modLocalRuntime("cc.tweaked:cc-tweaked-${prop("minecraft_version")}-forge:${prop("cc_version")}")

    // Modrinth maven dependencies
    fun modrinth(id: String) = "maven.modrinth:$id:${prop("${id.replace('-', '_')}_version")}"
    fun enabled(id: String) = prop("enable_$id") == "true"

    if (enabled("jade"))
        modLocalRuntime(modrinth("jade"))
    if (enabled("ae2"))
        modLocalRuntime(modrinth("ae2"))
    if (enabled("mekanism"))
        modLocalRuntime(modrinth("mekanism"))
    if (enabled("cable_facades"))
        modLocalRuntime(modrinth("cable-facades"))
    if (enabled("fusion_connected_textures"))
        modLocalRuntime(modrinth("fusion-connected-textures"))
    if (enabled("continuity"))
        modLocalRuntime(modrinth("continuity"))
    if (enabled("athena_ctm"))
        modLocalRuntime(modrinth("athena-ctm"))
}

// This block of code expands all declared replace properties in the specified resource targets.
// A missing property will result in an error. Properties are expanded using ${} Groovy notation.
val replaceProperties = mapOf(
        "minecraft_version"       to prop("minecraft_version"),
        "minecraft_version_range" to prop("minecraft_version_range"),
        "neo_version"             to prop("neo_version"),
        "neo_version_range"       to prop("neo_version_range"),
        "loader_version_range"    to prop("loader_version_range"),
        "mod_id"                  to prop("mod_id"),
        "mod_name"                to prop("mod_name"),
        "mod_license"             to prop("mod_license"),
        "mod_version"             to prop("mod_version"),
        "mod_authors"             to prop("mod_authors"),
        "mod_description"         to prop("mod_description"),
)

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

// Include the output of "generateModMetadata" as an input directory for the build
// this works with both building through Gradle and the IDE.
sourceSets.main.get().resources.srcDir(generateModMetadata)
// To avoid having to run "generateModMetadata" manually, make it run on every project reload
tasks.ideaSyncTask.configure {
    dependsOn(generateModMetadata)
}

// Exclude the `data` source set from any jars build
tasks.withType<Jar>().configureEach {
    exclude("martian/minefactorial/datagen/*")
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven("file://${project.projectDir}/repo")
    }
}

publishMods {
    type = STABLE
    modLoaders.add("neoforge")

    file = tasks.remapJar.get().archiveFile
    additionalFiles.from(tasks.remapSourcesJar.get().archiveFile)

    var changelogText = "# ${prop("version")}\n\n"
    // Read the latest version from the changelog
    changelogText += File(rootDir, "changelog.md")
        .readText(StandardCharsets.UTF_8)
        .split(Regex("^#(?!#).*$", RegexOption.MULTILINE))[1]
        .trim()
    println(changelogText)
    changelog = changelogText

//    dryRun = true

    curseforge {
        projectId = "1144883"
        projectSlug = "minefactorial"
        accessToken = env.CURSEFORGE_TOKEN.value
        announcementTitle = "Get from CurseForge"

        minecraftVersions.add("1.21.1")
        javaVersions.add(JavaVersion.VERSION_21)
        javaVersions.add(JavaVersion.VERSION_22)
        clientRequired = true
        serverRequired = true

        optional("emi")
        optional("fusion-connected-textures")
    }

    modrinth {
        projectId = "4sjHMjq5"
        accessToken = env.MODRINTH_TOKEN.value
        announcementTitle = "Get from Modrinth"

        minecraftVersions.add("1.21.1")

        optional("emi")
        optional("fusion-connected-textures")
    }

    github {
        repository = "emmathemartian/minefactorial"
        // My branches are named by the minecraft version they target
        commitish = prop("minecraft_version")
        accessToken = env.GITHUB_TOKEN.value
        announcementTitle = "Get from GitHub"

        allowEmptyFiles = true
    }

    discord {
        val avatar = "https://raw.githubusercontent.com/EmmaTheMartian/minefactorial/refs/heads/1.21.1/src/main/resources/assets/minefactorial/icon.png"

        webhookUrl = env.DISCORD_WEBHOOK_URL.value
        username = "MineFactorial Build Bot"
        avatarUrl = avatar

        style {
            look = "MODERN"
            thumbnailUrl = avatar
        }
    }
}

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
