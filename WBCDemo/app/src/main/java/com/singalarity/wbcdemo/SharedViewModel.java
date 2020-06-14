package com.singalarity.wbcdemo;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Cryption> WBC = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginStatus = new MutableLiveData<>();
    private MutableLiveData<String> userName = new MutableLiveData<>();

    public LiveData<String> getUserName() {
        return this.userName;
    }

    public void setUserName(String name) {
        this.userName.setValue(name);
    }

    public LiveData<Boolean> getLoginStatus() {
        return this.loginStatus;
    }

    public void setLoginStatus(Boolean status) {
        this.loginStatus.setValue(status);
    }

    public LiveData<Cryption> getWBC() {
        return this.WBC;
    }

    public void setWBC(Cryption wbc) {
        this.WBC.setValue(wbc);
    }
}