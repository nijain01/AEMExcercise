package com.aemexcercise.core.models;

public class Link {

    /**
     * Class attributes.
     */
    private String pageLink;
    private String title;
    private String name;

    /**
     * @return the link
     */
    public String getPageLink() {
        return pageLink;
    }

    /**
     * @param pageLink the link to set
     */
    public void setPageLink(String pageLink) {
        this.pageLink = pageLink;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}