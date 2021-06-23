package com.example.learningcurvemvvmrecipeapp.interactors.recipe_list

import com.example.learningcurvemvvmrecipeapp.cache.AppDatabaseFake
import com.example.learningcurvemvvmrecipeapp.cache.RecipeDaoFake
import com.example.learningcurvemvvmrecipeapp.cache.model.RecipeEntityMapper
import com.example.learningcurvemvvmrecipeapp.domain.model.Recipe
import com.example.learningcurvemvvmrecipeapp.network.RecipeService
import com.example.learningcurvemvvmrecipeapp.network.data.MockWebServerResponses.recipeListResponse
import com.example.learningcurvemvvmrecipeapp.network.model.RecipeDtoMapper
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class SearchRecipesTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val appDatabase = AppDatabaseFake()
    private val DUMMY_TOKEN = "fate token"
    private val DUMMY_QUERY = "does not matter"


    // system in test
    private lateinit var searchRecipes: SearchRecipes


    // dependencies
    private lateinit var recipeService: RecipeService
    private lateinit var recipeDao: RecipeDaoFake
    private val dtoMapper = RecipeDtoMapper()
    private val entityMapper = RecipeEntityMapper()

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("/api/recipe/")
        recipeService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(RecipeService::class.java)

        recipeDao = RecipeDaoFake(appDatabase)

        // instantiating the system in test
        searchRecipes = SearchRecipes(
            recipeDao = recipeDao,
            recipeService = recipeService,
            entityMapper = entityMapper,
            dtoMapper = dtoMapper
        )
    }

    /**
     * 1. Are the recipes retrieved from the network?
     * 2. Are the recipes inserted into the cache?
     * 3. Are the recipes then emitted as a FLOW from the cache to the UI
     */
    @Test
    fun getRecipesFromNework_emitRecipesFromCache() : Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(recipeListResponse)
        )

        // confirm the cache is empty to start
        assert(recipeDao.getAllRecipes(1, 30).isEmpty())

        val flowItems = searchRecipes.execute(
            DUMMY_TOKEN, 1, DUMMY_QUERY, true
        ).toList()

        // confirm the cache is no longer empty
        assert(recipeDao.getAllRecipes(1, 30).isNotEmpty())

        // first emission (flowItems[0]) should be LOADING
        assert(flowItems[0].loading)

        // second emission (flowItems[1]) should be the list of recipes
        val recipes = flowItems[1].data
        assert(recipes?.size ?: 0 > 0)

        // confirm they are actually Recipe objects
        assert(recipes?.get(index = 0) is Recipe)

        /**
         * Ensure loading is false now.
         * We are still talking about the 2nd emission i.e.: flowItems[1]
         * Loading should be false now
         */
        assert(!flowItems[1].loading)
    }

    @Test
    fun getRecipesFromNetwork_emitHttpError() : Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody("{}")
        )

        val flowItems = searchRecipes.execute(
            DUMMY_TOKEN, 1, DUMMY_QUERY, true
        ).toList()

        // first emission (flowItems[0]) should be LOADING
        assert(flowItems[0].loading)

        //second emission (flowItems[1]) be ERROR
        val error = flowItems[1].error
        assert(error != null)

        /**
         * Ensure loading is false now.
         * We are still talking about the 2nd emission i.e.: flowItems[1]
         * Loading should be false now
         */
        assert(!flowItems[1].loading)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

}