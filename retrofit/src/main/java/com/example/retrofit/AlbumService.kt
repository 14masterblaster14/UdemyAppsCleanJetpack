package com.example.retrofit

import retrofit2.Response
import retrofit2.http.*

/**
 *  GET :   to retreive data from a server at the specified resource,
 *          its a default method in HTTP clients and returns a 200 status code.
 *          GET requests can be cached
GET requests remain in the browser history
GET requests can be bookmarked
GET requests should never be used when dealing with sensitive data
GET requests have length restrictions
GET requests are only used to request data (not modify)
 *
 *  POST :  to send data to the API server to create or update a resource and 200 status code is returned.
 *          It is non-idempotent. It mutates data on the backend server (by creating or updating a resource),
 *          as opposed to a GET request which does not change any data.
 *          POST requests are never cached
POST requests do not remain in the browser history
POST requests cannot be bookmarked
POST requests have no restrictions on data length
 *
 *  PUT :   to send data to the API to update or create a resource. The difference is that PUT requests are idempotent.
 *          That is, calling the same PUT request multiple times will always produce the same result.
 *          In contrast, calling a POST request repeatedly make have side effects of creating the same resource multiple times.
 *          It will respond with a 201 (Created), and if the request modifies existing resource the server will return a 200 (OK) or 204 (No Content).
 *
 *  PATCH : it is similar to POST and PUT. The difference with PATCH is that you only apply partial modifications to the resource.
 *          The difference between PATCH and PUT, is that a PATCH request is non-idempotent (like a POST request).
 *          A successful PATCH request should return a 2xx status code.
 *          PATCH requests should fail if invalid data is supplied in the request -- nothing should be updated.
 *
 *  DELETE : to delete the resource at the specified URL
 *            sending a DELETE request to an unknown resource should return a non-200 status code.
 *
 *  HEAD:   The HEAD method is almost identical to GET, except without the response body.
 *          In other words, if GET /users returns a list of users, then HEAD /users will make the same request
 *          but won't get back the list of users.
 *          HEAD requests are useful for checking what a GET request will return before actually making a GET request
 *          -- like before downloading a large file or response body.
 *
 *  OPTIONS: It should return data describing what other methods and operations the server supports at the given URL.
 */

interface AlbumService {

    //"https://jsonplaceholder.typicode.com/albums"
    @GET("/albums")
    suspend fun getAlbums(): Response<Albums>

    //"https://jsonplaceholder.typicode.com/albums?userId=3"
    @GET("/albums")
    suspend fun getSortedAlbums(@Query("userId") userId: Int): Response<Albums>

    //"https://jsonplaceholder.typicode.com/albums/3"
    @GET("/albums/{id}")
    suspend fun getAlbum(@Path(value = "id") albumId: Int): Response<AlbumsItem>

    @POST("/albums")
    suspend fun uploadAlbum(@Body album: AlbumsItem): Response<AlbumsItem>


}


/*

Action                  	    GET	                                                                            POST

BACK button/Reload	            Harmless	                                                                    Data will be re-submitted (the browser should alert the user that the data are about to be re-submitted)
Bookmarked	                    Can be bookmarked	                                                            Cannot be bookmarked
Cached	                        Can be cached	                                                                Not cached
Encoding type	                application/x-www-form-urlencoded	                                            application/x-www-form-urlencoded or multipart/form-data. Use multipart encoding for binary data
History	                        Parameters remain in browser history	                                        Parameters are not saved in browser history
Restrictions on data length	    Yes, when sending data, the GET method adds the data to the URL;                No restrictions
                                and the length of a URL is limited (maximum URL length is 2048 characters)
Restrictions on data type	    Only ASCII characters allowed	                                                No restrictions. Binary data is also allowed
Security	                    GET is less secure compared to POST because data sent is part of the URL        POST is a little safer than GET because the parameters are not stored in browser history or in web server logs
                                Never use GET when sending passwords or other sensitive information!
Visibility	                    Data is visible to everyone in the URL	                                        Data is not displayed in the URL
 */