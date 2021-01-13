package com.ka8eem.market24.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ka8eem.market24.models.CategoryModel;
import com.ka8eem.market24.models.CityModel;
import com.ka8eem.market24.models.ColorModel;
import com.ka8eem.market24.models.ModelsOfCarModel;
import com.ka8eem.market24.models.SubCategoryModel;
import com.ka8eem.market24.models.TypeCarModel;
import com.ka8eem.market24.repository.DataClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryViewModel extends ViewModel {

    public MutableLiveData<ArrayList<CategoryModel>> mutableCategoryList = new MutableLiveData<>();
    public MutableLiveData<ArrayList<CityModel>> mutableCityList = new MutableLiveData<>();
    public MutableLiveData<ArrayList<SubCategoryModel>> subCategoryList = new MutableLiveData<>();
    public MutableLiveData<ArrayList<TypeCarModel>> typecarList = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ModelsOfCarModel>> modelTypeList = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ColorModel>> colorList = new MutableLiveData<>();
    public MutableLiveData<ArrayList<CityModel>> subAreaList = new MutableLiveData<>();
    MutableLiveData<String> error = new MutableLiveData<>();


    public void getAllCategories() {

        DataClient.getINSTANCE().getAllCategories().enqueue(new Callback<ArrayList<CategoryModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryModel>> call, Response<ArrayList<CategoryModel>> response) {
                mutableCategoryList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<CategoryModel>> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }

    public void getAllCities() {
        DataClient.getINSTANCE().getAllCities().enqueue(new Callback<ArrayList<CityModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CityModel>> call, Response<ArrayList<CityModel>> response) {
                mutableCityList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<CityModel>> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }

    public void getSubCategory(String id) {
        DataClient.getINSTANCE().getSubCategory(id).enqueue(new Callback<ArrayList<SubCategoryModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SubCategoryModel>> call, Response<ArrayList<SubCategoryModel>> response) {
                subCategoryList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<SubCategoryModel>> call, Throwable t) {

            }
        });
    }

    public void getSubArea(String id) {
        DataClient.getINSTANCE().getSubArea(id).enqueue(new Callback<ArrayList<CityModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CityModel>> call, Response<ArrayList<CityModel>> response) {
                subAreaList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<CityModel>> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }

    /////////////**********************////////////////////
    public void getAllTypeCar() {
        DataClient.getINSTANCE().getTypeCars().enqueue(new Callback<ArrayList<TypeCarModel>>() {
            @Override
            public void onResponse(Call<ArrayList<TypeCarModel>> call, Response<ArrayList<TypeCarModel>> response) {
                typecarList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<TypeCarModel>> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }

    public void getAllColorCar() {
        DataClient.getINSTANCE().getTcolorCars().enqueue(new Callback<ArrayList<ColorModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ColorModel>> call, Response<ArrayList<ColorModel>> response) {
                colorList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<ColorModel>> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }

    public void getModelsCar(String id) {
        DataClient.getINSTANCE().getmodelcar(id).enqueue(new Callback<ArrayList<ModelsOfCarModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelsOfCarModel>> call, Response<ArrayList<ModelsOfCarModel>> response) {
                modelTypeList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<ModelsOfCarModel>> call, Throwable t) {

            }
        });
    }
}
