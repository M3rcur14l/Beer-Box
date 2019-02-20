package com.m3rc.beerbox.app.beer

import com.m3rc.beerbox.app.LifecycleViewModel
import com.m3rc.beerbox.app.beer.data.BeerRepository
import com.m3rc.beerbox.data.PunkService
import com.m3rc.beerbox.widget.searchview.Suggestion
import io.reactivex.Single
import javax.inject.Inject

private const val SUGGESTIONS = 5

class BeerViewModel @Inject constructor(
    private val beerRepository: BeerRepository,
    private val service: PunkService
) : LifecycleViewModel() {

    val beerList = beerRepository.getPagedList(this)
    var beerNameFilter: String?
        get() = beerRepository.beerNameFilter
        set(value) {
            beerRepository.beerNameFilter = value
        }
    var ebcRange: ClosedFloatingPointRange<Float>?
        get() = beerRepository.ebcRange
        set(value) {
            beerRepository.ebcRange = value
        }

    fun getSuggestion(input: String): Single<List<Suggestion>> =
        service.getBeers(beerName = input, perPage = SUGGESTIONS)
            .toObservable()
            .flatMapIterable { it }
            .map { Suggestion(0, it.name ?: "") }
            .toList()

    fun refreshList(beerNameFilter: String? = null) {
        this.beerNameFilter = beerNameFilter
        beerRepository.invalidate()
    }

    fun refreshList(ebcRange: ClosedFloatingPointRange<Float>? = null) {
        this.ebcRange = ebcRange
        beerRepository.invalidate()
    }

    fun refreshList() {
        this.beerNameFilter = null
        this.ebcRange = null
        beerRepository.invalidate()
    }

}