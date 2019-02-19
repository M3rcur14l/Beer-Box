package com.m3rc.beerbox.widget.searchview;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import com.m3rc.beerbox.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Antonello Fodde on 11/10/2017.
 * antonello.fodde@accenture.com
 */

public class FloatingSearchView extends FrameLayout {

    private static final int SUGGESTION_DELAY = 300;

    private final Intent voiceIntent;
    private final SearchView searchView;
    private final EditText searchTextView;
    private final ImageView closeButton;
    private final View voiceButton;
    private final ProgressBar progress;
    private final int[] searchViewPosition;
    private final Subject<Object> querySubmitSubject = PublishSubject.create().toSerialized();
    private final Subject<Object> queryInputSubject = PublishSubject.create().toSerialized();
    private final CompositeDisposable disposables = new CompositeDisposable();
    private ViewGroup suggestionsContainer;
    private Transition suggestionTransition;
    private SuggestionAdapter suggestionAdapter;
    private Consumer<String> onSubmit;
    private boolean skipSuggestions;
    private RecyclerView suggestionRecyclerView;
    private QuerySuggestionProvider querySuggestionProvider;
    private VoiceSearchProvider voiceSearchProvider;

    public FloatingSearchView(@NonNull Context context) {
        this(context, null);
    }

    public FloatingSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingSearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.floating_search_layout, this);
        searchView = findViewById(R.id.floating_search_query_text);
        searchTextView = findViewById(androidx.appcompat.R.id.search_src_text);
        closeButton = findViewById(androidx.appcompat.R.id.search_close_btn);
        voiceButton = findViewById(R.id.floating_search_query_voice);
        progress = findViewById(R.id.floating_search_query_progress);
        searchViewPosition = new int[2];

        voiceIntent = getVoiceSearchIntent();
        if (voiceIntent != null)
            voiceButton.setOnClickListener(this::onVoiceClick);
        else
            voiceButton.setVisibility(GONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                querySubmitSubject.onNext(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryInputSubject.onNext(newText);
                return true;
            }
        });
        createSearchTextViewListener();
    }

    public void createSearchTextViewListener() {
        searchTextView.setOnFocusChangeListener((v, hasFocus) ->
                post(() -> onFocusChanged(hasFocus)));
    }

    SuggestionAdapter onCreateSuggestionAdapter() {
        return new SuggestionAdapter(new ArrayList<>(),
                R.drawable.beer);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) return;

        disposables.add(queryInputSubject
                .debounce(SUGGESTION_DELAY, TimeUnit.MILLISECONDS)
                .ofType(String.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onInput));
        disposables.add(querySubmitSubject
                .ofType(String.class)
                .subscribe(this::onSubmit));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        disposables.clear();
    }

    private void onFocusChanged(boolean hasFocus) {
        voiceButton.setVisibility(hasFocus ? VISIBLE : GONE);
        closeButton.setVisibility(hasFocus ? VISIBLE : GONE);
        if (hasFocus)
            onInput(searchTextView.getText().toString());
        else
            setSuggestionsVisibility(GONE);
    }

    private void onInput(String input) {
        if (querySuggestionProvider != null && !skipSuggestions)
            disposables.add(querySuggestionProvider.getSuggestions(input)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(suggestions -> this.onShowSuggestion(suggestions, input),
                            Throwable::printStackTrace));
        else if (skipSuggestions)
            skipSuggestions = false;
    }

    private void onShowSuggestion(List<Suggestion> suggestions, String input) {
        if (suggestionRecyclerView != null && suggestionRecyclerView.getChildCount() > 0) {
            suggestionRecyclerView.removeAllViews();
        }
        suggestionAdapter.setSuggestions(suggestions, input);
        updateSuggestionsVisibility(suggestions);
    }

    private void onSubmit(String input) {
        queryInputSubject.onNext(""); //To avoid reopening of debounced suggestions
        searchView.clearFocus();
        setProgress(true);
        if (onSubmit != null)
            onSubmit.accept(input);
    }

    public void setOnSubmit(Consumer<String> onSubmit) {
        this.onSubmit = onSubmit;
    }

    public void setOnSuggestionSelected(Consumer<Suggestion> onSuggestionSelected) {
        if (suggestionAdapter != null)
            suggestionAdapter.setOnSuggestionSelected(suggestion -> {
                searchView.clearFocus();
                setSuggestionsVisibility(GONE);
                skipSuggestions = true;
                searchView.setQuery(suggestion.getEntry(), false);
                setProgress(true);
                onSuggestionSelected.accept(suggestion);
            });
    }

    public void setSuggestionsContainer(ViewGroup suggestionsContainer) {
        this.suggestionsContainer = suggestionsContainer;
        suggestionsContainer.setVisibility(INVISIBLE);
        suggestionTransition = new Slide(Gravity.TOP);
        suggestionsContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                suggestionsContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                getLocationInWindow(searchViewPosition);
                MarginLayoutParams lp = (MarginLayoutParams) suggestionsContainer.getLayoutParams();
                suggestionsContainer.setY(searchViewPosition[1]
                        + getHeight() / 2
                        + lp.topMargin);
            }
        });
        View suggestionsLayout = LayoutInflater.from(suggestionsContainer.getContext())
                .inflate(R.layout.floating_search_suggestion_list, suggestionsContainer, false);
        suggestionsContainer.addView(suggestionsLayout);
        suggestionRecyclerView = suggestionsLayout.findViewById(R.id.floating_search_suggestion_recycler_view);
        suggestionAdapter = onCreateSuggestionAdapter();
        suggestionRecyclerView.setLayoutManager(new LinearLayoutManager(suggestionsContainer.getContext()));
        ((SimpleItemAnimator) suggestionRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        suggestionRecyclerView.setAdapter(suggestionAdapter);
        suggestionsContainer.setOnClickListener(v -> setSuggestionsVisibility(GONE));
    }

    private void updateSuggestionsVisibility(List<Suggestion> suggestions) {
        if (suggestionsContainer.getVisibility() == VISIBLE && suggestions.isEmpty()) {
            suggestionsContainer.setVisibility(INVISIBLE);
            suggestionRecyclerView.setVisibility(INVISIBLE);
        } else if (suggestionsContainer.getVisibility() != VISIBLE && !suggestions.isEmpty()) {
            suggestionsContainer.setVisibility(VISIBLE);
            TransitionManager.beginDelayedTransition(suggestionsContainer, suggestionTransition);
            suggestionRecyclerView.setVisibility(VISIBLE);
        }
    }

    public void setSuggestionsVisibility(int visibility) {
        suggestionsContainer.setVisibility(visibility);
    }

    public void setProgress(boolean inProgress) {
        if (inProgress)
            progress.setVisibility(VISIBLE);
        else
            progress.setVisibility(GONE);
    }

    public void setQueryHint(@Nullable CharSequence hint) {
        searchView.setQueryHint(hint);
    }

    public CharSequence getQuery() {
        return searchView.getQuery();
    }

    public void setQuery(@NonNull CharSequence query) {
        searchView.setQuery(query, false);
    }

    public void setQuerySuggestionProvider(QuerySuggestionProvider querySuggestionProvider) {
        this.querySuggestionProvider = querySuggestionProvider;
    }

    public void setVoiceSearchProvider(VoiceSearchProvider voiceSearchProvider) {
        this.voiceSearchProvider = voiceSearchProvider;
        disposables.add(voiceSearchProvider.getVoiceSearch()
                .subscribe(query -> searchView.setQuery(query, true)));
    }

    private Intent getVoiceSearchIntent() {
        Intent voiceIntent = null;

        Intent voiceAppSearchIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        Intent voiceWebSearchIntent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
        voiceWebSearchIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        if (getContext().getPackageManager().resolveActivity(voiceAppSearchIntent,
                PackageManager.MATCH_DEFAULT_ONLY) != null) {
            voiceIntent = voiceAppSearchIntent;
        } else if (getContext().getPackageManager().resolveActivity(voiceWebSearchIntent,
                PackageManager.MATCH_DEFAULT_ONLY) != null) {
            voiceIntent = voiceWebSearchIntent;
        }
        if (voiceIntent != null)
            voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        return voiceIntent;
    }

    @SuppressWarnings("unused")
    protected void onVoiceClick(View v) {
        try {
            if (getContext() instanceof Activity && voiceSearchProvider != null)
                ((Activity) getContext()).startActivityForResult(voiceIntent, voiceSearchProvider.getRequestCode());
        } catch (ActivityNotFoundException e) {
            // Should not happen, since we check the availability of
            // voice search before showing the button. But just in case...
            Log.w(this.getClass().getSimpleName(), "Could not find voice search activity");
        }
    }

    /**
     * Clean the search and remove autosuggestion
     */
    public void cleanSearchView(boolean skipSuggestions) {
        searchTextView.setText("");
        searchTextView.clearFocus();
        searchView.clearFocus();
        this.skipSuggestions = skipSuggestions;
        setSuggestionsVisibility(GONE);
    }

    public void removeFocusFromTextView() {
        searchTextView.clearFocus();
    }

}
