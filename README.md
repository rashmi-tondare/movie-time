# movie-time
An android app to display movies playing now

This app uses themoviedb.org API. So, to get it working follow these steps:<br/>
1. <a href="https://www.google.com/url?q=https://www.themoviedb.org/account/signup&sa=D&ust=1454740262167000&usg=AFQjCNEzEh0v12wCQUklLUNnVC4gErP_2Q">Create an account</a> to genereate an API key. In your request for the API key specify educational/non-commercial use<br/>
2. Once you have the key, add the following lines to your app's build.gradle:<br/>
<code>debug {
            buildConfigField "String", "MOVIE_DB_API_KEY", "\"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx5e\""
        }</code><br/>
3. Android Studio will prompt you to sync your gradle files. Go ahead and sync it and you're good to go!<br/><br/>
This app also uses <a href="https://developer.android.com/topic/libraries/data-binding/index.html">Android's data binding library</a> to avoid the boiler plate of finding and setting views.<br/><br/>
There is also an implementation of firebase chat and FCM notifications when a new chat message is received in the chat room.
