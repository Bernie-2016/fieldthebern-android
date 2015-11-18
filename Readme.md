[![Circle CI](https://circleci.com/gh/Bernie-2016/fieldthebern-android/tree/develop.svg?style=svg&circle-token=ca0895f7453c8d07ce49d9b59c05c527ef146bda)](https://circleci.com/gh/Bernie-2016/fieldthebern-android/tree/develop)

#Canvass for Bernie / Field the Bern (Android)

This app under development. The goal is to provide a tool for grassroots organizers to canvass with.  

Issue info is provided by [FeelTheBern.org](http://FeelTheBern.org)

[Video of the iOS version](http://cl.ly/113H0T2u350V)  


##Code architecture

This app **loosely** uses [Model-View-Presenter](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter) and the [Repository](http://code.tutsplus.com/tutorials/the-repository-design-pattern--net-35804) design patterns.  

We also use the following open-source libraries.  


* [Dagger 2](http://google.github.io/dagger/)
* [Mortar](https://github.com/square/mortar) and [Flow](https://github.com/square/flow)
* [Retrofit 2](https://github.com/square/retrofit)
* [Timber](https://github.com/JakeWharton/timber) (for logging)
* [Butterknife](https://github.com/JakeWharton/butterknife)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [Otto](https://github.com/square/otto)

Before contributing, please be sure your are familiar with the design patterns and libraries. 
Feel free to reach out on Slack if you have questions!


##Building

You will need a **YouTube API key** and **Google Maps Android API Key**.  
Follow the instructions here: [Registering your application](https://developers.google.com/youtube/android/player/register)

Both APIs use the same key, just add both to your application in the Google API Console.

Once you have your key, create a `keys.xml` file under the `app/src/main/res/values` folder.  
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="googleApiKey">YOUR-API-KEY</string>
</resources>
```

##Do's and Dont's

**Don't** use `AsyncTask`...   **Do** use `RxJava`

**Don't** use `Fragments`...   **Do** use `Flow`

##Git

Please follow the [git-flow](http://nvie.com/posts/a-successful-git-branching-model/) branching and branch naming convention

##Issue data from FeelTheBern.org

The JSON for the FeeTheBern issues can be found at:
[http://feelthebern.org/ftb-json/](http://feelthebern.org/ftb-json/)


####Structure

There are two important model types returned by FTB: `Collection` and `Page`  

Custom deserializers `CollectionDeserializers` and `PageContentDeserializer` are passed to `Gson` which is passed to `Retrofit` to do the work.

#####Collections


The endpoint returns a custom model type called a `Collection`  

Collections contain an array `items` which can contain other `Collections` or `Pages`

```
collection
 +-items  
     +-page
     +-collection
         +-items  
             +-page
             +-page
     +-page
     +-collection
         +-items [array] 
             +-page
             +-page
             +-page 
               +-content [array]
```

#####Page Content

'content' array items *roughly* equate to HTML nodes such as  
* `h1`  
* `h2`  
* `h3`  
* `p`  
* `img`  
* `nav`  
* `video`  
  
