package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SubMainModel implements Serializable {

    @SerializedName("categories")
    private List<CategoryModel> categories;

    @SerializedName("subcategories")
    private List<SubCategoryModel> subcategories;

    @SerializedName("areas")
    private List<AreaModel> areas;

    @SerializedName("subareas")
    private List<SubAreaModel> subareas;

    @SerializedName("payment_images")
    private List<PannerModel> paymentImages;

    @SerializedName("payment_ads")
    private List<PaymentAdsModel> paymentAds;

    @SerializedName("normal_ads")
    private List<ProductModel> normalAds;

    @SerializedName("ad")
    private ProductModel ads;

    @SerializedName("user")
    private UserModel user;

    @SerializedName("ads")
    private MyProductModel my_ads;

    @SerializedName("search")
    private MySearchProductModel product_search;

    @SerializedName("favourites")
    private MyFavouriteModel my_ads_favourite;

    @SerializedName("conversations")
    private MyConversationsModel my_conversations;

    @SerializedName("messages")
    private GetMessagesModel get_messages;

    @SerializedName("message")
    private MessageModel send_message;

    @SerializedName("terms")
    private TermsModel termsModel;

    public SubMainModel() { }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public List<SubCategoryModel> getSubcategories() {
        return subcategories;
    }

    public List<AreaModel> getAreas() {
        return areas;
    }

    public List<SubAreaModel> getSubareas() {
        return subareas;
    }

    public List<PannerModel> getPaymentImages() {
        return paymentImages;
    }

    public List<PaymentAdsModel> getPaymentAds() {
        return paymentAds;
    }

    public List<ProductModel> getNormalAds() {
        return normalAds;
    }

    public ProductModel getAds() {
        return ads;
    }

    public TermsModel getTermsModel() {
        return termsModel;
    }

    public MyProductModel getMy_ads() {
        return my_ads;
    }

    public MyFavouriteModel getMy_ads_favourite() {
        return my_ads_favourite;
    }

    public MySearchProductModel getProduct_search() { return product_search; }

    public MyConversationsModel getMy_conversations() { return my_conversations; }

    public GetMessagesModel getGet_messages() { return get_messages; }

    public MessageModel getSend_message() { return send_message; }

    public UserModel getUser() { return user; }
}
