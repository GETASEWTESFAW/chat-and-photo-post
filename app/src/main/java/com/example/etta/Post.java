package com.example.etta;

public class Post {
    public String post;
    public String url;
    public Post(String post, String url){
        this.post=post;
        this.url=url;
    }
    public Post(){

    }
    public void setPost(String post){
        this.post=post;
    }
    public void setUrl(String url){
        this.url=url;
    }

    public String getPost() {
        return this.post;
    }

    public String getUrl() {
        return this.url;
    }
}
