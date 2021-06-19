package com.ka8eem.market24.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ka8eem.market24.models.AdsModel;
import com.ka8eem.market24.models.ConversationModel;
import com.ka8eem.market24.models.FavouriteModel;
import com.ka8eem.market24.models.GetMessagesModel;
import com.ka8eem.market24.models.MainModel;
import com.ka8eem.market24.models.MessageModel;
import com.ka8eem.market24.models.MessagesModel;
import com.ka8eem.market24.models.MyConversationsModel;
import com.ka8eem.market24.models.MySearchProductModel;
import com.ka8eem.market24.models.PannerModel;
import com.ka8eem.market24.models.PaymentAdsModel;
import com.ka8eem.market24.models.ProductModel;
import com.ka8eem.market24.models.RegisterResponse;
import com.ka8eem.market24.models.ReportModel;
import com.ka8eem.market24.models.RequestModel;
import com.ka8eem.market24.models.SendMessageModel;
import com.ka8eem.market24.models.SpecialInfoModel;
import com.ka8eem.market24.repository.DataClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewModel extends ViewModel {
    public MutableLiveData<List<ProductModel>> mutableProductList = new MutableLiveData<>();
    public MutableLiveData<MySearchProductModel> mutableAdsList = new MutableLiveData<>();
    public MutableLiveData<List<ProductModel>> mutableMyAdsList = new MutableLiveData<>();

    public MutableLiveData<MainModel> mutableUploadProduct = new MutableLiveData<>();
    public MutableLiveData<String> mutableReportAds = new MutableLiveData<>();
    public MutableLiveData<String> mutableRequestProduct = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ProductModel>> adsCategoryList = new MutableLiveData<>();
    public MutableLiveData<List<PaymentAdsModel>> paymentAdsList = new MutableLiveData<>();
    public MutableLiveData<List<FavouriteModel>> mutableFavouriteProduct = new MutableLiveData<>();
    public MutableLiveData<MyConversationsModel> mutableConversationModel = new MutableLiveData<>();
    public MutableLiveData<MessageModel> mutableSendMessageModel = new MutableLiveData<>();
    public MutableLiveData<GetMessagesModel> mutableGetMessagesModel = new MutableLiveData<>();
    public MutableLiveData<GetMessagesModel> mutableGetNextMessagesModel = new MutableLiveData<>();

    public MutableLiveData<MainModel> mutableUpdateFavourite = new MutableLiveData<>();

    public MutableLiveData<String> mutableDeleteProduct = new MutableLiveData<>();
    public MutableLiveData<String> uploadImagesResponse = new MutableLiveData<>();
  //  public MutableLiveData<String> uploadImageAsString = new MutableLiveData<>();
    public MutableLiveData<ArrayList<SpecialInfoModel>> MyAds_Message = new MutableLiveData<>();
    public MutableLiveData<List<PannerModel>> pannerImages = new MutableLiveData<>();
    public MutableLiveData<ProductModel> productById = new MutableLiveData<>();
    public MutableLiveData<String> _deleteImage = new MutableLiveData<>();
    public MutableLiveData<String> _get_error = new MutableLiveData<>();
    public MutableLiveData<String> _updateProduct = new MutableLiveData<>();
    public MutableLiveData<String> updateImageAsString = new MutableLiveData<>();
   public MutableLiveData<String> error = new MutableLiveData<>();

    MainModel mainModel = new MainModel();

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


    public void getHome() {
        DataClient.getINSTANCE().getHome().enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus()){
                    paymentAdsList.postValue(response.body().getResult().getPaymentAds());
                    mutableProductList.postValue(response.body().getResult().getNormalAds());
                    pannerImages.postValue(response.body().getResult().getPaymentImages());
                }
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }

    public void getSearchName(String name) {
        DataClient.getINSTANCE().getSearchName(name).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus())
                    mutableAdsList.postValue(response.body().getResult().getProduct_search());
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }

    public void getSearchLocation(String name , String lat , String longtitude , String raduis) {
        DataClient.getINSTANCE().getSearchLocation(name , lat , longtitude , raduis).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus())
                    mutableAdsList.postValue(response.body().getResult().getProduct_search());
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }

    public void getSearchCategory(String cat_id , String sub_cat_id) {
        DataClient.getINSTANCE().getSearchCategory(cat_id , sub_cat_id).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus())
                    mutableAdsList.postValue(response.body().getResult().getProduct_search());
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }

    public void getSearch(String name ,String cat_id , String sub_cat_id, String lat , String longtitude , String raduis) {
        DataClient.getINSTANCE().getSearch(name ,cat_id , sub_cat_id, lat , longtitude , raduis).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus())
                    mutableAdsList.postValue(response.body().getResult().getProduct_search());
                else
                    error.postValue(response.message() + " Error!");
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }

    public void getSearchByPage(String name ,String cat_id , String sub_cat_id, String lat , String longtitude , String raduis , int page) {
        DataClient.getINSTANCE().getSearchByPage(name ,cat_id , sub_cat_id, lat , longtitude , raduis,page).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus())
                    mutableAdsList.postValue(response.body().getResult().getProduct_search());
                else
                    error.postValue(response.message() + " Error!");
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }


    public void getMyAds(int id) {
        DataClient.getINSTANCE().getMyAds(id).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus())
                mutableMyAdsList.postValue(response.body().getResult().getMy_ads().getData());
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
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

    public void uploadProduct(AdsModel adsModel,List<MultipartBody.Part> images) {
        DataClient.getINSTANCE().uploadProduct(adsModel,images).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus())
                mutableUploadProduct.postValue(response.body());
                else
                    error.postValue(response.message() + " Error!");
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                mutableUploadProduct.postValue(mainModel);
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

  /*  public void uploadImageAsString(UploadImageModel model) {
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

   */
    public void updateItemFavourite(int user_id , String ads_id) {
        DataClient.getINSTANCE().updateItemFavourite(user_id,ads_id).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus())
                mutableUpdateFavourite.postValue(response.body());
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                updateImageAsString.postValue(t.getMessage());
                Log.e("upload favourite error", t.getMessage());
            }
        });
    }

    public void getAllFavourite(int user_id) {
        DataClient.getINSTANCE().getAllFavourite(user_id).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus())
                mutableFavouriteProduct.postValue(response.body().getResult().getMy_ads_favourite().getData());
                else
                    error.postValue(response.message() + " Error!");
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
           //     mutableUpdateFavourite.postValue(t.getMessage());
                Log.e("upload favourite error", t.getMessage());
            }
        });
    }


    public void getAllConversation(int user_id) {
        DataClient.getINSTANCE().getAllConversation(user_id).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus())
                    mutableConversationModel.postValue(response.body().getResult().getMy_conversations());
                else
                    error.postValue(response.message() + " Error!");
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                //     mutableUpdateFavourite.postValue(t.getMessage());
                Log.e("upload favourite error", t.getMessage());
            }
        });
    }

    public void getAllConversationByPage(int user_id ,int page) {
        DataClient.getINSTANCE().getAllConversationByPage(user_id ,page).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus())
                    mutableConversationModel.postValue(response.body().getResult().getMy_conversations());
                else
                    error.postValue(response.message() + " Error!");
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                //     mutableUpdateFavourite.postValue(t.getMessage());
                Log.e("upload favourite error", t.getMessage());
            }
        });
    }

    public void sendMessage (int buyer_id , int seller_id , int ads_id
            ,String message ,String  type_sender) {
        DataClient.getINSTANCE().sendMessage(buyer_id,seller_id,ads_id,message,type_sender).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus())
                    mutableSendMessageModel.postValue(response.body().getResult().getSend_message());
                else
                    error.postValue(response.message() + " Error!");
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                //     mutableUpdateFavourite.postValue(t.getMessage());
                Log.e("upload favourite error", t.getMessage());
            }
        });
    }

    public void getMessages (int conversation_id ) {
        DataClient.getINSTANCE().getMessages(conversation_id).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus())
                    mutableGetMessagesModel.postValue(response.body().getResult().getGet_messages());
                else
                    error.postValue(response.message() + " Error!");
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                //     mutableUpdateFavourite.postValue(t.getMessage());
                Log.e("upload favourite error", t.getMessage());
            }
        });
    }

    public void getMessagesByPage (int conversation_id , String page ) {
        DataClient.getINSTANCE().getMessagesByPage(conversation_id , page).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus())
                    mutableGetMessagesModel.postValue(response.body().getResult().getGet_messages());
                else
                    error.postValue(response.message() + " Error!");
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                //     mutableUpdateFavourite.postValue(t.getMessage());
                Log.e("get message next error", t.getMessage());
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
        DataClient.getINSTANCE().getProductById(id).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus())
                productById.postValue(response.body().getResult().getAds());
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                Log.e("product by id error", t.getMessage());
            }
        });
    }

    public void getProductByIdAndUser(String id,int user_id) {
        DataClient.getINSTANCE().getProductByIdAndUser(id,user_id).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus())
                    productById.postValue(response.body().getResult().getAds());
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
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