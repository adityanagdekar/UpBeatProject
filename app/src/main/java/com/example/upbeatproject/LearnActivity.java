package com.example.upbeatproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static androidx.appcompat.widget.LinearLayoutCompat.VERTICAL;

public class LearnActivity extends AppCompatActivity {
    public static final String MyTag="MyTag";
    LearnActivityAdapter mlearnActivityAdapter =new LearnActivityAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        Log.d(MyTag,"Inside LearnActivity");

        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mlearnActivityAdapter);

        //fetchData();
        fetchDataByRetrofit();

        mlearnActivityAdapter.setLearnActivityListener(new LearnActivityAdapter.LearnActivityClicked() {
            @Override
            public void onLearnActivityClick(LearnActivityItem learnActivityItem) {
//                Toast.makeText(LearnActivity.this, "Clicked ", Toast.LENGTH_SHORT).show();
                String url = learnActivityItem.getUrl();
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(LearnActivity.this, Uri.parse(url));
            }
        });
    }

    private void fetchDataByRetrofit() {
        Log.d(MyTag,"Inside fetchDataByRetrofit()");
        String url="https://newsapi.org/v2/";
        String q="stress self help anxiety happiness";
//                "mental health mindfulness self care";
        String excludeDomains="mashable.com";
        String news_api_key=getString(R.string.news_api_key);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NewsApiInterface newsApiInterface=retrofit.create(NewsApiInterface.class);
        Call<NewsApiResponse> call=newsApiInterface.getNewsItem(q,excludeDomains,news_api_key);
        call.enqueue(new Callback<NewsApiResponse>() {
            @Override
            public void onResponse(Call<NewsApiResponse> call, retrofit2.Response<NewsApiResponse> response) {
                Log.d(MyTag,"Inside onResponse of Retrofit");
                if (response.isSuccessful()){
                    NewsApiResponse newsApiResponse=response.body();
                    List<LearnActivityItem> learnActivityItemList = newsApiResponse.getLearnActivityItemList();
                    mlearnActivityAdapter.setLearnActivityList((ArrayList<LearnActivityItem>) learnActivityItemList);
                    Log.d(MyTag,"Retrofit learnActivityItemList.size(): "+learnActivityItemList.size());
                    Log.d(MyTag,"Retrofit learnActivityItemList.get(0): "+"\n"+
                            "Title: "+learnActivityItemList.get(0).getTitle()+"\n"+
                            "Author: "+learnActivityItemList.get(0).getAuthor()+"\n"+
                            "Url: "+learnActivityItemList.get(0).getUrl()+"\n"+
                            "ImageUrl: "+learnActivityItemList.get(0).getImageUrl()+"\n");
                }
                else{
                    String error = "Response Code: " + response.code()+ "\n"+
                            "response.message(): "+response.message()+"\n\n";
                    Log.d(MyTag,"Retrofit error: "+error);
                }
            }

            @Override
            public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                Log.d(MyTag,"Inside onFailure of Retrofit");
                String error = "Error: " + t.getMessage() + "\n\n";
                Log.d(MyTag,"Retrofit error: "+error);
            }
        });

    }

    private void fetchData() {
        Log.d(MyTag,"Inside fetchData()");
//        String url="https://newsapi.org/v2/everything?q=mental health mindfulness self care&apiKey="+getString(R.string.api_key);
//        String url="https://v2.jokeapi.dev/joke/Miscellaneous";
        String url="https://content.guardianapis.com/search?q=mindfulness emotional balance anxiety love ease sleep routines&api-key=84cef0cb-ea55-49d9-a331-c7e210be2d39";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(MyTag,"inside onResponse");
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response.getJSONObject("response")));
//                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
//                            Log.d("MyTag","status: "+response.getString("status"));
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
//                            Log.d("MyTag","jsonArray[0].title: "+
//                                    jsonArray.getJSONObject(0).getString("webTitle"));
                            ArrayList<LearnActivityItem> learnActivityItemArrayList = new ArrayList<>();
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject innerJsonObject = jsonArray.getJSONObject(i);
                                LearnActivityItem learnActivityItem = new LearnActivityItem(
                                        innerJsonObject.getString("webTitle"),
                                        innerJsonObject.getString("sectionName"),
                                        innerJsonObject.getString("webUrl"),
                                        innerJsonObject.getString("apiUrl")
                                );
                                learnActivityItemArrayList.add(learnActivityItem);
                                Log.d(MyTag,"learnActivityItemArrayList.size()): "
                                        +learnActivityItemArrayList.size());
                            }
                            mlearnActivityAdapter.setLearnActivityList(learnActivityItemArrayList);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                            Log.d(MyTag,"jsonException: "+jsonException);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LearnActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        Log.d(MyTag, "gone wrong");
                        Log.d(MyTag, "error.networkResponse.statusCode: " + error.networkResponse.statusCode);
                        Log.d(MyTag,"error.getLocalizedMessage(): "+error.getLocalizedMessage());
                    }
                });
//                {
//
//                    /**
//                    * Passing some request headers
//                    */
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                     HashMap<String, String> headers = new HashMap<String, String>();
//                     //headers.put("Content-Type", "application/json");
//                     headers.put("key", "Value");
//                     return headers;
//                    }
//            }
        Log.d("MyTag","jsonObjectRequest: "+jsonObjectRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(LearnActivity.this);
        requestQueue.add(jsonObjectRequest);
//        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
//                        new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                try {
//                                    JSONArray learnActivityItemJsonArray = response.getJSONArray("articles");
//                                    Log.d("MyTag","learnActivityItemJsonArray.length(): "+learnActivityItemJsonArray.length());
//
//                                    ArrayList<LearnActivityItem> learnActivityItemArrayList = new ArrayList<>();
//
//                                    for (int i=0;i<learnActivityItemJsonArray.length();i++){
//                                        JSONObject jsonObject = learnActivityItemJsonArray.getJSONObject(i);
//                                        LearnActivityItem learnActivityItem = new LearnActivityItem(
//                                                jsonObject.getString("title"),
//                                                jsonObject.getString("author"),
//                                                jsonObject.getString("url"),
//                                                jsonObject.getString("urlToImage")
//                                        );
//                                        learnActivityItemArrayList.add(learnActivityItem);
//                                    }
//                                    mlearnActivityAdapter.setLearnActivityList(learnActivityItemArrayList);
//
//                                } catch (JSONException jsonException) {
//                                    jsonException.printStackTrace();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                // TODO: Handle error
//                                Log.d("MyTag","gone wrong");
//                                Log.d("MyTag",error.getLocalizedMessage());
//                                Toast.makeText(LearnActivity.this, "gone wrong", Toast.LENGTH_SHORT).show();
//                            }
//                        });