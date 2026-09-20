package com.example.youtube.model;

import java.util.List;

public class VideoResponse {

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public static class Item {

        private String id;
        private Snippet snippet;

        public String getId() {
            return id;
        }

        public Snippet getSnippet() {
            return snippet;
        }
    }

    public static class Snippet {

        private String title;
        private String description;

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }
}