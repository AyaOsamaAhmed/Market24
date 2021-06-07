package com.ka8eem.market24.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ka8eem.market24.models.ForgetPasswordResponse;
import com.ka8eem.market24.models.MainModel;
import com.ka8eem.market24.models.RegisterResponse;
import com.ka8eem.market24.models.TermsModel;
import com.ka8eem.market24.models.UpdateProfileResponse;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.repository.DataClient;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    public MutableLiveData<UserModel> userModel = new MutableLiveData<>();
    public MutableLiveData<RegisterResponse> userModelRegister = new MutableLiveData<>();
    public MutableLiveData<String> userUpdate = new MutableLiveData<>();
    MutableLiveData<String> error = new MutableLiveData<>();
    public MutableLiveData<TermsModel> term = new MutableLiveData<>();
    public MutableLiveData<UserModel> userData = new MutableLiveData<>();

    public void login(String email, String password) {
        DataClient.getINSTANCE().login(email, password).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                userModel.postValue(response.body());
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                error.postValue(t.getMessage());
                Log.e("Login", t.getMessage());
            }
        });
    }

    public void updatePass(String user_id, String password) {
        DataClient.getINSTANCE().update_pass(user_id, password).enqueue(new Callback<ForgetPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgetPasswordResponse> call, Response<ForgetPasswordResponse> response) {
                userUpdate.postValue(response.message());
            }

            @Override
            public void onFailure(Call<ForgetPasswordResponse> call, Throwable t) {
                error.postValue(t.getMessage());
                Log.e("update pass error", t.getMessage());
            }
        });
    }

    public void userData(int user_id) {
        DataClient.getINSTANCE().getUserData(user_id).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                userData.postValue(response.body().getResult().getUser());
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                error.postValue(t.getMessage());
                Log.e("UserData", t.getMessage());
            }
        });
    }

    public void register(UserModel userModel) {
        DataClient.getINSTANCE().register(userModel).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                userModelRegister.postValue(response.body());
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                error.postValue(t.getMessage());
                Log.e("Register", t.getMessage());
            }
        });
    }

    public void updateProfile(UserModel userModel, MultipartBody.Part image) {

        DataClient.getINSTANCE().updateProfile(userModel,image).enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                userUpdate.postValue(response.message());
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                error.postValue(t.getMessage());
                Log.e("update profile error", t.getMessage());
            }
        });
    }

    public void GetTerms() {

            DataClient.getINSTANCE().getTerms().enqueue(new Callback<MainModel>() {
                @Override
                public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                    term.postValue(response.body().getResult().getTermsModel());
                }

                @Override
                public void onFailure(Call<MainModel> call, Throwable t) {
                    error.postValue(t.getMessage());
                    Log.e("terms error", t.getMessage());
                }
            });

    }
}