package com.sarkstechsolution.ghstorepartner.Model;

public class Products {

    private String name, pid, category, availability, mrp, sp, unit, image, description, sid;

    public Products() {
    }

    public Products(String name, String pid, String category, String availability, String mrp, String sp, String unit, String image, String description, String sid) {
        this.name = name;
        this.pid = pid;
        this.category = category;
        this.availability = availability;
        this.mrp = mrp;
        this.sp = sp;
        this.unit = unit;
        this.image = image;
        this.description = description;
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
