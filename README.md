# Worldgeneration-Profiler

[
![Curseforge Downloads](http://cf.way2muchnoise.eu/worldgeneration-profiler.svg)
![Curseforge Versions](http://cf.way2muchnoise.eu/versions/worldgeneration-profiler.svg)
](https://www.curseforge.com/minecraft/mc-mods/worldgeneration-profiler)
[
![Discord](https://img.shields.io/discord/297104769649213441?label=Discord)
](https://discordapp.com/invite/QXbWS36)

### Mod which profiles your worldgeneration.

- Download on [curseforge](https://www.curseforge.com/minecraft/mc-mods/worldgeneration-profiler).  
- Find more information on our [website](https://u-team.info/mods/worldgenerationprofiler).
- Updates can be found in the [changelog](CHANGELOG.md).

### How to build this mod

#### Setup Eclipse
- ``./gradlew genEclipseRuns eclipse``
- Import project as existing workspace

#### Setup IntelliJ IDEA
- ``./gradlew genIntellijRuns``
- Import as gradle project

#### Build
- ``./gradlew build``

### How to include this mod

- Repository: [repo.u-team.info](https://repo.u-team.info)
- Artifact: **info.u-team:worldgeneration_profiler-${config.forge.mcversion}:${config.worldgenerationprofiler.version}** 
- *{config.forge.mcversion}* is the minecraft version.
- *{config.worldgenerationprofiler.version}* is the worldgenerationprofiler version.

#### Using in Forge Gradle 3:
```gradle
repositories {
    maven { url = "https://repo.u-team.info" }
}

dependencies {
  compileOnly fg.deobf("info.u-team:worldgeneration_profiler-${config.forge.mcversion}:${config.worldgenerationprofiler.version}")
}
```

### License

- This mod is licensed under apache 2 license. For more information see [here](LICENSE).  
- This mod can be packed in any curseforge modpack you like.

### Issues

- Please report issues to the [github issues](../../issues).
- Include your minecraft version, forge version and mod version.
- Upload your log on [gist](https://gist.github.com) or [pastebin](https://pastebin.com) and include link in your report.
