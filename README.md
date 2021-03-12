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
[Evaluation of your app across the following attributes]
- **Category:** Shop/Sell
- **Mobile:** This application will primarily be developed for mobile devices but can also be used on a computer.
- **Story:** Users can upload items that they want to sell. If a user finds an item that they would like to buy, they have the option to contact the seller for further inquiries.
- **Market:** Anyone is allowed to use this mobile application. To keep transactions safe, buyer and seller can only meet in public places to exchange goods. Also, to avoid fraudulent actives funds will not be released to sellers until the buyer has successfully received their product.
- **Habit:** This app can be used pretty often for individuals who like to buy and resale items. Also, it will be used pretty often by individuals who like to buy used/new items at a cheaper price.
- **Scope:** First a user will be able to create a profile with their name, location, etc. Then, any user with a profile can search, post, buy, or sell items. When a user posts an item to sell, they must post clear pictures (from all angles) and create a base price. When a user finds an item that they want to buy, they have the option to contact the seller and negotiate a price. All money transactions will be done through our application. Once the item has been successfully picked up by the buyer funds will be released to the seller.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can post a new photo to their feed that includes a description of the product
*	User can create a new account
*	User can login and log out
*	User can view a feed of photos
*	User can view location of the item posted
*	User can comment on a post

**Optional Nice-to-have Stories**

* User can search by category 
*	User can send an offer using a messaging system
*	User can visit another person's profile to view all their items for sale
*	User can view own profile

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

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]

### Networking

** Add list of network requests by screen **

* Home Screen
  * (Read/GET) List of All Available Products

* Post + Comment Screen
  * (Read/GET) List of Comments
  * (Read/GET) Product Listing

* Login/Register Screen
  * Log in existing user
  * Register a new user

* Create Listing Screen
  * (Create/POST) Create new listing

* Profile Screen
  * (Read/GET) Verify current user 
  * (Read/GET) View all listings by user 
  * (Delete) Delete listing
  * Log user out
