package com.ka8eem.market24.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ka8eem.market24.models.CategoryModel;
import com.ka8eem.market24.models.AreaModel;
import com.ka8eem.market24.models.ColorModel;
import com.ka8eem.market24.models.MainModel;
import com.ka8eem.market24.models.ModelsOfCarModel;
import com.ka8eem.market24.models.SubAreaModel;
import com.ka8eem.market24.models.SubCategoryModel;
import com.ka8eem.market24.models.TypeCarModel;
import com.ka8eem.market24.repository.DataClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryViewModel extends ViewModel {

    public MutableLiveData<List<CategoryModel>> mutableCategoryList = new MutableLiveData<>();
    public MutableLiveData<List<AreaModel>> mutableAreaList = new MutableLiveData<>();
    public MutableLiveData<List<SubCategoryModel>> subCategoryList = new MutableLiveData<>();
    public MutableLiveData<ArrayList<TypeCarModel>> typecarList = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ModelsOfCarModel>> modelTypeList = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ColorModel>> colorList = new MutableLiveData<>();
    public MutableLiveData<List<SubAreaModel>> subAreaList = new MutableLiveData<>();
    MutableLiveData<String> error = new MutableLiveData<>();


    public void getAllCategories() {

        DataClient.getINSTANCE().getAllCategories().enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                mutableCategoryList.postValue(response.body().getResult().getCategories());
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }

    public void getAllCities() {
        DataClient.getINSTANCE().getAllAreas().enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                mutableAreaList.postValue(response.body().getResult().getAreas());
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }

    public void getSubCategory(String id) {
        DataClient.getINSTANCE().getSubCategory(id).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if(response.body().getStatus()) {
                    subCategoryList.postValue(response.body().getResult().getSubcategories());
                }else
                    error.postValue(response.body().getMessage() + " Error!");

            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
                error.postValue(t.getMessage() + " Error!");
            }
        });
    }

    public void getSubArea(String id) {
        DataClient.getINSTANCE().getSubArea(id).enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                subAreaList.postValue(response.body().getResult().getSubareas());
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {
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
