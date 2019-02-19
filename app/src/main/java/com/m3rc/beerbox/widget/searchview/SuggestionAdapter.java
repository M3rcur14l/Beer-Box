package com.m3rc.beerbox.widget.searchview;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;
import com.m3rc.beerbox.R;

import java.util.List;

/**
 * Created by Antonello Fodde on 13/10/2017.
 * antonello.fodde@accenture.com
 */
public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder> {

    private final int[] suggestionIcons;
    private String input;
    private List<Suggestion> suggestions;
    private Consumer<Suggestion> onSuggestionSelected;

    SuggestionAdapter(List<Suggestion> suggestions, @DrawableRes int... suggestionIcons) {
        this.suggestions = suggestions;
        this.suggestionIcons = suggestionIcons;
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.floating_search_suggestion_row, parent, false);
        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        Suggestion suggestion = suggestions.get(position);
        if (suggestionIcons != null && suggestion.getType() < suggestionIcons.length)
            holder.suggestionIcon.setImageResource(suggestionIcons[suggestion.getType()]);
        SpannableString stringForSuggestion = new SpannableString(suggestion.getEntry());
        int indexOfFoundElem = suggestion.getEntry().toLowerCase().indexOf(input.toLowerCase());

        if (indexOfFoundElem != -1) {
            stringForSuggestion.setSpan(new StyleSpan(Typeface.BOLD), indexOfFoundElem, indexOfFoundElem + input.length(), 0);
        }

        holder.suggestionEntry.setText(stringForSuggestion);

        holder.itemView.setOnClickListener(v -> {
            if (onSuggestionSelected != null)
                onSuggestionSelected.accept(suggestion);
        });
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    void setSuggestions(List<Suggestion> suggestions, String input) {
        this.suggestions = suggestions;
        this.input = input;
        notifyDataSetChanged();
    }

    void setOnSuggestionSelected(Consumer<Suggestion> onSuggestionSelected) {
        this.onSuggestionSelected = onSuggestionSelected;
    }

    static class SuggestionViewHolder extends RecyclerView.ViewHolder {

        final ImageView suggestionIcon;
        final TextView suggestionEntry;

        SuggestionViewHolder(View itemView) {
            super(itemView);
            suggestionIcon = itemView.findViewById(R.id.floating_search_suggestion_icon);
            suggestionEntry = itemView.findViewById(R.id.floating_search_suggestion_entry);
        }
    }
}
