package com.example.demo.Config;

public interface URLPattern {

    // permitAll
    String[] permitAllUrls = new String[]{
            "/",
            "/docs/*",
            "/oauth2/userinfo", "/oauth2/token", "/oauth2/authorization/*",
            "/login", "/login/oauth2/test/*", "/login/test/*",
            "/error",
            "/redis"
    };
    String[] permitAllGetMethod = new String[]{

    };
    String[] permitAllPostMethod = new String[]{

    };

    String[] permitAllPutMethod = new String[]{
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
