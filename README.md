Original App Design Project - README Template
===

# Take It

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
This application allows users to post and sale items that they no longer need or use.

### App Evaluation

- **Category:** Shop/Sell
- **Mobile:** This application will primarily be developed for mobile devices but can also be used on a computer.
- **Story:** Users can upload items that they want to sell. If a user finds an item that they would like to buy, they have the option to contact the seller for further inquiries.
- **Market:** Anyone is allowed to use this mobile application. To keep transactions safe, buyer and seller can only meet in public places to exchange goods. Also, to avoid fraudulent actives funds will not be released to sellers until the buyer has successfully received their product.
- **Habit:** This app can be used pretty often for individuals who like to buy and resale items. Also, it will be used pretty often by individuals who like to buy used/new items at a cheaper price.
- **Scope:** First a user will be able to create a profile with their name, location, etc. Then, any user with a profile can search, post, buy, or sell items. When a user posts an item to sell, they must post clear pictures (from all angles) and create a base price. When a user finds an item that they want to buy, they have the option to contact the seller and negotiate a price. All money transactions will be done through our application. Once the item has been successfully picked up by the buyer funds will be released to the seller.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

- [X]	User can create a new account
- [X]	User can login and log out
- [X] User can view main home screen and navigate to profile and photo fragments
- [X] User can post a new photo to the home feed that includes a description of the product
- [X]	User can view a feed of photos from the home feed
- [ ]	User can view location of the item posted
- [ ]	User can comment on a post

**Optional Nice-to-have Stories**

* User can search by category 
*	User can send an offer using a messaging system
*	User can visit another person's profile to view all their items for sale
*	User can view own profile
*	Restyle the application

### 2. Screen Archetypes

* Login Screen
   * User can login

* Registration Screen
   * User can create a new account

* Stream
   * User can view a feed of photos
   * User can comment on a post

* Creation
   * User can post a new photo to their feed

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home Feed
* Post a Photo
* Profile

**Flow Navigation** (Screen to Screen)

* Login Screen -> If no account, sign up.
* Successful Registration/Login -> Home Feed
* Post a Photo -> Camera opens, photo taken, post created -> Home Feed with new post.
* Profile -> Logout

## Wireframes
![IMG_5329](https://i.imgur.com/08zEyWB.jpg)

## Gif Walkthrough
<img src="https://i.imgur.com/JJPiJ7w.gif" width="250px"> <img src="https://i.imgur.com/p3eivAu.gif" width="250px">

## Schema 

### Models

Post
| Property | Type | Description |
| --- | --- | ---|
| Image | File | Image of item that is being sold |
| Description/caption | String | Description of item being sold |
| Seller | String | Author of post |
| Status | String | Describes if item is still for sale or sold already |
| CommentCount | Integer | Total number of comments on post |
| LikeCount | Integer | Total number of likes on post (optional) |


### Networking

* Home Screen
  * (Read/GET) List of All Available Products
     ```java
        ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);
        query.include(Listing.KEY_USER);
        query.setLimit(20); // Potentially get rid of this.
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Listing> listings, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting listings.");
                    return;
                }
                for (Listing listing : listings) {
                    Log.i(TAG, "Listing: " + listing.getDescription() + ", Username: " + listing.getUser().getUsername());
                }
            }
        });
     ```
* Post + Comment Screen
  * (Read/GET) Product for Sale
       ```java
       // Attach a click listener to row of products so that when clicked, it can move into the comment section, similar to Flixster.
       // This will be a new activity. It should display only the single listing, as well as the comments belonging to said listing.
       
       // Wrap the specific listing that was clicked and pass it to that activity.
     ```
  * (Read/GET) List of Comments
     ```java
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.whereEqualTo("objectId", ObjectID of post above.);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting comments.");
                    return;
                }
                for (Comment comment : comments) {
                    Log.i(TAG, "Comment: " + comment.content() + ", Username: " + comment.getUser().getUsername());
                }
            }
        });
     ```

* Login/Register Screen
  * Log in existing user
      ```java
      ParseUser.logInInBackground(username, password, new LogInCallback() {
          @Override
          public void done(ParseUser parseUser, ParseException e) {
              if (e != null) {
                  Log.e(TAG, "Issue with login", e);
                  Toast.makeText(LoginActivity.this, 
                      "Invalid Username/Password. Please try again.", Toast.LENGTH_SHORT).show();
                  return;
              }
          }
      });
      ```
  * Register a new user
      ```java
      ParseUser user = new ParseUser();
      user.setUsername(usernameView.getText().toString());
      user.setPassword(passwordView.getText().toString());
      user.setEmail(email.getText().toString());
                
      user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
              if (e == null) {
                  Toast.makeText(RegisterActivity.this, 
                                    "Welcome, " + user.getUsername() + "!", Toast.LENGTH_SHORT).show();
              }
          }
      });
      ```

* Create Listing Screen
  * (Create/POST) Create new listing
     ```java
      Listing listing = new Listing();
      listing.setACL(listingACL);
      // All listing information, like the owner of the post, etc.
      listing.saveInBackground();
     ```

* Profile Screen
  * (Read/GET) View all listings by user
     ```java
      ParseQuery<Listing> allListings = ParseQuery.getQuery("Listing");
        allListings.whereContains("username", ParseUser.getCurrentUser().getObjectId());
        allListings.findInBackground(new FindCallback<Listing>() {
            public void done(List<Listing> listings, ParseException e) {
                if (e == null) {
                    // Store listings in a List to display.
                    Log.i(TAG, "List number of listings.");
                } 
                else { 
                    Log.e(TAG, "Error message.");
                }
            }
        }); 
     ```
  * (Delete) Delete listing
    ```java
        ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);
        query.whereEqualTo("objectId", ObjectID of listing to be deleted.);
        query.findInBackground(new FindCallback<Listing>() {
            @Override
            public void done(List<Listing> listings, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting listings.");
                    return;
                }
                for (Listing listing : listings) {
                    listing.deleteInBackground();
                    Log.i(TAG, "Listing " + listing.getObjectId() + " has been deleted.");
                }
            }
        });
      ```
