package com.m3rc.beerbox.app.beer

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import com.m3rc.beerbox.R
import com.m3rc.beerbox.app.BaseActivity
import com.m3rc.beerbox.widget.searchview.QuerySuggestionProvider
import com.m3rc.beerbox.widget.searchview.Suggestion
import com.m3rc.beerbox.widget.searchview.VoiceSearchProvider
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_beer.*

private const val VOICE_SEARCH_REQUEST = 1

class BeerActivity : BaseActivity(), VoiceSearchProvider, QuerySuggestionProvider {

    var fragment: BeerFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer)

        if (savedInstanceState == null) {
            val fragment = BeerFragment.newInstance()

            setupSearchView()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit()

            this.fragment = fragment
        }
    }

    private fun setupSearchView() {
        searchView.setSuggestionsContainer(suggestionContainer)
        searchView.setVoiceSearchProvider(this)
        searchView.setQuerySuggestionProvider(this)
        searchView.setOnSubmit {
            //TODO
        }
        searchView.setOnSuggestionSelected {
            //TODO
        }
        searchView.createSearchTextViewListener()
    }

    override fun getSuggestions(input: String): Single<List<Suggestion>> =
        fragment?.viewModel?.getSuggestion(input) ?: Single.just(emptyList())

    override val voiceSearch = PublishSubject.create<String>().toSerialized()

    override val requestCode = VOICE_SEARCH_REQUEST

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                VOICE_SEARCH_REQUEST -> {
                    val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    if (results != null && !results.isEmpty())
                        voiceSearch.onNext(results[0])
                }
            }
        }
    }
}
