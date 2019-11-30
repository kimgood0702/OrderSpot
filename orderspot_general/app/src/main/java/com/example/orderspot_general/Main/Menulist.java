package com.example.orderspot_general.Main;

public class Menulist {

    private String menuImage;   // Image
    private String menuName;  // name
    private String menuPrice;   // price
    private String productid; // 상품 id
    private String MerchantUser_muser_ID; // 매장 ID
    private boolean selected; // 체크박스

    public Menulist(String menuImage, String menuName, String menuPrice, String productid ,String MerchantUser_muser_ID, boolean selected) {
        this.menuImage = menuImage;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.productid = productid;
        this.MerchantUser_muser_ID = MerchantUser_muser_ID;
        this.selected = selected;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        this.menuPrice = menuPrice;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getMerchantUser_muser_ID() {
        return MerchantUser_muser_ID;
    }

    public void setMerchantUser_muser_ID(String merchantUser_muser_ID) {
        MerchantUser_muser_ID = merchantUser_muser_ID;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
