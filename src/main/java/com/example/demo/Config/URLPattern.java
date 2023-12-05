package com.example.demo.Config;

public interface URLPattern {

    // permitAll
    String[] loginUrls = new String[]{
            "/login", "/login/oauth2/test/*", "/oauth2/authorization/*"
    };
    String[] permitAllGetMethod = new String[]{
            // PostController
            "/post",
            "/post/*",
            "/post/title/*", "/post/username/*",
            "/post/*/username/*/likes",

            // CommentController
            "/comment/post/*","/comment/user/*",
            "/comment/*/likes/*"
    };
    String[] permitAllPostMethod = new String[]{
            "/post"
    };

    // isAuthenticated
    String[] authenticatedGetMethod = new String[]{

    };
    String[] authenticatedPostMethod = new String[]{

    };

}
