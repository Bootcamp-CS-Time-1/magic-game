package br.com.bootcamp.magicgamecs.mock

import android.content.Context
import android.net.Uri
import br.com.bootcamp.magicgamecs.core.ext.readAsset
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockDispatcher(private val context: Context) : Dispatcher() {

    companion object {
        const val CARD_IMAGE_ASSET = "card.jpeg"
        const val SETS_ASSET = "sets_success.json"
        const val CARDS_10E_100_ASSET = "cards_10E_100_success.json"
        const val CARDS_10E_1_ASSET = "cards_10E_1_success.json"
    }

    private val responseFilesByPath: Map<String, String> = mapOf(
        "/card.jpeg" to CARD_IMAGE_ASSET,
        "/v1/sets" to SETS_ASSET,
        "/v1/cards?set=10E" to CARDS_10E_100_ASSET,
        "/v1/cards?set=10E" to CARDS_10E_1_ASSET
    )

    private val queue = arrayListOf<String>()

    fun enqueue(asset: String) {
        queue.add(asset)
    }

    override fun dispatch(request: RecordedRequest?): MockResponse {
        val errorResponse = MockResponse().setResponseCode(404)

        val pathWithoutQueryParams = Uri.parse(request?.path).path ?: return errorResponse
        val responseFile = responseFilesByPath[pathWithoutQueryParams]
            ?: queue.takeIf { it.isNotEmpty() }?.removeAt(0)

        return if (responseFile != null) {
            val responseBody = context.readAsset(responseFile)
            MockResponse().setResponseCode(200).setBody(responseBody)
        } else {
            errorResponse
        }
    }
}