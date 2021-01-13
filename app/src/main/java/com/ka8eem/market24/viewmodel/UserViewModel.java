package com.ka8eem.market24.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ka8eem.market24.models.RegisterResponse;
import com.ka8eem.market24.models.UserModel;
import com.ka8eem.market24.repository.DataClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    public MutableLiveData<UserModel> userModel = new MutableLiveData<>();
    public MutableLiveData<RegisterResponse> userModelRegister = new MutableLiveData<>();
    public MutableLiveData<String> userUpdate = new MutableLiveData<>();
    MutableLiveData<String> error = new MutableLiveData<>();

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

    public void updatePass(String email, String password) {
        DataClient.getINSTANCE().update_pass(email, password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                userUpdate.postValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                error.postValue(t.getMessage());
                Log.e("update pass error", t.getMessage());
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

    public void updateProfile(UserModel userModel) {
        DataClient.getINSTANCE().updateProfile(userModel).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                userUpdate.postValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                error.postValue(t.getMessage());
                Log.e("update profile error", t.getMessage());
            }
        });
    }
}