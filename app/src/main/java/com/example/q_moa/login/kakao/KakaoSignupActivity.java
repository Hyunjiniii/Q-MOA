package com.example.q_moa.login.kakao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.q_moa.activity.MainActivity;
import com.example.q_moa.login.LoginActivity;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

public class KakaoSignupActivity extends Activity {

    private static final String TAG="KakaoSignupActivity";
    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {


            @Override
            public void onFailure(ErrorResult errorResult) {
                int ErrorCode = errorResult.getErrorCode();
                int ClientErrorCode = -777;

                if (ErrorCode == ClientErrorCode) {
                    Toast.makeText(getApplicationContext(), "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG" , "오류로 카카오로그인 실패 ");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("TAG" , "오류로 카카오로그인 실패 ");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                String profileUrl = userProfile.getProfileImagePath();
                String userId = String.valueOf(userProfile.getId());
                String userName = userProfile.getNickname();

                Log.d(TAG, "onSuccess: "+"/ profileurl : "+profileUrl+" / userid : "+userId+" / username : "+userName);
            }

            @Override
            public void onNotSignedUp() {
                // 자동가입이 아닐경우 동의창
            }
        });
//            @Override
//            public void onFailure(ErrorResult errorResult) {
//                String message = "failed to get user info. msg=" + errorResult;
//                Logger.d(message);
//
//                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
//                if (result == ErrorCode.CLIENT_ERROR_CODE) {
//                    finish();
//                } else {
//                    redirectLoginActivity();
//                }
//            }
//
//            @Override
//            public void onSessionClosed(ErrorResult errorResult) {
//                redirectLoginActivity();
//            }
//
//            @Override
//            public void onNotSignedUp() {} // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함
//
//            @Override
//            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
//                String kakaoID = String.valueOf(userProfile.getId()); // userProfile에서 ID값을 가져옴
//                String kakaoNickname = userProfile.getNickname();     // Nickname 값을 가져옴
//                String url = String.valueOf(userProfile.getProfileImagePath());
//
//                Logger.d("UserProfile : " + userProfile);
//                Log.d("kakao", "==========================");
//                Log.d("kakao", ""+userProfile);
//                Log.d("kakao", kakaoID);
//                Log.d("kakao", kakaoNickname);
//                Log.d("kakao", "==========================");
//                redirectMainActivity(url, kakaoNickname); // 로그인 성공시 MainActivity로
//            }
//        });

//        List<String> keys = new ArrayList<>();
//        keys.add("properties.nickname");
//        keys.add("properties.profile_image");
//        keys.add("kakao_account.email");
//
//        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
//            @Override
//            public void onFailure(ErrorResult errorResult) {
//                super.onFailure(errorResult);
//                Log.e(TAG, "requestMe onFailure message : " + errorResult.getErrorMessage());
//            }
//
//            @Override
//            public void onFailureForUiThread(ErrorResult errorResult) {
//                super.onFailureForUiThread(errorResult);
//                Log.e(TAG, "requestMe onFailureForUiThread message : " + errorResult.getErrorMessage());
//            }
//
//            @Override
//            public void onSessionClosed(ErrorResult errorResult) {
//                Log.e(TAG, "requestMe onSessionClosed message : " + errorResult.getErrorMessage());
//            }
//
//            @Override
//            public void onSuccess(MeV2Response result) {
//                Log.e(TAG, "requestMe onSuccess message : " + result.getKakaoAccount().getEmail() + " " + result.getId() + " " + result.getNickname());
//            }
//
//        });

    }

    private void redirectMainActivity(String url, String nickname) {
        Intent intent = new Intent(KakaoSignupActivity.this, MainActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("nickname", nickname);
        startActivity(intent);
        finish();
    }
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
