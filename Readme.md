[![Circle CI](https://circleci.com/gh/Bernie-2016/fieldthebern-android/tree/develop.svg?style=svg&circle-token=ca0895f7453c8d07ce49d9b59c05c527ef146bda)](https://circleci.com/gh/Bernie-2016/fieldthebern-android/tree/develop)

#Field the Bern (Android)

This app under development. The goal is to provide a tool for grassroots organizers to canvass with.  

Issue info is provided by [FeelTheBern.org](http://FeelTheBern.org)

[Video of the iOS version under development](http://cl.ly/113H0T2u350V)  
[Video of the Android version under development](https://vid.me/UA6J)  

##Code architecture

This app **loosely** make use of these design patterns:  
 [Model-View-Presenter](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter)  
 [Repository](http://code.tutsplus.com/tutorials/the-repository-design-pattern--net-35804)  
  

We also use the following open-source libraries.  


* [Dagger 2](http://google.github.io/dagger/)
* [Mortar](https://github.com/square/mortar) and [Flow](https://github.com/square/flow)
* [Retrofit 2](https://github.com/square/retrofit)
* [Timber](https://github.com/JakeWharton/timber) (for logging)
* [Butterknife](https://github.com/JakeWharton/butterknife)
* [RxJava](https://github.com/ReactiveX/RxJava)


Before contributing, please be sure your are familiar with the design patterns and libraries. 
Feel free to reach out on Slack if you have questions!


##Building

Because all these API keys are based on your debug key, you will need to ask for a 
valid `debug.keystore` from the current app developers.  

All these keys **must** be generated with the same `debug.keystore`

You will need a **YouTube API key** and **Google Maps Android API Key**. 
Easiest way to get the Google API keys would be to ask a current **fieldthebern-android** developer.
 
You can also set up your own API keys (for YouTube and Google Maps) by following the instructions here: [Registering your application](https://developers.google.com/youtube/android/player/register)
Both APIs use the same key, just add both to your application in the Google API Console.  

In order to have the app work with **Facebook** you will need to ask us for the Facebook App ID.  
You cannot setup your own facebook app because it needs to use the same app as the **fieldthebern-api**

Once you have your key, create a `keys.xml` file under the `app/src/main/res/values` folder.  
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="googleApiKey">YOUR-API-KEY</string>
    <string name="facebook_app_id">1234567890</string>
</resources>
```

##Mortar and Flow

A quick primer on how we're using mortar and flow.

These three files show the usage of mortar and flow as blank templates with just the boilerplate stuff in them and some javadocs to explain what is going on.

[ExampleView.java](app/src/main/java/com/berniesanders/fieldthebern/views/ExampleView.java)

[ExampleScreen.java](app/src/main/java/com/berniesanders/fieldthebern/screens/ExampleScreen.java)

[screen_example.xml](app/src/main/res/layout/screen_example.xml)

How screen navigation and dependency injection works in our app with flow and mortar:

* To change "screens" call flow: `Flow.get(context).set(new ExampleScreen());`  

* Flow will manage the backstack by saving the object passed to `set()`
 
* flow and mortar read the `ExampleScreen` and inflate the view based on the resId in the `@Layout` annotation `R.layout.screen_example`
 
* The `screen_example` XML specifies `TemplateView` as the view class to inflate. (this can extend any valid layout like `LinearLayout`)
 
* When the view is inflated, it calls a mortar "service" that we set up named `DaggerService`
 
* `DaggerService` creates the Dagger component based on what's in the `ExampleScreen.java` code 
 
* Using the dagger component, `DaggerService` injects the `ExampleScreen.Presenter` on our custom view `ExampleView`
 
* The presenter is static and will stay alive on rotation.
 
* However you can only use the presenter to manipulate the view between `onLoad()` and `dropView()`
 
* Some common features of Activities and Fragments are not available in presenters or custom views such a dialog boxes, and control of the action bar.
 
* To get around that we use 'controllers' that are basically the same kind of presenters described above.
 
* The controllers are made available via a service-lookup pattern through mortar.  Any valid view context can request the service to, for example, hide the actionbar


Example code using the `ActionBarController` to show the Toolbar from a presenter's `onLoad()` method
```java
@Override
protected void onLoad(Bundle savedInstanceState) {
    ActionBarService.getActionbarController(getView()).showToolbar();
}
```

More example code showing how our 'controllers' work can be found in these two example files:


[ExampleController.java](app/src/main/java/com/berniesanders/fieldthebern/controllers/ExampleController.java)

[ExampleService.java](app/src/main/java/com/berniesanders/fieldthebern/controllers/ExampleService.java)



##Do's and Dont's

**Don't** use `AsyncTask`...   **Do** use `RxJava`

**Don't** use `Fragments`...   **Do** use `Flow`

##Git

Please follow the [git-flow](http://nvie.com/posts/a-successful-git-branching-model/) branching and branch naming convention


##Canvass API

This is the primary API for all canvassing and auth requests.

Canvass API docs can be found on the [fieldthebern-api wiki](https://github.com/Bernie-2016/fieldthebern-api/wiki)

##Issue data from FeelTheBern.org

Content to fill out the "issues" section is provided by [feelthebern.org](http://feelthebern.org/) 
The JSON for the issues can be found at:
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
  
