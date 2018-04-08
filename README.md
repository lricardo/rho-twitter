# RhoTwitter

## About

This application is a job technical challenge. 

## Checklist

* Get Twitter public feed using its streaming API (prefer data push over polling) ✅
* Search for a word provided by the user. That should produce a flow of data big enough for our purposes.   
* Once collected, the tweets will be included in a view. ✅ (needs optimizations)
* Every tweet has a lifespan, meaning that after that time, they will have to be removed from the list.  
* Make this lifespan easy to tune in code.  
* If the connection is interrupted or the app is run offline, the tweets that  were alive during the previous execution will be shown until a connection  is established.  
Use stock framework UI elements.  

## Technical challenges

### Get Twitter public feed using its streaming API (prefer data push over polling)

There are multiple ways of doing that:

* Easy way: using twitter4j
* Medium way: using Retrofit + signpost (with some tweaks preventing the socket from timing out)
* Very Hard way: using HttpURLConnection and implement the OAuth1a authentication workflow

My choice: using Twitter4j, simply because I wanted to focus on the application development and not in authentication workflows.


