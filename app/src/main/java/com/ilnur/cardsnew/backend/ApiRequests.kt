package com.ilnur.cardsnew.backend

import android.util.Log
import com.ilnur.cardsnew.database.*
import com.ilnur.cardsnew.viewmodel.MainViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.Response
import javax.inject.Inject


data class LoginData(
    val login: String,
    val password: String
)

enum class DataState {
    NO_ERROR,
    WRONG_DATA,
    ERROR,
    NO_CONNECTION
}

interface ApiRequests{
    suspend fun downloadSubjNotLogged(subject: Subject, viewModel: MainViewModel)

    suspend fun downloadAllSubjs(subjects: List<Subject>, viewModel: MainViewModel)

    suspend fun auth(login: String,password: String, type: String = "login", protocolVersion: Int = 1,
    ): Response<ResponseRest>

    suspend fun getSubjTopics( url: String): Response<ResponseTopics>

    suspend fun getCatCards( url: String): Response<ResponseCards>
}


class ApiRequestsImp @Inject constructor(val api: API, val db: AppDatabase): ApiRequests {

    @Inject
    lateinit var categoryDao: CategoryDao

    @Inject
    lateinit var cardDao: CardDao

    @Inject
    lateinit var subjectDao: SubjectDao

    override suspend fun downloadAllSubjs(subjects: List<Subject>, viewModel: MainViewModel) {
        coroutineScope {
            subjects.forEach { subject ->
                when (subject.isAdded) {
                    false -> async {downloadSubjNotLogged(subject, viewModel)}
                    true -> {Log.d("Subject already added", "${subject.title} already")}
                }
            }
        }
    }

    //download Subject
    override suspend fun downloadSubjNotLogged(subject: Subject, viewModel: MainViewModel) {
        coroutineScope {
            val categories = async { api.getSubjTopics(getCatsUrlNotLogged(subject.href)) }
            categories.await().body().let { respTopics ->
                respTopics?.data?.forEach { respCategory ->
                    val category = convertToCategory(respCategory, subject.href)
                    async { categoryDao.insert(category) }
                    val cards = async { api.getCatCards(getCardsNotLogged(subject.href, category.id)) }
                    cards.await().body().let { respCards ->
                        respCards?.data?.forEach {
                            val card = it.toCard(subject.href)
                            //Log.d("Card downloaded", "${category.title} - ${card.id} downloaded")
                            async { cardDao.insert(card) }
                        }
                    }
                    Log.d("Category downloaded", "${category.title} downloaded")
                }
            }
            Log.d("Subj downloaded", "$subject downloaded")
            async { subjectDao.insert(subject.apply { isAdded = true }) }
            viewModel.updateSubjects(subject.href)
        }
    }

    override suspend fun auth(
        login: String,
        password: String,
        type: String,
        protocolVersion: Int
    ): Response<ResponseRest> = api.auth(login, password, type, protocolVersion)

    override suspend fun getSubjTopics(url: String): Response<ResponseTopics> = api.getSubjTopics(url)

    override suspend fun getCatCards(url: String): Response<ResponseCards>  = api.getCatCards(url)
}

fun getCatsUrlNotLogged(href: String) =
    "https://$href-ege.sdamgia.ru/api?protocolVersion=1&type=card_cat"

fun getCardsNotLogged(href: String, id: Int) =
    "https://$href-ege.sdamgia.ru/api?protocolVersion=1&type=card&category_id=$id"

fun convertToCategory(item: CategoryResp, href: String) =
    Category(
        id = item.id!!,
        subj = href,
        title = item.title,
        parent_id = item.parent_id,
        reversible = item.reversible,
        order = item.order,
        stamp = item.stamp
    )

//fun convertToCard(item: CardResp, href: String) =
fun CardResp.toCard(href: String) = Card(
    id = id,
    subj = href,
    avers = avers,
    revers = revers,
    category_id = category_id,
    result = result,
    result_stamp = result_stamp
)

fun validateSignIn(body: ResponseRest?): DataState {
    if (body == null)
        return DataState.ERROR
    if (body.error != null) {
        return DataState.WRONG_DATA
    }
    if (body.data != null) {
        return DataState.NO_ERROR
    }
    return DataState.ERROR
}
