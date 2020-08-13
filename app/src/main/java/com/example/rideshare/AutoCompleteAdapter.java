package com.example.rideshare;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * An ArrayAdapter which alters the size of the dropdown based on the count of the results.
 * If the number of items in the dropdown is less than 4, its contents are wrapped,
 * otherwise its height to set to a specific value.
 *
 * @author Sukriti
 * @version 1.0
 */
public class AutoCompleteAdapter extends ArrayAdapter {
    private List<String> tempItems;
    private List<String> suggestions;
    private AutoCompleteTextView dropdown;

    private static final int COUNT_THRESHOLD = 4;
    private static final int DROPDOWN_HEIGHT = 350;

    public AutoCompleteAdapter(Context context, int resource, List<String> items, AutoCompleteTextView view) {
        super(context, resource, 0, items);
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
        dropdown = view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (String names : tempItems) {
                    if (names.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(names);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                suggestions.clear();
                for (String names : tempItems) {
                    suggestions.add(names);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<String> filterList = (ArrayList<String>) results.values;

            if (results != null && results.count > 0) {
                if (results.count < COUNT_THRESHOLD)
                    dropdown.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                else
                    dropdown.setDropDownHeight(DROPDOWN_HEIGHT);
            }

            if (results != null && results.count > 0) {
                clear();
                for (String item : filterList) {
                    add(item);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
