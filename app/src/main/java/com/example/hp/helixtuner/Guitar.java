package com.example.hp.helixtuner;

public class Guitar {
private  String Title;

private  int Imagebottom;
private  int ImageTop;

    public Guitar(String title, int imagebottom, int imageTop) {
        Title = title;
        Imagebottom = imagebottom;
        ImageTop = imageTop;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getImagebottom() {
        return Imagebottom;
    }

    public void setImagebottom(int imagebottom) {
        Imagebottom = imagebottom;
    }

    public int getImageTop() {
        return ImageTop;
    }

    public void setImageTop(int imageTop) {
        ImageTop = imageTop;
    }
}
