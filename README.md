# Media Review Social Networking App Design

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
2. [Schema](#Schema)
3. [Meta U Requirements](#Meta-U-Requirements)

## Overview
### Description
Social networking app centered around movies, music, and book reviews where the main feed consists of people's reviews and opinions of different pieces of media. A bit like Goodreads + Letterboxd + FB.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can login, create a new account, and logout
* User can post reviews
* User can view a feed of other users' reviews
* User can add a review to their 'my-list'

**Optional Nice-to-have Stories**

* User can change their username
* User can view their and other users' profiles that has their reviews
* User can view their list on their profile
* User can comment on reviews

### 2. Screen Archetypes

* Login Screen
   * User can login
   * User can create a new account
* Feed (Detail)
   * User can view a feed of other users' reviews
* Profile Screen
   * User can view their and other users' profiles that has their reviews/opinions
* Post Creation Screen
   * User can post reviews
* Settings Screen
   * User can logout
   * User can change their username

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Main Feed
* Review Creation
* Profile View
* Settings


## Schema 
### Models
#### Review Post

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user review (default field) |
   | user        | Pointer to User| user that posted review |
   | mediaName         | String     | name of media reviewed |
   | mediaType         | String     | type of media reviewed |
   | body          | String   | review body |
   | commentsCount | Number   | number of comments that has been posted on the review |
   | thumbsUpCount | Number   | number of 'thumbs-up's on the review |
   | createdAt     | DateTime | date when review was posted (default field) |
### Networking
#### List of network requests by screen
   - Home Feed Screen
      - (Read/GET) Query reviews on timeline
         ```java
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.include(Review.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Review>() {
            @Override
            public void done(List<Review> reviews, ParseException e) {
                // work with review query
                }
         ```
      - (Create/POST) Create a new 'thumbs-up' on a review
      - (Delete) Delete existing 'thumbs-up'
      - (Create/POST) Create a new comment on a review
      - (Delete) Delete existing comment
   - Create Review Screen
      - (Create/POST) Create a new review object
   - Profile Screen
      - (Read/GET) Query logged in user object
      - (Update/PUT) Update user profile image
   - Settings Screen
      - (Update/PUT) Update username
    
## Meta U Requirements

### Go beyond CodePath, Difficult/ Ambiguous Technical Problems (atleast 2)

* Your app provides multiple opportunities for you to overcome difficult/ambiguous technical problems (more below)
  * Combine information from three different APIs (movies, music, books) into a single data model.
  * Cache API data for offline use.

### SDK & Database Integration

* Your app interacts with a database (e.g. Parse) 
  * Project will use Parse for Users & Reviews.
* Your app integrates with at least one SDK (e.g. Google Maps SDK, Facebook SDK) or API (that you didn’t learn about in CodePath).
  * Project will use three APIs, one for each media type.

### User Authentication

* You can log in/log out of your app as a user
* You can sign up with a new user profile
* Your app has multiple views

### Visuals & Interactions

* Your app uses at least one gesture (e.g. double tap to like, e.g. pinch to scale) 
  * Can implement to double tap to 'thumbs-up' a review.
* Your app incorporates at least one external library to add visual polish
  * Haven't done research on this yet but I should be able to use Material Design.
* Your app uses at least one animation (e.g. fade in/out, e.g. animating a view growing and shrinking)
  * Maybe an animation for a logo on a launch screen.
