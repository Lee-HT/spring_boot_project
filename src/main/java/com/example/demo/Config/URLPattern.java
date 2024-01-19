package com.example.demo.Config;

public interface URLPattern {

    // permitAll
    String[] loginUrls = new String[]{
            "/",
            "/oauth2/userinfo", "/login",
            "/login/oauth2/test/*", "/oauth2/authorization/*"
    };
    String[] permitAllGetMethod = new String[]{
            "/error",

            // PostController
            "/post",
            "/post/*",
            "/post/title/*", "/post/username/*",
            "/post/*/likes",

            // CommentController
            "/comment/post/*", "/comment/user/*",
            "/comment/*/likes/*"
    };
    String[] permitAllPostMethod = new String[]{

    };

    String[] permitAllPutMethod = new String[]{
            "/post/likes"
    };


    // hasRole User
    String[] userGetMethod = new String[]{
            // PostController
            "/post/*/likes"

            // CommentController
    };
    String[] userPostMethod = new String[]{

    };
    String[] userDeleteMethod = new String[]{

    };

}
