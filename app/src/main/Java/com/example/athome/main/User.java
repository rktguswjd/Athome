package com.example.athome.main;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.athome.retrofit.ApiService;
import com.example.athome.retrofit.AuthReservation;
import com.example.athome.retrofit.AuthResult;
import com.example.athome.retrofit.AuthSharedLocation;
import com.example.athome.retrofit.EditResult;
import com.example.athome.retrofit.LoginResult;
import com.example.athome.retrofit.RegisterResult;
import com.example.athome.retrofit.RestRequestHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class User implements Parcelable {

    private String userId;
    private String userPassword;
    private String userName;
    private String userEmail;
    private String userPhone;
    private ArrayList<String> userCarNumber;
    private Integer userPoint;
    private AuthSharedLocation authSharedLocation;
    private Integer userState;
    private String token;
    private Integer todaySharingState;
    private LoginResult loginResult;
    String authMessage;
    String authRes;
    String registerMessage;
    String registerRes;
    String loginRes;
    String editPasswordRes;
    String editPasswordMessage;

    public User() {

    };

    //로그인시 사용
    public User(String userId, String userPassword) {
        this.userId = userId;
        this.userPassword = userPassword;
    }

    //회원가입시 사용
    public User(String userId, String userPassword, String userName, String userEmail, String userPhone) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;

    }


    protected User(Parcel in) {
        userId = in.readString();
        userPassword = in.readString();
        userName = in.readString();
        userEmail = in.readString();
        userPhone = in.readString();
        userCarNumber = in.createStringArrayList();
        if (in.readByte() == 0) {
            userPoint = null;
        } else {
            userPoint = in.readInt();
        }
        if (in.readByte() == 0) {
            userState = null;
        } else {
            userState = in.readInt();
        }
        token = in.readString();
        if (in.readByte() == 0) {
            todaySharingState = null;
        } else {
            todaySharingState = in.readInt();
        }
        authMessage = in.readString();
        authRes = in.readString();
        registerMessage = in.readString();
        registerRes = in.readString();
        loginRes = in.readString();
        editPasswordRes = in.readString();
        editPasswordMessage = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEditPasswordRes() {
        return editPasswordRes;
    }

    public String getEditPasswordMessage() {
        return editPasswordMessage;
    }

    public String getToken() {
        return token;
    }

    public String getRegisterMessage() {
        return registerMessage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getUserPoint() {
        return userPoint;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public AuthSharedLocation getAuthSharedLocation() {
        return authSharedLocation;
    }

    public void setAuthSharedLocation(AuthSharedLocation authSharedLocation) {
        this.authSharedLocation = authSharedLocation;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public ArrayList<String> getUserCarNumber() {
        return userCarNumber;
    }

    public void setUserCarNumber(ArrayList<String> userCarNumber) {
        this.userCarNumber = userCarNumber;
    }

    public Integer getUserState() {
        return userState;
    }

    public String getAuthRes() {
        return authRes;
    }

    public Integer getTodaySharingState() {
        return todaySharingState;
    }

    public void setTodaySharingState(Integer todaySharingState) {
        this.todaySharingState = todaySharingState;
    }

    public String register() {

        ApiService serviceApi = new RestRequestHelper().getApiService();

        final Call<RegisterResult> res = serviceApi.signUp(userId, userPassword, userName, userEmail, userPhone); // register (실제 통신이 이루어지는 곳)


        //받아온거 뽑아내기 (동기처리)
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RegisterResult registerResult = res.execute().body();
                    registerRes = registerResult.getRegisterResult();
                    registerMessage = registerResult.getRegisterResultMessage();
                    // test log
                    if (registerRes == null) {
                        Log.d("TEST", "Register 통신 실패....");
                    } else if (registerRes.equals("success")) {
                        Log.d("TEST", "Register 성공!");
                    } else if (registerRes.equals("fail")) {
                        Log.d("TEST", "Register 실패.. ");
                    }

                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registerRes;
    }

    //로그인 인증
    public LoginResult login(String deviceToken) {

        ApiService serviceApi = new RestRequestHelper().getApiService();
        final Call<LoginResult> res = serviceApi.login(userId, userPassword,deviceToken);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loginResult = res.execute().body();
                    token = loginResult.getToken();

                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        }).start();
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginResult;
    }

    //토큰 인증
    public boolean authenticate(String sharedToken) {
        this.token = sharedToken;
        ApiService serviceApi = new RestRequestHelper().getApiService();
        final Call<AuthResult> res = serviceApi.authenticate(sharedToken, "wonseok");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final AuthResult authResult = res.execute().body();
                    //User에 담는다 받은 결과를
                    authRes = authResult.getResult();
                    authMessage = authResult.getAuthMessage();
                    if(authRes.equals("success")) {
                        userPoint = authResult.getAuthUser().getPoint();
                        userId = authResult.getAuthUser().getUserId();
                        userEmail = authResult.getAuthUser().getUserEmail();
                        userPhone = authResult.getAuthUser().getUserPhone();
                        userName = authResult.getAuthUser().getUserName();
                        userCarNumber = (ArrayList<String>) authResult.getAuthUser().getUserCarNumber();
                        userState = authResult.getAuthUser().getState();
                        todaySharingState = authResult.getTodaySharingState();

//                        if(authResult.getAuthUser().getAuthSharedLocation()!=null){
//                        authSharedLocation = authResult.getAuthUser().getAuthSharedLocation();}
//                        authReservation.add(authResult.getAuthUser().getAuthReservation());

                    }
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        }).start();
        try {
            Thread.sleep(1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //검증 결과 리턴
        if (userId != null)
            return true;
        else {
            return false;
        }
    }

    public void editPassword(String token, String currentPassword, final String newPassword) {

        ApiService serviceApi = new RestRequestHelper().getApiService();
        final Call<EditResult> res = serviceApi.editPassword(token,currentPassword,newPassword);
        Log.i("TEST",currentPassword+" "+newPassword);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    final EditResult editResult = res.execute().body();
                    editPasswordRes = editResult.getResult();
                    editPasswordMessage = editResult.getMessage();
                    if (editPasswordRes == null) {
                        Log.d("TEST", "비밀번호 변경 오류");
                    } else if (editPasswordRes.equals("success")) {
                        userPassword = newPassword;
                        Log.d("TEST", "비밀번호 변경 성공");
                    } else if (editPasswordRes.equals("fail")) {
                        Log.d("TEST", "비밀번호 변경 실패.. ");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            Thread.sleep(1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getAuthMessage() {
        return authMessage;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userPassword);
        dest.writeString(userName);
        dest.writeString(userEmail);
        dest.writeString(userPhone);
        dest.writeStringList(userCarNumber);
        if (userPoint == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userPoint);
        }
        if (userState == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userState);
        }
        dest.writeString(token);
        if (todaySharingState == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(todaySharingState);
        }
        dest.writeString(authMessage);
        dest.writeString(authRes);
        dest.writeString(registerMessage);
        dest.writeString(registerRes);
        dest.writeString(loginRes);
        dest.writeString(editPasswordRes);
        dest.writeString(editPasswordMessage);
    }
}