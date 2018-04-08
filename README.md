# RhoTwitter

## About

This application is a job technical challenge. 

## Technical challenges

### Get Twitter public feed using its streaming API (prefer data push over polling)

There are multiple ways of doing that:

* Easy way: using twitter4j
* Medium way: using Retrofit + signpost (with some tweaks preventing the socket from timing out)
* Very Hard way: using HttpURLConnection and implement the OAuth1a authentication workflow

My choice: using Twitter4j, simply because I wanted to focus on the application development as (1) OAuth is supported by this library and it is not simple to implement, (2) there are a set of API states that need to be covered. This is a proven library and as such, I decided to use it.

