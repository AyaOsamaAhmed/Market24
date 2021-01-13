package com.ka8eem.market24.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ka8eem.market24.R;
import com.ka8eem.market24.models.AdsModel;
import com.ka8eem.market24.models.PannerModel;
import com.ka8eem.market24.models.PaymentAdsModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.models.RegisterResponse;
import com.ka8eem.market24.models.ReportModel;
import com.ka8eem.market24.models.RequestModel;
import com.ka8eem.market24.models.SpecialInfoModel;
import com.ka8eem.market24.models.UploadImageModel;
import com.ka8eem.market24.repository.DataClient;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.transform.sax.SAXResult;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewModel extends ViewModel {
    public MutableLiveData<ArrayList<ProductModel>> mutableProductList = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ProductModel>> mutableAdsList = new MutableLiveData<>();
    public MutableLiveData<String> mutableUploadProduct = new MutableLiveData<>();
    public MutableLiveData<String> mutableReportAds = new MutableLiveData<>();
    public MutableLiveData<String> mutableRequestProduct = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ProductModel>> adsCategoryList = new MutableLiveData<>();
    public MutableLiveData<ArrayList<PaymentAdsModel>> paymentAdsList = new MutableLiveData<>();
    public MutableLiveData<String> mutableDeleteProduct = new MutableLiveData<>();
    public MutableLiveData<String> uploadImagesResponse = new MutableLiveData<>();
    public MutableLiveData<String> uploadImageAsString = new MutableLiveData<>();
    public MutableLiveData<ArrayList<SpecialInfoModel>> MyAds_Message = new MutableLiveData<>();
    public MutableLiveData<ArrayList<PannerModel>> pannerImages = new MutableLiveData<>();
    public MutableLiveData<ProductModel> productById = new MutableLiveData<>();
    public MutableLiveData<String> _deleteImage = new MutableLiveData<>();
    public MutableLiveData<String> _get_error = new MutableLiveData<>();
    public MutableLiveData<String> _updateProduct = new MutableLiveData<>();
    public MutableLiveData<String> updateImageAsString = new MutableLiveData<>();
    MutableLiveData<String> error = new MutableLiveData<>();


    public void getProducts(String selectedCategoryID, String selectCityID,
                            String selectedSubCatId, String selectedSubAreaId,
                            String searchText) {
        DataClient.getINSTANCE().getProducts(
                selectedCategoryID,
                selectCityID,
                selectedSubCatId,
                selectedSubAreaId,
                searchText).enqueue(new Callback<ArrayList<ProductModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductModel>> call, Response<ArrayList<ProductModel>> response) {
                mutableProductList.postValue(response.body());
            }


            @Override
            public void onFailure(Call<ArrayList<ProductModel>> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }

    public void getMyAds(int id) {
        DataClient.getINSTANCE().getMyAds(id).enqueue(new Callback<ArrayList<ProductModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductModel>> call, Response<ArrayList<ProductModel>> response) {
                mutableAdsList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<ProductModel>> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }

    public void getMyAds_Message(int id) {
        DataClient.getINSTANCE().getMyAds_Message(id).enqueue(new Callback<ArrayList<SpecialInfoModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SpecialInfoModel>> call, Response<ArrayList<SpecialInfoModel>> response) {
                MyAds_Message.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<SpecialInfoModel>> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }

    public void uploadProduct(AdsModel adsModel) {
        DataClient.getINSTANCE().uploadProduct(adsModel).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                mutableUploadProduct.postValue(response.body().getRes());
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                mutableUploadProduct.postValue("-1-1");
            }
        });
    }

    public void reportAds(ReportModel reportModel) {
        DataClient.getINSTANCE().reportAds(reportModel).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                error.postValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                error.postValue(t.getMessage() + " Error");
            }
        });
    }

    public void requestProduct(RequestModel requestModel) {
        DataClient.getINSTANCE().requestProduct(requestModel).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mutableRequestProduct.postValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                error.postValue(t.getMessage() + " Error");
            }
        });
    }

    public void getAdsByCategory(String id) {
        DataClient.getINSTANCE().getAdsByCategory(id).enqueue(new Callback<ArrayList<ProductModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductModel>> call, Response<ArrayList<ProductModel>> response) {
                adsCategoryList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<ProductModel>> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }

    public void getPaymentAds() {
        DataClient.getINSTANCE().getPaymentAds().enqueue(new Callback<ArrayList<PaymentAdsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PaymentAdsModel>> call, Response<ArrayList<PaymentAdsModel>> response) {
                paymentAdsList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<PaymentAdsModel>> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }

    public void deleteProduct(String id) {
        DataClient.getINSTANCE().deleteProduct(id).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                mutableDeleteProduct.postValue(response.body().getRes());
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }

    public void uploadImages(RequestBody id, ArrayList<MultipartBody.Part> images) {
        DataClient.getINSTANCE().uploadImages(id, images).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                uploadImagesResponse.postValue(response.body());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                uploadImagesResponse.postValue(t.getMessage() + "karim");
            }
        });
    }

    public void uploadImageAsString(UploadImageModel model) {
        DataClient.getINSTANCE().uploadImagesAsString(model).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                uploadImageAsString.postValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                uploadImageAsString.postValue(t.getMessage() + "-1-2-3");
                Log.e("upload image error", t.getMessage());
            }
        });
    }

    public void updateImageAsString(UploadImageModel model) {
        DataClient.getINSTANCE().updateImagesAsString(model).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                updateImageAsString.postValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                updateImageAsString.postValue(t.getMessage() + "-1-2-3");
                Log.e("upload image error", t.getMessage());
            }
        });
    }

    public void getPannerImages() {
        DataClient.getINSTANCE().getPannerImages().enqueue(new Callback<ArrayList<PannerModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PannerModel>> call, Response<ArrayList<PannerModel>> response) {
                pannerImages.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<PannerModel>> call, Throwable t) {
                Log.e("panner images error", t.getMessage());
            }
        });
    }

    public void getProductById(String id) {
        DataClient.getINSTANCE().getProductById(id).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                productById.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                Log.e("product by id error", t.getMessage());
            }
        });
    }

    public void deleteImage(String id) {
        DataClient.getINSTANCE().deleteImage(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                _deleteImage.postValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                _deleteImage.postValue(t.getMessage() + "error");
            }
        });
    }

    public void get_error(String get_error) {
        DataClient.getINSTANCE().get_error(get_error).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                _get_error.postValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                _get_error.postValue(t.getMessage() + "error");
            }
        });
    }
    public void updateProduct(AdsModel adsModel) {
        DataClient.getINSTANCE().updateProduct(adsModel).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                _updateProduct.postValue(response.body().getRes());
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                _updateProduct.postValue("-1-1");
            }
        });
    }
}