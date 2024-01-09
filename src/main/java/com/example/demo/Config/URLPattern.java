package com.example.demo.Config;

public interface URLPattern {

    // permitAll
    String[] loginUrls = new String[]{
            "/oauth2/userinfo", "/login",
            "/login/oauth2/test/*", "/oauth2/authorization/*"
    };
    String[] permitAllGetMethod = new String[]{
            "/error",

            // PostController
            "/post",
            "/post/*",
            "/post/title/*", "/post/username/*",
            "/post/*/username/*/likes",

            // CommentController
            "/comment/post/*", "/comment/user/*",
            "/comment/*/likes/*"
    };
    String[] permitAllPostMethod = new String[]{
            // PostController

            // CommentController
    };

    // isAuthenticated
    String[] authenticatedGetMethod = new String[]{

    };
    String[] authenticatedPostMethod = new String[]{

    };

}
