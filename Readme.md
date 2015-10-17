#FeelTheBern Android

This app under development and is currently mostly a mirror of the FTB website. 
The goal is to provide a tool for grassroots organizers to have quick and easy 
access to Bernie's stance on the issues.
 
It uses the JSON feed to render views (rather than a WebView)


##Code architecture

FTB Android **loosely** uses [Model-View-Presenter](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter) and the [Repository](http://code.tutsplus.com/tutorials/the-repository-design-pattern--net-35804) design patterns.  

We also use the following open-source libraries.  


* [Dagger 2](http://google.github.io/dagger/)
* [Mortar](https://github.com/square/mortar) and [Flow](https://github.com/square/flow)
* [Retrofit 2](https://github.com/square/retrofit)
* [Timber](https://github.com/JakeWharton/timber) (for logging)
* [Butterknife](https://github.com/JakeWharton/butterknife)
* [RxJava](https://github.com/ReactiveX/RxJava)

Before contributing, please be sure your are familiar with the design patterns and libraries. 
Feel free to reach out on Slack if you have questions!


##Do's and Dont's

**Don't** use `AsyncTask`...   **Do** use `RxJava`

**Don't** use `Fragments`...   **Do** use `Flow`

##Git

Please follow the [git-flow](http://nvie.com/posts/a-successful-git-branching-model/) branching and branch naming convention


##JSON

The JSON is parsed using `GSON`.  The JSON can be found at: [http://feelthebern.org/ftb-json/](http://feelthebern.org/ftb-json/)

Custom deserializers `CollectionDeserializers` and `PageContentDeserializer` are passed to `Retrofit` to do the work.

####Structure

The JSON structure looks something like this
  
    collection {obj}
     +-items [array]
         +-page
         +-collection
             +-items [array]
                 +-page
                 +-page
         +-page
         +-collection
             +-items [array]
                 +-page
                 +-page {obj}
                     +-content [array]
                     
####'content' array items *roughly* equate to HTML nodes such as  

* `h1`  
* `h2`  
* `h3`  
* `p`  
* `img`  
* `nav`  
* `video`  
