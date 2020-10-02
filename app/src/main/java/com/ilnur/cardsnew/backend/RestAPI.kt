package com.ilnur.cardsnew.backend

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.ilnur.cardsnew.utils.Resource
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface API {

    @POST("/api")
    suspend fun auth(
        @Query("user") login: String,
        @Query("password") password: String,
        @Query("type") type: String = "login",
        @Query("protocolVersion") protocolVersion: Int = 1,
    ): Response<ResponseRest>

    @GET
    suspend fun getSubjTopics(@Url url: String): Response<ResponseTopics>

    @GET
    suspend fun getCatCards(@Url url: String): Response<ResponseCards>

    /*@FormUrlEncoded
    @GET
    fun auth(
        @Url url: String, @Field("user") login: String, @Field("password") password: String,
        @Field("type") type: String = "login", @Field("protocolVersion") protVersion: Int = 1
    ): Call<ResponseRest>*/
}


abstract class BaseApiResponse{
    var status: Int = 0
    var message: String? = null
}

data class ResponseTopics(
    val data: List<CategoryResp>?
)

data class ResponseCards(
    val data: List<CardResp>?
)

data class CardResp(
    @SerializedName("id") val id: Int,
    //@SerializedName("subj")val subj: String,
    @SerializedName("avers") val avers: String?,
    @SerializedName("revers") val revers: String?,
    @SerializedName("category_id")  val category_id: Int?,
    @SerializedName("result") val result: Int?,
    @SerializedName("result_stamp")  val result_stamp: String?)

data class CategoryResp(
    @SerializedName("id")val id: Int?,
    @SerializedName("title")val title: String?,
    @SerializedName("parent_id")val parent_id: Int?,
    @SerializedName("reversible")val reversible: Int?,
    @SerializedName("order")val order: Int?,
    @SerializedName("stamp")val stamp: String?
)

@Keep
data class ResponseRest(
    @SerializedName("error") val error: String?,
    @SerializedName("data") val data: RespData?
)

@Keep
data class RespData(
    @SerializedName("session") val session: String
)


object Protocol {
    var protocolVersion = "protocolVersion=1"
}