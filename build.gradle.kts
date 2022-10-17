plugins {
    val kotlinVersion = "1.5.30"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion


    id("net.mamoe.mirai-console") version "2.9.2"
}

group = "love.huhu"
version = "1.0"
repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencies {
    val CORE_VERSION="2.12.2"
    val CONSOLE_VERSION="2.12.2"
    compileOnly("net.mamoe:mirai-core:$CORE_VERSION") // mirai-core 的 API
    compileOnly("net.mamoe:mirai-console:$CONSOLE_VERSION") // 后端
    api("cn.hutool:hutool-all:5.8.5")
    testImplementation("net.mamoe:mirai-console-terminal:$CONSOLE_VERSION") // 前端, 用于启动测试
}
