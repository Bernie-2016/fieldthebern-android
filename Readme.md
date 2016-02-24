[![Circle CI](https://circleci.com/gh/Bernie-2016/fieldthebern-android/tree/develop.svg?style=svg&circle-token=ca0895f7453c8d07ce49d9b59c05c527ef146bda)](https://circleci.com/gh/Bernie-2016/fieldthebern-android/tree/develop)

#Field the Bern (Android)
####Crowdsourced voter canvassing.
FieldTheBern helps you go door to door and canvass for Bernie. Knock on doors, tell people about Bernie, find out their level of interest. Next to phonebanking, canvassing is probably the second most crucial and effective way to get votes.The app allows volunteers to enter data about the visit. For example: if no one was home, or the resident strongly supports Bernie, and if they have been contacted before, or want to be contacted again.  A map shows pins for houses that have already been visited so that other volunteers don't overlap each other.  The app includes all the issues information from http://FeelTheBern.org/ (cached for offline) as well as primary and caucus dates and info for all 50 states. Canvassers can also track progress and score points and compete with other volunteers.

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
* [Otto](https://github.com/square/otto)
* [Retrolambda](https://github.com/evant/gradle-retrolambda)

Before contributing, please be sure your are familiar with the design patterns and libraries. 
Feel free to reach out on Slack if you have questions!


##Building

Because all these API keys are based on your debug key, you will need to ask for a 
valid `debug.keystore` from the current app developers.  

You should create a folder `/secure` in the root of your project, (already gitignored). 

This folder is where you will put a file called `debug_keys.properties` which has the API and OAuth keys you will need to build a functioning build of the app.

```
key.google=...
key.oathclient=...
key.oathsecret=...
key.facebookid=...
```
Please contact the current app devs for the `debug.keystore` and `debug_keys.properties` files

[Retrolambda](https://github.com/evant/gradle-retrolambda) is used to give us access to Java 8's lambdas, so you will need to [install the Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html). Then go to Android Studio -> Preferences -> Path Variables and add a new JAVA8_HOME variable with the value set to your jdk8 path.

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
 
* The `screen_example` XML specifies `ExampleView` as the view class to inflate. (this can extend any valid layout like `LinearLayout`)
 
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
    ActionBarService.get(getView()).showToolbar();
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
  

##License

Copyright 2016 Bernie 2016, Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
