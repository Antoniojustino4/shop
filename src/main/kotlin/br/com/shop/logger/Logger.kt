//package br.com.shop.logger
//
//import lombok.extern.slf4j.Slf4j
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import java.io.File
//import java.nio.file.Files
//import java.nio.file.Paths
//import java.nio.file.StandardOpenOption
//import java.time.LocalDate
//import java.time.LocalDateTime
//
//
//@Slf4j
//class Logger {
//    private var logger: Logger
//    private var clazz: String = ""
//
//    init {
//        this.clazz = Thread.currentThread().stackTrace[2].className
//        this.logger = LoggerFactory.getLogger(clazz)
//    }
//
//    private fun logs(text: String){
//        val date = LocalDate.now()
//
//        val file = File("src/main/resources/logs/${date}.txt")
//        if (!file.exists()) {
//            file.createNewFile()
//        }
//
//        val textLog = "[${LocalDateTime.now()}] $clazz: $text"
//        Files.write(Paths.get(file.path), listOf(textLog), StandardOpenOption.APPEND)
//    }
//
//    fun info(text: String){
//        logs(text)
//        logger.info(text)
//    }
//
//    fun debug(text: String){
//        logs(text)
//        logger.debug(text)
//    }
//
//    fun warn(text: String){
//        logs(text)
//        logger.warn(text)
//    }
//
//    fun error(text: String, throwable: Throwable){
//        logs(text)
//        logger.error(text, throwable)
//    }
//
//    fun trace(text: String){
//        logs(text)
//        logger.trace(text)
//    }
//}