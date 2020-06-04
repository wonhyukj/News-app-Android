# News App with React and Nodejs
Project repository of News Android App

# Content
- Android Studio for Android app development.
- Guardian, Google Trends and Open Weather APIs and Android SDK.
- Third party libraries like Picasso, Glide and Volley

#### [Demo] (Click Image for the demonstration)
#### [![Demo](https://github.com/wonhyukjang/-News-app-Android/blob/master/Demo.png)](https://www.youtube.com/watch?v=IowBaIi6fjE&t=5s)

# High Level Description

# Home Tab / Weather Summary Card
The weather summary card of the current location of the user is displayed and below the weather card. </br>
There is a list of 10 latest news as shown in Figure 3.</br>
The current location is to be fetched from the emulator itself, which will be used to call the Openweathermap API to get the weather data.</br>
The list of news is to be fetched from the Guardians API.</br>
On long click on a news card, a dialog will pop up with the image, title and the option to Bookmark and share it on Twitter</br>

# Headline Tab
In this tab there are multiple fragments of different categories:
- World: This fragment shows the latest news related to the World News.
- Business: This fragment shows the latest news related to the Business News.
- Politics: This fragment shows the latest news related to the Political News.
- Sports: This fragment shows the latest news related to the Sport News.
- Technology: This fragment shows the latest news related to the Technology News.
- Science: This fragment shows the latest news related to the Science News.

# Trending Tab
When the user enters a new search term and presses the actionSend button or the enter key, the graph should render with the new search term. Notes:
- This tab uses MPAndroidChart 3rd party library
- This chart contains one LineChart
- Call the Google Trends API from Node.js backend
- The default search term to render the graph is “Coronavirus”

# Bookmark Tab
- Give users an ability to add a news article to Bookmark. The bookmarked news must persist even after the user has closed the app. 
Every news card is clickable (Not the image) to go to detailed news activity
- The user can add a news article to bookmark from the Home Tab, Headlines Tab and also when the user searches from a particular keyword. The news can be also bookmarked from the dialog on long press.
- The user can remove a news article from the Home Tab, Headlines Tab, Search Activity and also from the Bookmark tab. The news can be also removed from bookmark using the dialog on long press.

# Detailed Article Tab
On clicking the articles from any of the Home Tab, Headlines Tab, Search Results or the Bookmark tab, the detailed view of an article should open up

# Search Functionality
- On top right side, there will be a search button which opens a textbox where the user can enter keyword to search for news. 
- The user is provided with suggestions of keywords using the Bing Autosuggest API.
- When the user taps on a suggestion, it is filled inside the search box and clicking enter/next takes the user to the next page.
- Before you get the data from your backend server, a progress bar should display on the screen.
- On the next page, the user will then be redirected to a new page/activity which will show the recycler view with the list of news related to the keyword.
# Twitter Button
From every twitter button, on clicking the button, the article should be shared by opening a browser with Twitter Intent
