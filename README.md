# Android Book Scanner
Android app that scans books, stores information from Google Books, then allows users to view cataloged book information.

<img src="https://github.com/Grotke/screenshots/blob/master/bookscannermenu.png" width="300">
<img src="https://github.com/Grotke/screenshots/blob/master/scanneranotherbookedit.png" width="300">
<img src="https://github.com/Grotke/screenshots/blob/master/scannerbookview.png" width="300">
## Description and Background
This book scanner is being developed to help me catalog my books when I move. I want to keep enough information to know what I did with the book.

Currently, the scanner uses the ZXing barcode scanner library and gets book information using the Google Books API. It stores important information (authors, categories, rating, number of reviews, thumbnail url, location, date scanned) in a database and displays this data. The location can be added by the user after the book is scanned. I may add the ability to do bulk scans without interrupting with the location entry each time.

Pressing the "Scan" button on the starting menu launches the barcode reader. The barcode is automatically scanned when in the scanner's view and then a editing screen is shown that allows the user to enter a location for what they did with the book. This could be something like "Box 1, Box 2..." or "Kitchen Shelf, Bedroom Night Stand..." or, in my case, "Toss, Keep, Sell...". Then the scanner is pulled up again. This continues until the user backs out of the scanner. 

In the future, I plan to add the ability to export the data as a text or database file. I also plan to allow sorting and searching on the data. 

Go to http://www.josephcmontgomery.com/projects.html#bookscanner for more information on the development process.

## Building Instructions
If you wanted to download this app for some reason, you'd probably have to import it to Android Studio since that's what I developed it in. You could probably use Eclipse too.

Then you'd just build it like any other Android Studio project and upload it to a device.
## Issues
This isn't completed yet but if you wanted to use this there are a few things to keep in mind.

1. The database automatically deletes itself when the app is opened. This is for testing purposes. You'd have to change this if you wanted the database to persist.

2. The app currently doesn't use an API key to access Google Books. I don't know the ramifications of this since it seems to work regardless, but it's something to keep in mind.

3. The user may need to download the barcode scanner in order to use this app. I don't know for sure if they have to. Packaging the barcode scanner library with the app is planned for the future.
