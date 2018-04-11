# RhoTwitter

## About

This application is a job technical challenge. 

## Checklist

* Get Twitter public feed using its streaming API (prefer data push over polling) ✅
* Search for a word provided by the user. That should produce a flow of data big enough for our purposes.  ✅
* Once collected, the tweets will be included in a view. ✅ (needs optimizations)
* Every tweet has a lifespan, meaning that after that time, they will have to be removed from the list.   ✅
* Make this lifespan easy to tune in code.  ✅
* If the connection is interrupted or the app is run offline, the tweets that  were alive during the previous execution will be shown until a connection  is established.   ✅
* Use stock framework UI elements.   ✅

## Libraries

### Twitter4j

There are multiple ways of consuming the Twitter Streaming API. Some, are the following:

* Using twitter4j (the easier)
* Using Retrofit + signpost
* Using HttpURLConnection and implement the OAuth1a authentication workflow

My first choice was using Retrofit + signpost, but I had a little difficulty on understanding how to do it and I ended loosing more time than needed on that. As such, I decided to use Twitter4j,  because I wanted to focus on the application development itself (and not on the authentication workflow).

In the end I felt a little like I cheated, as consuming the API is an important part. So, in the branch `retrofit_impl` I'm planning to rebuild the data fetch part of the application with retrofit and signpost.


