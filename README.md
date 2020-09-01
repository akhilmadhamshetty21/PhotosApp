# PhotosApp

In this Application, we are going to make a Photo Gallery, which will use a URL that retrieves a text file containing a dictionary of keywords and image URLs related
to the associated keyword. The application consists of a single activity that enables the user to download and view online photos.  
Loading Images based on Keywords
The interface should be created to match the user interface (UI) presented in Figure 1.
You will be using layout files, and strings.xml to create the user interface. Perform the
following tasks:
1. When the activity starts, the keyword api should be called to get the list of possiblekeywords. The keyword api will be called using an AsyncTask.
2. Clicking on the “Go” Button, it would display a list of keywords:-  
2(a). You can either Alert Dialog or Spinner to implement it.
3. Clicking on a Keyword should do the following:
a) The TextView will hold the search keyword clicked by the user.
b) We use AsyncTask/Thread to connect to the Get URLs API and retrieve the list of image urls related to the selected keyword.
4. When the api data is retrieved:
a) Use another AsyncTask/Thread to retrieve the first image associated with the keyword and display it in the ImageView.
b) The AsyncTask/Thread class should be in a separate file/class not inner to the main thread. i.e. We managed passing parameters to the class and then
pass back the result image downloaded to the UI.
c) While the image is being retrieved, we displayed a Progress Bar.
5. The Next and Previous photo icons should be disabled when the application is launched, and enabled after the first photo is displayed. 
The buttons will also remain disabled in the case there is only 1 image or there are no images corresponding to a keyword. Use icons provided in Support Files for setting the image icons (next.png,prev.png)
6. Do not store the photos, simply download and display the retrieved photos. Also do not attempt to download all the URLs receive, and your app should only download
and display a single photo at any given time.
7. Upon clicking the “Next Photo” icon, you should download the next photo in list of URLs retrieved (following the of order of appearance in the URL list retrieved). 
You should call the AsyncTask/ Thread used to download the next photo.
a) If the currently displayed photo happens to be the last photo, you should download and retrieve the photo at index 0 (the first photo) when the Next icon
is pressed.
8. Clicking the “Previous Photo” icon, you should download the previous photo. If the currently displayed photo happens to be the first photo, you should download and
retrieve the photo at the last index position (last photo).
9. If the api returns an empty list of urls, then clear the currently displayed ImageView and display an error Toast message “No Images Found”.
10. Your application should download the requested photo only if there is an established internet connection. If there is no internet connection you should display a Toast
message indicting that there is no internet connection and do not attempt to send the
HTTP request.
