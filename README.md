# RhoTwitter

## About

This application is a job technical challenge. 

## Checklist

* Get Twitter public feed using its streaming API (prefer data push over polling) ✅
* Search for a word provided by the user. That should produce a flow of data big enough for our purposes.  ✅
* Once collected, the tweets will be included in a view. ✅
* Every tweet has a lifespan, meaning that after that time, they will have to be removed from the list.   ✅
* Make this lifespan easy to tune in code.  ✅
* If the connection is interrupted or the app is run offline, the tweets that  were alive during the previous execution will be shown until a connection  is established.   ✅
* Use stock framework UI elements.   ✅

## To deploy this application

Make sure you have a file called secrets.xml with your Twitter Application Credentials:

```bash
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="consumer_key">XXXXX</string>
    <string name="consumer_secret"XXXXX></string>
    <string name="access_token">XXXXX</string>
    <string name="access_token_secret">XXXXX</string>
</resources>
```
## Libraries

* **GSON** is a utility for serializing and unserializing JSON objects. It is awesome.
* **Retrofit2** is a high-level REST abstraction on top of HTTP for interacting with APIs. It may use OkHttp as HTTP client backend.
* **signpost** is a utility for OkHttp which is the facilitates the  signing HTTP messages on the Java platform in conformance with the OAuth Core 1.0a standard. 
* **OkHTTP** is an HTTP Client. Retrofit needs to perform HTTP operations.

## In this repository
* Master: Twitter4j Implementation
* retrofit_impl: Retrofit-based implementation

My first choice was using Retrofit + signpost, but I had a little difficulty in understanding how to do it and I ended losing more time than needed on that. As such, I decided to use Twitter4j, because I wanted to focus on the application development itself (and not on the authentication workflow). Also, Twitter4j is very complete, Android compatible, and multi-thread enabled (it uses OkHttp in its implementation).

In the end, I felt a little like I cheated, as consuming the API is an important part. So, in the branch `retrofit_impl` I had rebuilt the data fetch part of the application with retrofit and signpost.
