package com.ka8eem.market24.interfaces;

import com.ka8eem.market24.Notification.MyResponse;
import com.ka8eem.market24.Notification.Sender;
import com.ka8eem.market24.models.AdsModel;
import com.ka8eem.market24.models.AreaModel;
import com.ka8eem.market24.models.ColorModel;
import com.ka8eem.market24.models.ForgetPasswordResponse;
import com.ka8eem.market24.models.MainModel;
import com.ka8eem.market24.models.ModelsOfCarModel;
import com.ka8eem.market24.models.PannerModel;
import com.ka8eem.market24.models.PaymentAdsModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.models.RegisterResponse;
import com.ka8eem.market24.models.ReportModel;
import com.ka8eem.market24.models.RequestModel;
import com.ka8eem.market24.models.SpecialInfoModel;
import com.ka8eem.market24.models.SubCategoryModel;
import com.ka8eem.market24.models.TypeCarModel;
import com.ka8eem.market24.models.UpdateProfileResponse;
import com.ka8eem.market24.models.UploadImageModel;
import com.ka8eem.market24.models.UserModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface DataInterface {

    // GET request
    @GET("php/get_all_ads.php")
    Call<ArrayList<ProductModel>> getProducts(@Query("select_cat") String selectedCategoryID,
                                              @Query("select_city") String selectedCityID,
                                              @Query("select_sub_cat") String selectedSubCatId,
                                              @Query("select_sub_city") String selectedSubAreaId,
                                              @Query("value_text") String searchText);
    @GET("market/api/home/get")
    Call<MainModel> getHome();

    @POST("market/api/general/categories")
    Call<MainModel> getAllCategories();

    @POST("market/api/general/areas")
    Call<MainModel>  getAllAreas();

    @FormUrlEncoded
    @POST("market/api/general/user")
    Call<MainModel>  getUserData(@Field("user_id") int user_id);

    // no use
    @GET("get_mostReq_ads.php")
    Call<ArrayList<ProductModel>> getMostRequested();
   // no use
    @GET("get_recently_ads.php")
    Call<ArrayList<ProductModel>> getRecentlyAdded();

    @GET("php/login.php")
    Call<UserModel> login(@Query("acc") String email,
                          @Query("pass") String password);

    @POST("market/api/ads/get")
    Call<MainModel> getMyAds(@Query("user_id") int user_id);

    // no use
    @GET("get_oneImage.php")
    Call<ArrayList<SpecialInfoModel>> getMyAds_Message(@Query("ID") int pID);

    @GET("get_ads_category.php")
    Call<ArrayList<ProductModel>> getAdsByCategory(@Query("cat_id") String catId);

    @GET("php/update_pass.php")
    Call<String> update_pass(@Query("email") String email,
                             @Query("pass") String password);

    @POST("market/api/general/subCategories")
    Call<MainModel> getSubCategory(@Query("category_id") String catId);

    // delete
    @GET("php/get_payment_ads.php")
    Call<ArrayList<PaymentAdsModel>> getPaymentAds();
 // no use
    @GET("delete_MyAds.php")
    Call<RegisterResponse> deleteProduct(@Query("ads_id") String id);
 // no use
    @GET("get_type.php")
    Call<ArrayList<TypeCarModel>> getTypeCar();
 // no use
    @GET("get_color_car.php")
    Call<ArrayList<ColorModel>> getcolorCar();
 // no use
    @GET("get_model.php")
    Call<ArrayList<ModelsOfCarModel>> getModelsCar(@Query("id_type") String type_car);

    @POST("market/api/search")
    Call<MainModel> getSearchName(@Query("keyword") String keyword  );

    @POST("market/api/search")
    Call<MainModel> getSearchLocation(@Query("keyword") String keyword ,@Query("lat") String latitude , @Query("long") String longitude
            , @Query("rad") String radius );

    @POST("market/api/search")
    Call<MainModel> getSearchCategory(@Query("cat_id") String category_id , @Query("sub_cat") String sub_category );

    @POST("market/api/search")
    Call<MainModel> getSearch(@Query("keyword") String keyword ,@Query("lat") String latitude , @Query("long") String longitude
            , @Query("cat_id") String category_id , @Query("sub_cat") String sub_category , @Query("rad") String radius );


    @POST("market/api/search")
    Call<MainModel> getSearchByPage(@Query("keyword") String keyword ,@Query("lat") String latitude , @Query("long") String longitude
            , @Query("cat_id") String category_id , @Query("sub_cat") String sub_category , @Query("rad") String radius
            ,@Query("page") int page);


    @POST("market/api/general/subAreas")
    Call<MainModel> getSubAreas(@Query("area_id") String id);

    // ads banner
    @GET("php/get_images_payment.php")
    Call<ArrayList<PannerModel>> getPannerImages();

    @POST("market/api/ads/adDetails")
    Call<MainModel> getProductById(@Query("ads_id") String id);

    @POST("market/api/ads/adDetails")
    Call<MainModel> getProductByIdAndUser(@Query("ads_id") String id , @Query("user_id") int user_id);


 // no use
    @GET("delete_image.php")
    Call<String> deleteImage(@Query("img_id") String id);
 // no use
    @GET("get_error.php")
    Call<String> get_error(@Query("error_name") String get_error);


    // POST requests
    @POST("php/register.php")
    Call<RegisterResponse> register(@Body UserModel userModel);


    @POST("market/api/ads/add")
    @Multipart
    Call<MainModel> uploadProduct(@Part("user_id")String user_id , @Part("category_id")String category_id
            , @Part("subcategory_id")String subcategory_id , @Part("name")String name
            , @Part("price")String price , @Part("description")String description
            , @Part("lat")String lat , @Part("lng")String lng
            , @Part("address")String address  ,@Part("able_disscussion") String able_disscussion
            ,@Part("phone") String phone, @Part List<MultipartBody.Part> images
    );


 // no use
    @POST("create_report.php")
    Call<String> reportAds(@Body ReportModel reportModel);
 // no use
    @POST("create_request.php")
    Call<String> requestProduct(@Body RequestModel requestModel);


    @POST("php/update_profile.php")
    Call<String> updateProfile1(@Body UserModel userModel);

 // no use
    @POST("update_image_ads.php")
    Call<String> updateImagesAsString(@Body UploadImageModel imageModel);

    @FormUrlEncoded
    @POST("market/api/favourite/get")
    Call<MainModel> getAllFavourite(@Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("market/api/favourite/add")
    Call<MainModel> updateItemFavourite(@Field("user_id") int user_id , @Field("ads_id") String ads_id);


    @FormUrlEncoded
    @POST("market/api/auth/forgetPassword")
    Call<ForgetPasswordResponse> forgetPassword (@Field("email") String email);

    @FormUrlEncoded
    @POST("market/api/auth/changePassword")
    Call<ForgetPasswordResponse> changePassword (@Field("user_id") String user_id , @Field("password") String password);


    @POST("market/api/auth/updateProfile")
    @Multipart
    Call<UpdateProfileResponse> updateProfile (@Part("user_id") int user_id , @Part("name") String name
            , @Part("address") String address , @Part("phone") String phone ,
                                               @Part MultipartBody.Part image   );


    // no use
    @POST("update_product.php")
    Call<RegisterResponse> updateProduct(@Body AdsModel adsModel);

    // upload more than one file
    @Multipart
    @POST("upload_img.php")
    Call<String> uploadFiles(
            @Part("ads_id") RequestBody catId,
            @Part ArrayList<MultipartBody.Part> images
    );

    ///////////////****firebase*****///////////////////

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAANzplDhM:APA91bHBWYyxXNYz-Zo9o-3K5_uJFf_ezrv8aCBNCJ-uL6WJE0fRY7v3NzjjBSCjfiBOO0WxcyRTTq5vkhtRlJNXA8uHKRmFc3HhU9ixFjn38YAl0gOlANjWkZ4JRr8Ug8e6iZsWGsoE"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

    @GET("market/api/general/terms")
    Call<MainModel> get_terms();

    @FormUrlEncoded
    @POST("market/api/chat/all")
    Call<MainModel> getAllConversations( @Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("market/api/chat/all")
    Call<MainModel> getAllConversationsByPage( @Field("user_id") int user_id ,  @Field("page") int page);

    @FormUrlEncoded
    @POST("market/api/chat/message")
    Call<MainModel> sendMessage ( @Field("buyer_id") int buyer_id , @Field("seller_id") int seller_id , @Field("ads_id") int ads_id
    , @Field("message") String message , @Field("type_sender") String  type_sender );

    @FormUrlEncoded
    @POST("market/api/chat/getMessages")
    Call<MainModel> getMessages ( @Field("conversation_id") int conversation_id );

    @FormUrlEncoded
    @POST("market/api/chat/getMessages")
    Call<MainModel> getMessagesByPage ( @Field("conversation_id") int conversation_id ,@Field("page") String page );


    // upload one file
//    @Multipart
//    @POST("upload_img.php")
//    Call<ResponseBody> uploadOneFile(
//            @Part("ads_id") RequestBody catId,
//            @Part MultipartBody.Part images
//    );

}