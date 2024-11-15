package org.example

import kotlinx.serialization.json.Json
import org.example.model.Category
import org.example.model.Recipe
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

fun main() {

    var categoryList: List<Category> = listOf()
    var categoriesIdList: List<Int> = listOf()


    val thread =
        Thread { // инициализируем URL, который храит адрес на который мы обращаемся для запроса получения данных
            categoryList = loadCategories()
            println(categoryList)

            categoriesIdList = categoryList.map { it.id }
            println(categoriesIdList)
        }
    // На андроид необходимо выполнять эту задачу в другом потоке
    // Добавляем разрешение
    thread.start()

    // Объявляем пул потоков
    val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

    // Выполняем задачи в пуле потоков
    threadPool.execute {
        thread.join()

        categoriesIdList.forEach {
            println(loadRecipesList(it))
        }

    }
}

private fun loadRecipesList(categoryId: Int): List<Recipe> {
    val url = URL("https://recipes.androidsprint.ru/api/category/$categoryId/recipes")
    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
    connection.connect()
    val string = connection.inputStream.bufferedReader().readText()
    val recipesList: List<Recipe> = Json.decodeFromString(string)

    return recipesList
}

private fun loadCategories(): List<Category> {
    val url = URL("https://recipes.androidsprint.ru/api/category")
    // Получаем объект URL Connection
    // Приводим к типу HTTP, тк действуем по протоколу HTTP
    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
    // Добавляем метод connect - означает соединение по урлу
    connection.connect()
    // получаем тело запроса
    val string = connection.inputStream.bufferedReader().readText()
    println(string)

    return Json.decodeFromString(string)
}