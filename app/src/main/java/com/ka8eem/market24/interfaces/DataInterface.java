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
import com.ka8eem.market24.models.UploadImageModel;
import com.ka8eem.market24.models.UserModel;

import java.util.ArrayList;

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

    @GET("get_mostReq_ads.php")
    Call<ArrayList<ProductModel>> getMostRequested();

    @GET("get_recently_ads.php")
    Call<ArrayList<ProductModel>> getRecentlyAdded();

    @GET("php/login.php")
    Call<UserModel> login(@Query("acc") String email,
                          @Query("pass") String password);

    @GET("My_ads.php")
    Call<ArrayList<ProductModel>> getMyAds(@Query("ID") int pID);

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
//
    @GET("delete_MyAds.php")
    Call<RegisterResponse> deleteProduct(@Query("ads_id") String id);

    @GET("get_type.php")
    Call<ArrayList<TypeCarModel>> getTypeCar();

    @GET("get_color_car.php")
    Call<ArrayList<ColorModel>> getcolorCar();

    @GET("get_model.php")
    Call<ArrayList<ModelsOfCarModel>> getModelsCar(@Query("id_type") String type_car);

    @POST("market/api/general/subAreas")
    Call<MainModel> getSubAreas(@Query("area_id") String id);

    // ads banner
    @GET("php/get_images_payment.php")
    Call<ArrayList<PannerModel>> getPannerImages();

    @GET("get_product_id.php")
    Call<ProductModel> getProductById(@Query("id") String id);

    @GET("delete_image.php")
    Call<String> deleteImage(@Query("img_id") String id);

    @GET("get_error.php")
    Call<String> get_error(@Query("error_name") String get_error);

    // POST requests
    @POST("php/register.php")
    Call<RegisterResponse> register(@Body UserModel userModel);

    @POST("market/api/ads/add")
    Call<MainModel> uploadProduct(@Body AdsModel adsModel);

    @POST("create_report.php")
    Call<String> reportAds(@Body ReportModel reportModel);

    @POST("create_request.php")
    Call<String> requestProduct(@Body RequestModel requestModel);

    @POST("php/update_profile.php")
    Call<String> updateProfile(@Body UserModel userModel);


   // @POST("upload_images_ads.php")
   // Call<String> uploadImagesAsString(@Body UploadImageModel imageModel);

    @POST("update_image_ads.php")
    Call<String> updateImagesAsString(@Body UploadImageModel imageModel);

    @FormUrlEncoded
    @POST("market/api/auth/forgetPassword")
    Call<ForgetPasswordResponse> forgetPassword (@Field("email") String email);

    @POST("update_product.php")
    Call<RegisterResponse> updateProduct(@Body AdsModel adsModel);

//


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

    // upload one file
//    @Multipart
//    @POST("upload_img.php")
//    Call<ResponseBody> uploadOneFile(
//            @Part("ads_id") RequestBody catId,
//            @Part MultipartBody.Part images
//    );

}