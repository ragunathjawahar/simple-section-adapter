About
-----

This is the **SIMPLEST** [Section Adapter][google-search] available for Android's [ListView][list-view-link]. It works with list adapters that you already have. No project specific dependencies. Just include the [latest jar][jar-download] or the [sources][sources-download-link] to your Android project.

Pros
----

  - The simplest implementation yet, you can create a sectioned ListView with just *6 lines* of code.
  - Zero project specific dependencies.

Compatibility
-------------

  - Android 1.6 and up

Usage
-----
```java
// 1. Create a Sectionizer    
class BookSectionizer implements Sectionizer<Book> {

    @Override
    public String getSectionTitleForItem(Book book) {
        return book.getGenre();
    }
}

// 2. Wrap your existing adapter with the SimpleSectionAdapter
SimpleSectionAdapter<Book> sectionAdapter = new SimpleSectionAdapter<Book>(context, 
        yourBookAdapter, R.layout.section_header, R.id.title, 
        new BookSectionizer());
    
// 3. Set the SimpleSectionAdapter to your ListView
listView.setAdapter(sectionAdapter);
```

Also you can check a [complete example][simple-example-link] for a quick start. The [sources][sources-download-link] have a few more examples as well.

**NOTE: The data source (Cursor, ArrayList or Array) provided to your Adapter should be sorted in a logical way you want them to be sectioned. For instance, if you want to section your books by genres, they have to be sorted by genres before you wrap it within the [SimpleSectionAdapter][simple-section-adapter].**

Screenshots
-------------------
<img src="http://mobsandgeeks.com/images/android/ssa-ss1.png"> &nbsp; <img src="http://mobsandgeeks.com/images/android/ssa-ss2.png">


OnItemClickListener
-------------------
While using an [OnItemClickListener][item-click-listener] instead of using the list item's position directly, use it as shown below.

```java
@Override
public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
    // 1. You could do this
    City city = (City) sectionAdapter.getItem(position);
        
    // 2. Or you could do this :)
    int index = sectionAdapter.getIndexForPosition(position);
    City sameCity = cities.get(index);

    // More code…
}
```

FAQs
----
  - **Can I use SimpleSectionAdapter with ArrayAdapter or CursorAdapter?** 
    <br />Yes, you can use [SimpleSectionAdapter][simple-section-adapter] with any adapter that extends [BaseAdapter][base-adapter].

  - **Should I sort my data in my data source source (Cursor, ArrayList or Array) logically in the order they have to be sectioned?** 
    <br />Yes, you have to sort your data, [SimpleSectionAdapter][simple-section-adapter] does not perform sorting.

  - **Are there any resource files that has to be included with my project?** 
    <br />No, you don't have to include anything besides the [SimpleSectionAdapter][simple-section-adapter] and the [Sectionizer][sectionizer].

  [list-view-link]: http://developer.android.com/reference/android/widget/ListView.html
  [google-search]: https://www.google.co.in/search?ie=UTF-8&q=android+section+adapter
  [github-project]: https://github.com/ragunathjawahar/simple-section-adapter
  [sources-download-link]: https://github.com/ragunathjawahar/simple-section-adapter/zipball/master
  [jar-download]: https://github.com/ragunathjawahar/simple-section-adapter/downloads
  [simple-section-adapter]: https://github.com/ragunathjawahar/simple-section-adapter/blob/master/src/com/mobsandgeeks/adapters/SimpleSectionAdapter.java
  [sectionizer]: https://github.com/ragunathjawahar/simple-section-adapter/blob/master/src/com/mobsandgeeks/adapters/Sectionizer.java
  [base-adapter]: http://developer.android.com/reference/android/widget/BaseAdapter.html
  [item-click-listener]: http://developer.android.com/reference/android/widget/AdapterView.OnItemClickListener.html
  [simple-example-link]: https://github.com/ragunathjawahar/simple-section-adapter/blob/master/src/com/mobsandgeeks/demo/ArrayAdapterDemoActivity.java
