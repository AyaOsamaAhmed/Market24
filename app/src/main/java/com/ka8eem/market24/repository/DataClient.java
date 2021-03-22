package com.ka8eem.market24.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ka8eem.market24.interfaces.DataInterface;
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
import com.ka8eem.market24.util.Constants;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DataClient {

    private DataInterface dataInterface;
    private static DataClient INSTANCE;

    private DataClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        dataInterface = retrofit.create(DataInterface.class);
    }

    public static synchronized DataClient getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new DataClient();
        return INSTANCE;
    }

    public Call<MainModel> getAllCategories() {
        return dataInterface.getAllCategories();
    }

    public Call<MainModel> getAllAreas() {
        return dataInterface.getAllAreas();
    }

    public Call<MainModel> getHome() {
        return dataInterface.getHome();
    }

    public Call<ArrayList<ProductModel>> getProducts(String catID,
                                                     String cityID,
                                                     String subCatId,
                                                     String subAreaId,
                                                     String searchText) {
        return dataInterface.getProducts(catID, cityID, subCatId, subAreaId, searchText);
    }

    public Call<ArrayList<ProductModel>> getMostRequested() {
        return dataInterface.getMostRequested();
    }

    public Call<ArrayList<ProductModel>> getRecentlyAdded() {
        return dataInterface.getRecentlyAdded();
    }

    public Call<UserModel> login(String email, String password) {
        return dataInterface.login(email, password);
    }

    public Call<RegisterResponse> register(UserModel userModel) {
        return dataInterface.register(userModel);
    }

    public Call<String> update_pass(String email, String password) {
        return dataInterface.update_pass(email, password);
    }

    public Call<ArrayList<ProductModel>> getMyAds(int id) {
        return dataInterface.getMyAds(id);
    }

    public Call<ArrayList<SpecialInfoModel>> getMyAds_Message(int id) {
        return dataInterface.getMyAds_Message(id);
    }


    public Call<MainModel> uploadProduct(AdsModel adsModel) {
        return dataInterface.uploadProduct(adsModel);
    }

    public Call<String> reportAds(ReportModel reportModel) {
        return dataInterface.reportAds(reportModel);
    }

    public Call<String> requestProduct(RequestModel requestModel) {
        return dataInterface.requestProduct(requestModel);
    }

    public Call<String> updateProfile(UserModel userModel) {
        return dataInterface.updateProfile(userModel);
    }

    public Call<ArrayList<ProductModel>> getAdsByCategory(String id) {
        return dataInterface.getAdsByCategory(id);
    }

    public Call<MainModel> getSubCategory(String id) {
        return dataInterface.getSubCategory(id);
    }

    public Call<ArrayList<PaymentAdsModel>> getPaymentAds() {
        return dataInterface.getPaymentAds();
    }

    public Call<RegisterResponse> deleteProduct(String id) {
        return dataInterface.deleteProduct(id);
    }

    public Call<MainModel> getSubArea(String id) {
        return dataInterface.getSubAreas(id);
    }

    public Call<String> uploadImages(RequestBody id, ArrayList<MultipartBody.Part> images) {
        return dataInterface.uploadFiles(id, images);
    }

   /* public Call<String> uploadImagesAsString(UploadImageModel model) {
        return dataInterface.uploadImagesAsString(model);
    }

    */

    public Call<String> updateImagesAsString(UploadImageModel model) {
        return dataInterface.updateImagesAsString(model);
    }

    public Call<ForgetPasswordResponse> forgetPassword (String email) {
        return dataInterface.forgetPassword(email);
    }

    public Call<ArrayList<PannerModel>> getPannerImages() {
        return dataInterface.getPannerImages();
    }

    public Call<ProductModel> getProductById(String id) {
        return dataInterface.getProductById(id);
    }

    public Call<String> deleteImage(String id) {
        return dataInterface.deleteImage(id);
    }

    public Call<String> get_error(String get_error) {
        return dataInterface.get_error(get_error);
    }

    public Call<RegisterResponse> updateProduct(AdsModel adsModel) {
        return dataInterface.updateProduct(adsModel);
    }

    /////////////**********************////////////////////
    public Call<ArrayList<TypeCarModel>> getTypeCars() {
        return dataInterface.getTypeCar();
    }

    public Call<ArrayList<ColorModel>> getTcolorCars() {
        return dataInterface.getcolorCar();
    }

    public Call<ArrayList<ModelsOfCarModel>> getmodelcar(String id) {
        return dataInterface.getModelsCar(id);
    }

}