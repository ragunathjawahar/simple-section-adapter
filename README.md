About
=====

This is the **SIMPLEST** [Section Adapter][google-search] available for Android. Works with any custom adapter that you already have. No project specific dependencies. Just include the [jar][jar-download] or the sources[github-project] to your Android project.

Pros
----

  - The simplest implementation requires just *6 lines* of code.
  - Zero project specific dependencies.

Versions
-------

  - Android 1.6 and up

Implementation
--------------

    // 1. Create a Sectionizer    
    class BookSectionizer implements Sectionizer<Book> {

        @Override
        public String getSectionTitleForItem(Book book) {
            return book.getGenre();
        }
    }

    // 2. Wrap your existing adapter with the SimpleSectionAdapter
    ....
    sectionAdapter = new SimpleSectionAdapter<Book>(this, 
                yourBookAdapter, R.layout.section_header, R.id.title, 
                new BookSectionizer());
    ...

OnItemClickListener
-------------------
While using an [OnItemClickListener][item-click-listener] instead of using the list item's position directly, use is as shown below.

    @Override
    public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
        int actualPosition = sectionAdapter.getUndecoratedItemPosition(position);

        // More code...
    }


FAQs
----
  - **Can I use SimpleSectionAdapter with ArrayAdapter or CursorAdapter?** 
    <br />Yes, you can use [SimpleSectionAdapter][simple-section-adpater] with any adapter that extends [BaseAdapter][base-adapter].

  - **Are there any resource files that has to be included with my project?** 
    <br />No, you don't have to include anything beyond the SimpleSectionAdapter and the Sectionizer.

  [google-search]: https://www.google.co.in/search?ie=UTF-8&q=android+section+adapter
  [github-project]: https://github.com/ragunathjawahar/simple-section-adapter
  [jar-download]: http://jarlink
  [item-click-listener]: http://developer.android.com/reference/android/widget/AdapterView.OnItemClickListener.html
  [simple-section-adpater]: http://linktosimplesectionadapter
  [base-adapter]: http://developer.android.com/reference/android/widget/BaseAdapter.html

