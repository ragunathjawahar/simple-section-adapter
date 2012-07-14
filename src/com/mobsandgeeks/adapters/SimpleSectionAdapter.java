/*
 * Copyright (C) 2012 Mobs and Geeks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobsandgeeks.adapters;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A very simple adapter that adds sections to adapters written for {@link ListView}s.
 * <br />
 * <b>NOTE: The adapter assumes that the data source of the decorated list adapter is sorted.</b>
 * 
 * @author Ragunath Jawahar R <rj@mobsandgeeks.com>
 * @version 0.2
 */
public class SimpleSectionAdapter<T> extends BaseAdapter {
    // Debug
    static final boolean DEBUG = false;
    static final String TAG = SimpleSectionAdapter.class.getSimpleName();
    
    // Constants
    private static final int VIEW_TYPE_SECTION_HEADER = 0;
    
    // Attributes
    private Context context;
    private BaseAdapter listAdapter;
    private int sectionHeaderLayoutId;
    private int sectionTitleTextViewId;
    private Sectionizer<T> sectionizer;
    private LinkedHashMap<String, Integer> sections;
    
    /**
     * Constructs a {@linkplain SimpleSectionAdapter}.
     * 
     * @param context The context for this adapter.
     * @param listAdapter A {@link ListAdapter} that has to be sectioned.
     * @param sectionHeaderLayoutId Layout Id of the layout that is to be used for the section header. 
     * @param sectionTitleTextViewId Id of a TextView present in the section header layout.
     * @param sectionizer Sectionizer for sectioning the {@link ListView}.
     */
    public SimpleSectionAdapter(Context context, BaseAdapter listAdapter, 
            int sectionHeaderLayoutId, int sectionTitleTextViewId, 
            Sectionizer<T> sectionizer) {
        if(context == null) {
            throw new IllegalArgumentException("context cannot be null.");
        } else if(listAdapter == null) {
            throw new IllegalArgumentException("listAdapter cannot be null.");
        } else if(sectionizer == null) {
            throw new IllegalArgumentException("sectionizer cannot be null.");
        } else if(!isTextView(context, sectionHeaderLayoutId, sectionTitleTextViewId)) {
            throw new IllegalArgumentException("sectionTitleTextViewId should be a TextView.");
        }
        
        this.context = context;
        this.listAdapter = listAdapter;
        this.sectionHeaderLayoutId = sectionHeaderLayoutId;
        this.sectionTitleTextViewId = sectionTitleTextViewId;
        this.sectionizer = sectionizer;
        this.sections = new LinkedHashMap<String, Integer>();
        
        // Find sections
        findSections();
    }
    
    private boolean isTextView(Context context, int layoutId, int textViewId) {
        View inflatedView = View.inflate(context, layoutId, null);
        View foundView = inflatedView.findViewById(textViewId);
        
        return foundView instanceof TextView;
    }
    
    @Override
    public int getCount() {
        return listAdapter.getCount() + getSectionCount();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        SectionHolder sectionHolder = null;
        
        switch (getItemViewType(position)) {
        case VIEW_TYPE_SECTION_HEADER:
            if(view == null) {
                view = View.inflate(context, sectionHeaderLayoutId, null);
                
                sectionHolder = new SectionHolder();
                sectionHolder.titleTextView = (TextView) view.findViewById(sectionTitleTextViewId);
                
                view.setTag(sectionHolder);
            } else {
                sectionHolder = (SectionHolder) view.getTag();
            }
            break;

        default:
            view = listAdapter.getView(getIndexForPosition(position), 
                    convertView, parent);
            break;
        }
        
        if(sectionHolder != null) {
            String sectionName = sectionTitleForPosition(position);
            sectionHolder.titleTextView.setText(sectionName);
        }
        
        return view;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return listAdapter.areAllItemsEnabled() ? 
                sections.size() == 0 : false;
    }

    @Override
    public int getItemViewType(int position) {
        int positionInCustomAdapter = getIndexForPosition(position);
        return sections.values().contains(position) ? 
                VIEW_TYPE_SECTION_HEADER : 
                    listAdapter.getItemViewType(positionInCustomAdapter) + 1;
    }

    @Override
    public int getViewTypeCount() {
        return listAdapter.getViewTypeCount() + 1;
    }

    @Override
    public boolean isEnabled(int position) {
        return sections.values().contains(position) ? 
                false : listAdapter.isEnabled(getIndexForPosition(position));
    }
    
    @Override
    public Object getItem(int position) {
        return listAdapter.getItem(getIndexForPosition(position));
    }

    @Override
    public long getItemId(int position) {
        return listAdapter.getItemId(getIndexForPosition(position));
    }
    
    @Override
    public void notifyDataSetChanged() {
        listAdapter.notifyDataSetChanged();
        findSections();
        super.notifyDataSetChanged();
    }
    
    /**
     * Returns the actual index of the object in the data source linked to the this list item.
     * 
     * @param position List item position in the {@link ListView}.
     * @return Index of the item in the wrapped list adapter's data source.
     */
    public int getIndexForPosition(int position) {
        int nSections = 0;
        
        Set<Entry<String, Integer>> entrySet = sections.entrySet();
        for(Entry<String, Integer> entry : entrySet) {
            if(entry.getValue() < position) {
                nSections++;
            }
        }
        
        return position - nSections;
    }

    static class SectionHolder {
        public TextView titleTextView;
    }
    
    private void findSections() {
        int n = listAdapter.getCount();
        int nSections = 0;
        sections.clear();
        
        for(int i=0; i<n; i++) {
            @SuppressWarnings("unchecked")
            String sectionName = sectionizer.getSectionTitleForItem(
                    (T) listAdapter.getItem(i));
            
            if(!sections.containsKey(sectionName)) {
                sections.put(sectionName, i + nSections);
                nSections ++;
            }
        }
        
        if(DEBUG) {
            Log.d(TAG, String.format("Found %d sections.", sections.size()));
        }
    }

    private int getSectionCount() {
        return sections.size();
    }

    private String sectionTitleForPosition(int position) {
        String title = null;
        
        Set<Entry<String, Integer>> entrySet = sections.entrySet();
        for(Entry<String, Integer> entry : entrySet) {
            if(entry.getValue() == position) {
                title = entry.getKey();
                break;
            }
        }
        
        return title;
    }
}
