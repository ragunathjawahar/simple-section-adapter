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

package com.mobsandgeeks.demo;

import com.mobsandgeeks.adapters.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
* @author Ragunath Jawahar R <rj@mobsandgeeks.com>
* @version 0.1
*/
public class SSADemoActivity extends ListActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        // Demos
        String[] demos = { "Array Adapter", "Custom Adapter" };

        // Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
                android.R.layout.simple_list_item_1, demos);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Class<?> clazz = null;

        switch(position) {
        case 0:
            clazz = ArrayAdapterDemoActivity.class;
            break;
        case 1:
            clazz = CustomAdapterDemoActivity.class;
            break;
        }

        if(clazz != null) {
            Intent intent = new Intent(this, clazz);
            startActivity(intent);
        }
    }

}
