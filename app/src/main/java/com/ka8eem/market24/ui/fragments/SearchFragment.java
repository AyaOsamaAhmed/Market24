//package com.ka8eem.market24.ui.fragments;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProviders;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.RelativeLayout;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import com.ka8eem.market24.R;
//import com.ka8eem.market24.adapters.SearchAdapter;
//import com.ka8eem.market24.models.CategoryModel;
//import com.ka8eem.market24.models.CityModel;
//import com.ka8eem.market24.models.ProductModel;
//import com.ka8eem.market24.models.SubCategoryModel;
//import com.ka8eem.market24.util.Constants;
//import com.ka8eem.market24.viewmodel.CategoryViewModel;
//import com.ka8eem.market24.viewmodel.ProductViewModel;
//
//import java.util.ArrayList;
//
//
//public class SearchFragment extends Fragment {
//
//
//    public SearchFragment() {
//        // Required empty public constructor
//    }
//
//    RelativeLayout container_sub_city, container_sub_cat;
//    ArrayAdapter<String> catAdapter, cityAdapter, subCatAdapter, subAreaAdapter;
//    Spinner categorySpinner, citySpinner, Sub_categorySpinner, Sub_citySpinner;
//    RecyclerView recyclerView;
//    SearchAdapter searchAdapter;
//    ProductViewModel productViewModel;
//    CategoryViewModel categoryVM;
//
//    ArrayList<CategoryModel> catList = new ArrayList<>();
//    ArrayList<CityModel> cityList = new ArrayList<>();
//    ArrayList<ProductModel> productList;
//    ArrayList<SubCategoryModel> sub_catList = new ArrayList<>();
//    ArrayList<CityModel> subAreaList = new ArrayList<>();
//    private int selectedCityID, selectedCatID, selectedSubCatId, selectedSubAreaId;
//    int catIndex, areaIndex, subCatIndex, subAreaIndex;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_search, container, false);
//        container_sub_cat = view.findViewById(R.id.container_subcat);
//        container_sub_city = view.findViewById(R.id.container_subcity);
//        Sub_categorySpinner = view.findViewById(R.id.sub_category_spinner);
//        Sub_citySpinner = view.findViewById(R.id.sub_city_spinner);
//        container_sub_cat.setVisibility(View.GONE);
//        container_sub_city.setVisibility(View.GONE);
//        categorySpinner = view.findViewById(R.id.category_spinner);
//        citySpinner = view.findViewById(R.id.city_spinner);
//        recyclerView = view.findViewById(R.id.search_recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        categoryVM = ViewModelProviders.of(getActivity()).get(CategoryViewModel.class);
//        initSpinners();
//
//        productViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
//        initRecyclerView();
//
//        return view;
//    }
//
//
//    private void initSpinners() {
//        catList = new ArrayList<>();
//        cityList = new ArrayList<>();
//        sub_catList = new ArrayList<>();
//        subAreaList = new ArrayList<>();
//        categoryVM.getAllCategories();
//        categoryVM.mutableCategoryList.observe(getActivity(), new Observer<ArrayList<CategoryModel>>() {
//            @Override
//            public void onChanged(ArrayList<CategoryModel> categoryModels) {
//                if (getActivity() != null && getContext() != null) {
//                    catList = new ArrayList<>();
//                    String curLang = "AR";
//                    curLang = Constants.getLocal(getContext());
//                    ArrayList<String> list = new ArrayList<>();
//                    for (CategoryModel it : categoryModels) {
//                        catList.add(it);
//                        if (curLang.equals("AR"))
//                            list.add(it.getCategoryName());
//                        else
//                            list.add(it.getCatNameEn());
//                    }
//                    if (getContext() != null) {
//                        String all = getContext().getString(R.string.all_categories);
//                        list.add(0, all);
//                        catList.add(0, new CategoryModel(0, all, "0"));
//                        list.remove(list.size() - 1);
//                        catAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_textview, list);
//                        catAdapter.setDropDownViewResource(R.layout.text_drop);
//                        categorySpinner.setAdapter(catAdapter);
//                    }
//                }
//            }
//        });
//
//
//        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (productList != null) {
//                    catIndex = position;
//                    selectedCatID = catList.get(position).getCategoryId();
//                    if (position == 0) {
//                        container_sub_cat.setVisibility(View.GONE);
//                        getProducts("0", "0", "0", "0");
//                    } else {
//                        categoryVM.getSubCategory(selectedCatID + "");
//                        categoryVM.subCategoryList.observe(getActivity(), new Observer<ArrayList<SubCategoryModel>>() {
//                            @Override
//                            public void onChanged(ArrayList<SubCategoryModel> subCategoryModels) {
//                                if (getActivity() != null && getContext() != null) {
//                                    sub_catList = new ArrayList<>(subCategoryModels);
//                                    String curLang = "AR";
//                                    curLang = Constants.getLocal(getContext());
//                                    ArrayList<String> listNames = new ArrayList<>();
//                                    listNames.add(getString(R.string.choose_sub_category));
//                                    for (SubCategoryModel it : subCategoryModels) {
//                                        if (curLang.equals("AR"))
//                                            listNames.add(it.getCubCatName());
//                                        else
//                                            listNames.add(it.getSubCatNameEn());
//                                    }
//
//                                    subCatAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_textview, listNames);
//                                    subCatAdapter.setDropDownViewResource(R.layout.text_drop);
//                                    Sub_categorySpinner.setAdapter(subCatAdapter);
//                                    container_sub_cat.setVisibility(View.VISIBLE);
//                                }
//                            }
//                        });
//
//
//                        getProducts(selectedCatID + "", selectedCityID + " "
//                                , selectedSubCatId + "", selectedSubAreaId + "");
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        categoryVM.getAllCities();
//        categoryVM.mutableCityList.observe(getActivity(), new Observer<ArrayList<CityModel>>() {
//            @Override
//            public void onChanged(ArrayList<CityModel> cityModels) {
//                if (getActivity() != null && getContext() != null) {
//                    cityList = new ArrayList<>();
//                    ArrayList<String> list = new ArrayList<>();
//                    String curLang = "AR";
//                    curLang = Constants.getLocal(getContext());
//                    for (CityModel model : cityModels) {
//                        cityList.add(model);
//                        if (curLang.equals("AR"))
//                            list.add(model.getCityName());
//                        else
//                            list.add(model.getAreaNameEn());
//                    }
//                    if (getContext() != null) {
//                        String all = getContext().getString(R.string.all_cities);
//                        list.add(0, all);
//                        cityList.add(0, new CityModel(0, all));
//                        list.remove(list.size() - 1);
//                        cityAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_textview, list);
//                        cityAdapter.setDropDownViewResource(R.layout.text_drop);
//                        citySpinner.setAdapter(cityAdapter);
//                    }
//                }
//            }
//        });
//
//
//        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (productList != null) {
//                    areaIndex = position;
//                    selectedCityID = cityList.get(position).getCityID();
//                    if (position == 0) {
//                        getProducts("0", "0", "0", "0");
//                        container_sub_city.setVisibility(View.GONE);
//                    } else {
//                        areaIndex = position;
//                        selectedCityID = cityList.get(position).getCityID();
//                        categoryVM.getSubArea(selectedCityID + "");
//                        categoryVM.subAreaList.observe(getActivity(), new Observer<ArrayList<CityModel>>() {
//                                    @Override
//                                    public void onChanged(ArrayList<CityModel> cityModels) {
//                                        if (getActivity() != null && getContext() != null) {
//                                            subAreaList = new ArrayList<>(cityModels);
//                                            String curLang = "AR";
//                                            ArrayList<String> listNames = new ArrayList<>();
//                                            listNames.add(getString(R.string.choose_sub_area));
//                                            curLang = Constants.getLocal(getContext());
//                                            for (CityModel it : cityModels) {
//                                                if (curLang.equals("AR"))
//                                                    listNames.add(it.getCityName());
//                                                else
//                                                    listNames.add(it.getAreaNameEn());
//                                            }
//                                            subAreaAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_textview, listNames);
//                                            subAreaAdapter.setDropDownViewResource(R.layout.text_drop);
//                                            Sub_citySpinner.setAdapter(subAreaAdapter);
//                                            container_sub_city.setVisibility(View.VISIBLE);
//                                        }
//                                    }
//                                }
//                        );
//                        getProducts(selectedCatID + "", selectedCityID + " "
//                                , selectedSubCatId + "", selectedSubAreaId + "");
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        Sub_categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (productList != null) {
//                    //subCatIndex = position;
//                    //selectedSubCatId = sub_catList.get(subCatIndex).getSubCatId();
//                    if (position == 0) {
//                        selectedSubCatId = position;
//                        // getProducts("0", "0", "0", "0");
//                        // container_sub_city.setVisibility(View.GONE);
//                    } else {
//                        subCatIndex = position;
//                        selectedSubCatId = sub_catList.get(subCatIndex).getSubCatId();
//                    }
//                    getProducts(selectedCatID + "", selectedCityID + ""
//                            , selectedSubCatId + "", selectedSubAreaId + "");
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        Sub_citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (productList != null) {
//                    //subCatIndex = position;
//                    //selectedSubCatId = sub_catList.get(subCatIndex).getSubCatId();
//                    if (position == 0) {
//                        selectedSubCatId = position;
//                        // getProducts("0", "0", "0", "0");
//                        // container_sub_city.setVisibility(View.GONE);
//                    } else {
//                        subAreaIndex = position;
//                        selectedSubAreaId = subAreaList.get(subAreaIndex).getCityID();
//                    }
//                    getProducts(selectedCatID + "", selectedCityID + ""
//                            , selectedSubCatId + "", selectedSubAreaId + "");
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }
//
//    private void getProducts(String catID, String cityID, String subCatId, String subAreaId) {
//        productViewModel.getProducts(catID, cityID, subCatId, subAreaId);
//    }
//
//    private void initRecyclerView() {
//        getProducts(selectedCatID + "", selectedCityID + " "
//                , selectedSubCatId + "", selectedSubAreaId + "");
//        productViewModel.mutableProductList.observe(getActivity(), new Observer<ArrayList<ProductModel>>() {
//            @Override
//            public void onChanged(ArrayList<ProductModel> productModels) {
//                if (getActivity() != null) {
//                    if (productModels != null) {
//                        productList = new ArrayList<>(productModels);
//                    } else {
//                        if (getActivity() != null && getContext() != null)
//                            Toast.makeText(getContext(), R.string.no_result, Toast.LENGTH_SHORT).show();
//                    }
//                    searchAdapter = new SearchAdapter();
//                    searchAdapter.setList(productModels);
//                    recyclerView.setAdapter(searchAdapter);
//                }
//            }
//        });
//    }
//}