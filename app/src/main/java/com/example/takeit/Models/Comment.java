
package com.example.takeit.Models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public static final String KEY_CONTENT = "content";
    public static final String KEY_COMMENT_PARENT = "commentParent";
    public static final String KEY_USER_COMMENTING = "userCommenting";
    public static final String KEY_CREATED_AT = "createdAt";


    public String getContent() {
        return getString(KEY_CONTENT);
    }

    public void setContent(String content) {
        put(KEY_CONTENT, content);
    }

    public ParseUser getUserCommenting() {
        return getParseUser(KEY_USER_COMMENTING);
    }

    public void setUser(ParseUser userCommenting) {
        put(KEY_USER_COMMENTING, userCommenting);
    }

    public ParseUser getCommentParent() {
        return getParseUser(KEY_COMMENT_PARENT);
    }

    public void setCommentParent(ParseUser userParent) {
        put(KEY_COMMENT_PARENT, userParent);
    }

    public Date getDate() {
        return getDate(KEY_CREATED_AT);
    }

    public void setDate(Date date) {
        put(KEY_CREATED_AT, date);
    }


}
