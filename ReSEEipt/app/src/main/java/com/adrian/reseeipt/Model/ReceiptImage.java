package com.adrian.reseeipt.Model;

import android.graphics.Bitmap;

public class ReceiptImage {
    private int imageID;
    private int receiptID;
    private Bitmap imageBitmap;

    // Use this constructor when retrieving objects from database and converting it into objects
    public ReceiptImage(int imageID, int receiptID, Bitmap imageBytes) {
        this.imageID = imageID;
        this.receiptID = receiptID;
        this.imageBitmap = imageBytes;
    }

    // Use this constructor when adding new image objects
    public ReceiptImage(int receiptID, Bitmap imageBytes) {
        this.receiptID = receiptID;
        this.imageBitmap = imageBytes;
    }

    public ReceiptImage(){

    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public int getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(int receiptID) {
        this.receiptID = receiptID;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBytes(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
