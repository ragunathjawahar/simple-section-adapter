package com.mobsandgeeks.adapters.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mobsandgeeks.adapters.R;
import com.mobsandgeeks.adapters.Sectionizer;
import com.mobsandgeeks.adapters.SimpleSectionAdapter;

import android.app.ListActivity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapterDemoActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        
        // 1. Your data source
        List<City> cities = getCities();
        
        // 2. Sort them using the distance from the current city
        City chennai = new City("Chennai", 13.15, 80.283333);
        DistanceComparator distanceComparator = new DistanceComparator(chennai);
        Collections.sort(cities, distanceComparator);
        
        // 3. Create your custom adapter
        CityAdapter cityAdapter = new CityAdapter(this, R.layout.list_item_city, cities);
        
        // 4. Create a Sectionizer
        DistanceSectionizer distanceSectionizer = new DistanceSectionizer(chennai);
        
        // 5. Wrap your adapter within the SimpleSectionAdapter
        SimpleSectionAdapter<City> sectionAdapter = new SimpleSectionAdapter<City>(this, 
                cityAdapter, R.layout.section_header, R.id.title, distanceSectionizer);
        
        // 6. Set the adapter to your ListView
        setListAdapter(sectionAdapter);

    }
    
    /*
     * A custom adapter that extends the ArrayAdapter<T>.
     */
    class CityAdapter extends ArrayAdapter<City> {
        private List<City> cities;

        public CityAdapter(Context context, int textViewResourceId,
                List<City> cities) {
            super(context, textViewResourceId, cities);
            this.cities = cities;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Holder holder = null;
            
            if(view == null) {
                view = View.inflate(CustomAdapterDemoActivity.this, 
                        R.layout.list_item_city, null);
                
                holder = new Holder();
                holder.cityTextView = (TextView) view.findViewById(R.id.city);
                holder.geoPointTextView = (TextView) view.findViewById(R.id.geo_point);
                
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            
            // Set properties
            City city = cities.get(position);
            holder.cityTextView.setText(city.getName());
            holder.geoPointTextView.setText(String.format("lat: %f, long: %f", 
                    city.getLatitude(), city.getLongitude()));
            
            return view;
        }
        
    }
    
    /*
     * Holder for the custom adapter.
     */
    static class Holder {
        public TextView cityTextView;
        public TextView geoPointTextView;
    }
    
    /*
     * Sectionizer supplies labels based on the distance from the current city.
     */
    class DistanceSectionizer implements Sectionizer<City> {
        private City currentCity;
        
        public DistanceSectionizer(City currentCity) {
            this.currentCity = currentCity;
        }

        @Override
        public String getSectionTitleForItem(City city) {
            String sectionTitle = "Unknown";
            
            float distance = getDistanceInKm(currentCity, city);
            if(distance < 500) {
                sectionTitle = "Less than 500 km";
            } else if(distance < 1000) {
                sectionTitle = "500 - 999 km";
            } else if(distance < 2000) {
                sectionTitle = "1000 - 1999 km";
            } else if(distance < 3000) {
                sectionTitle = "2000 - 2999 km";
            } else if(distance < 5000) {
                sectionTitle = "3000 - 4999 km";
            } else if(distance < 8000){
                sectionTitle = "5000 - 7999 km";
            } else {
                sectionTitle = "More than 8000 km";
            }
            
            return sectionTitle;
        }
    }
    
    /*
     * This Comparator sorts cities based on their distances from the current city.
     */
    class DistanceComparator implements Comparator<City> {
        private City currentCity;
        
        public DistanceComparator(City currentCity) {
            this.currentCity = currentCity;
        }

        @Override
        public int compare(City city1, City city2) {            
            float currentCityToCity1 = getDistanceInKm(currentCity, city1);
            float currentCityToCity2 = getDistanceInKm(currentCity, city2);
            
            return currentCityToCity1 > currentCityToCity2 ? 1 : 
                currentCityToCity1 < currentCityToCity2 ? -1 : 0;
        }
    }
    
    /*
     * Method finds distance in kilometers between two given cities.
     */
    private float getDistanceInKm(City sourceCity, City destinationCity) {
        Location sourceCityLocation = new Location("");
        sourceCityLocation.setLatitude(sourceCity.getLatitude());
        sourceCityLocation.setLongitude(sourceCity.getLongitude());
        
        Location destinationCityLocation = new Location("");
        destinationCityLocation.setLatitude(destinationCity.getLatitude());
        destinationCityLocation.setLongitude(destinationCity.getLongitude());
        
        return sourceCityLocation.distanceTo(destinationCityLocation) / 1000;
    }
    
    /*
     * This is your data source. In reality, this could come from a SQLite database,
     * a remote server or a flat-file.
     */
    private List<City> getCities() {
        List<City> cities = new ArrayList<City>();
        
        cities.add(new City("Alert", 82.466667, 62.5));
        cities.add(new City("Bangalore", 12.966667, 77.583333));
        cities.add(new City("Oslo", 59.933333, 10.75));
        cities.add(new City("Stockholm", 59.35, 18.066667));
        cities.add(new City("Riga", 56.966667, 24.133333));
        cities.add(new City("Glasgow", 55.866667, 4.25));
        cities.add(new City("Ufa", 54.75, 55.966667));
        cities.add(new City("Liverpool", 53.483333, 2.983333));
        cities.add(new City("Amsterdam", 52.366667, 4.9));
        cities.add(new City("The Hague", 52.066667, 4.3));
        cities.add(new City("Prague", 50.083333, 14.433333));
        cities.add(new City("Hammerfest", 70.65, 23.683333));
        cities.add(new City("Winnipeg", 49.9, 97.133333));
        cities.add(new City("Paris", 48.866667, 2.333333));
        cities.add(new City("Seattle", 47.6, 122.316667));
        cities.add(new City("Budapest", 47.466667, 19.05));
        cities.add(new City("Venice", 45.433333, 12.316667));
        cities.add(new City("Naples", 40.833333, 14.25));
        cities.add(new City("Beijing", 39.9, 116.383333));
        cities.add(new City("Cincinnati", 39.133333, 84.05));
        cities.add(new City("Mashhad", 36.3, 59.616667));
        cities.add(new City("Tokyo", 35.683333, 139.766667));
        cities.add(new City("Srinagar", 34.083333, 74.783333));
        cities.add(new City("Amristar", 31.633333, 74.85));
        cities.add(new City("Austin", 30.3, 97.75));
        cities.add(new City("Udon Thani", 17.416667, 102.75));
        cities.add(new City("Manila", 14.6, 120.983333));
        cities.add(new City("Bangkok", 13.75, 100.516667));
        cities.add(new City("Singapore", 1.283333, 103.85));
        cities.add(new City("Colombo", 6.9, 79.833333));
        cities.add(new City("Hong Kong", 22.283333, 114.133333));
        cities.add(new City("Mumbai", 18.966667, 72.816667));
        cities.add(new City("Penang", 5.416667, 100.316667));
        cities.add(new City("Sydney", -33.866667, 151.216667));
        
        return cities;
    }
}
