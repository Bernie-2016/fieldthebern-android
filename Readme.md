[![Circle CI](https://circleci.com/gh/Bernie-2016/fieldthebern-android/tree/develop.svg?style=svg&circle-token=ca0895f7453c8d07ce49d9b59c05c527ef146bda)](https://circleci.com/gh/Bernie-2016/fieldthebern-android/tree/develop)

#Field the Bern (Android)

This app under development. The goal is to provide a tool for grassroots organizers to canvass with.  

Issue info is provided by [FeelTheBern.org](http://FeelTheBern.org)

[Video of the iOS version](http://cl.ly/113H0T2u350V)  


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

You will need a **YouTube API key** and **Google Maps Android API Key**.  
Follow the instructions here: [Registering your application](https://developers.google.com/youtube/android/player/register)

Both APIs use the same key, just add both to your application in the Google API Console.

You will also need to setup **Facebook App** to get the App ID.  You only need to create an app in "developer" mode.  
Follow the instructions here: [Facebook Login for Android](https://developers.facebook.com/docs/facebook-login/android)

Once you have your key, create a `keys.xml` file under the `app/src/main/res/values` folder.  
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="googleApiKey">YOUR-API-KEY</string>
    <string name="facebook_app_id">1234567890</string>
</resources>
```

##Mortar and Flow

A quick primer on mortar and flow.

These three files show the usage of mortar and flow as blank templates with just the boilerplate stuff in them and some javadocs to explain what is going on.

[views/TemplateView.java](https://github.com/Bernie-2016/fieldthebern-android/blob/develop/app/src/main/java/com/berniesanders/canvass/views/TemplateView.java)

[screens/TemplateScreen.java](https://github.com/Bernie-2016/fieldthebern-android/blob/develop/app/src/main/java/com/berniesanders/canvass/screens/TemplateScreen.java)

[res/layout/screen_template.xml](https://github.com/Bernie-2016/fieldthebern-android/blob/develop/app/src/main/res/layout/screen_template.xml)

How screen navigation and dependency injection works in our app with flow and mortar:

* To change "screens" call flow: `Flow.get(context).set(new TemplateScreen());`  

* Flow will manage the backstack by saving the object passed to `set()`
 
* flow and mortar read the `TemplateScreen` and inflate the view based on the resId in the `@Layout` annotation `R.layout.screen_template`
 
* The `screen_template` XML specifies `TemplateView` as the view class to inflate. (this can extend any valid layout like `LinearLayout`)
 
* When the view is inflated, it calls a mortar "service" that we set up named `DaggerService`
 
* `DaggerService` creates the Dagger component based on what's in the `TemplateScreen.java` code 
 
* Using the dagger component, `DaggerService` injects the `TemplateScreen.Presenter` on our custom view `TemplateView`
 
* The presenter is static and will stay alive on rotation.
 
* However you can only use the presenter to manipulate the view between `onLoad()` and `dropView()`
 
* Some common features of Activities and Fragments are not available in presenters or custom views such a dialog boxes, and control of the action bar.
 
* To get around that we use 'controllers' that are basically the same kind of presenters described above.
 
* The controllers are made available via a service-lookup pattern through mortar.  Any valid view context can request the service to, for example, hide the actionbar


Example using the `ActionBarController` to show the Toolbar from a presenter's `onLoad()` method
```
@Override
protected void onLoad(Bundle savedInstanceState) {
    ActionBarService.getActionbarController(getView()).showToolbar();
}
```


##Do's and Dont's

**Don't** use `AsyncTask`...   **Do** use `RxJava`

**Don't** use `Fragments`...   **Do** use `Flow`

##Git

Please follow the [git-flow](http://nvie.com/posts/a-successful-git-branching-model/) branching and branch naming convention


##Canvass API

This is the primary API for all canvassing and auth requests.

Canvass API docs can be found on the [fieldthebern-api wiki](https://github.com/Bernie-2016/fieldthebern-api/wiki)

##Issue data from FeelTheBern.org

Content to fill out the "issues" section if provided by [feelthebern.org](http://feelthebern.org/) 
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
  
