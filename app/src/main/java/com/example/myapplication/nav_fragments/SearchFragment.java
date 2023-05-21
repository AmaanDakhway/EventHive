package com.example.myapplication.nav_fragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.model.Data;
import com.example.myapplication.model.PersonRecyclerAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    AutoCompleteTextView autocomplete;

    ArrayList<String> CourseArray=new ArrayList<String>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("data");
    String url = "https://course-recommend-engine.onrender.com/api/" ;
    Button button;
    TextView textView;

    String CourseName = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();

    private PersonRecyclerAdapter mAdapter;
    public SearchFragment() {
        // Required empty public constructor
    }
    public static class WrapContentLinearLayoutManager extends LinearLayoutManager {

        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        //... constructor
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_search, container, false);


        button = view.findViewById(R.id.button);
        textView = view.findViewById(R.id.textView);

        ref.orderByChild("Course Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterate over each child in the snapshot and retrieve the name field
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String name = childSnapshot.child("Course Name").getValue(String.class);
                    CourseArray.add(childSnapshot.child("Course Name").getValue(String.class));
                    Log.d(TAG, "Course Name: " + name);
                }
                String[] arr = CourseArray.toArray(new String[0]);
                autocomplete = (AutoCompleteTextView)
                        view.findViewById(R.id.autoCompleteTextView1);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (getContext(),android.R.layout.select_dialog_item, arr);

                autocomplete.setThreshold(2);
                autocomplete.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
                Log.e(TAG, "Error getting user names", databaseError.toException());
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseName = autocomplete.getText().toString();



                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String data = jsonObject.getString("result");
                                    Toast.makeText(getContext(), ""+data, Toast.LENGTH_SHORT).show();

                                    String jsonData = "[{\"id\": 1, \"title\": \"The Great Gatsby\"}, {\"id\": 2, \"title\": \"To Kill a Mockingbird\"}, {\"id\": 3, \"title\": \"Pride and Prejudice\"}]";

                                    // Create ObjectMapper instance
                                    ObjectMapper mapper = new ObjectMapper();

                                    // Parse JSON array into a JsonNode object
                                    JsonNode jsonNode = mapper.readTree(data);
                                    ArrayList<String> titleArray=new ArrayList<String>();
                                    // Iterate over each object in the array and extract the title field
                                    for (JsonNode node : jsonNode) {
                                        titleArray.add(node.get("title").asText());
                                        String title = node.get("title").asText();
                                        Log.d("Title",title);
                                    }
                                    StringBuilder builder = new StringBuilder();
                                    for (String item : titleArray) {
                                        builder.append(item).append("\n");
                                    }
                                    ArrayAdapter adapter = new ArrayAdapter<String>(getContext(),
                                            R.layout.activity_listview, R.id.label, titleArray);

                                    ListView listView = (ListView) view.findViewById(R.id.mobile_list);
                                    listView.setAdapter(adapter);

                                    String text = builder.toString();
                                    textView.setText(text);

                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("course", CourseName);

                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(requireContext());
                queue.add(stringRequest);
            }
        });








//        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView searchRecycler = view.findViewById(R.id.searchRecycler);
//        searchRecycler.setLayoutManager(new HomeFragment.WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
//        searchRecycler.setHasFixedSize(true);
//        Query query = mDb.collection("Users").limit(10);
//        FirestoreRecyclerOptions<Data> options=new FirestoreRecyclerOptions.Builder<Data>()
//                .setQuery(query,Data.class)
//                .build();
//        mAdapter = new PersonRecyclerAdapter(options);
//        searchRecycler.setAdapter(mAdapter);
//        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextInputEditText searchBox = view.findViewById(R.id.searchBox);
//        searchBox.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Log.d("SearchFragment", "Searchbox has changed to: " + s.toString());
//                Query query;
//                if (s.toString().isEmpty()) {
//                    query = mDb.collection("Users")
//                            .orderBy("phone", Query.Direction.ASCENDING).limit(10);
//                } else {
//                    query = mDb.collection("Users")
//                            .whereEqualTo("email", s.toString());
//                }
//                FirestoreRecyclerOptions<Data> options = new FirestoreRecyclerOptions.Builder<Data>()
//                        .setQuery(query, Data.class)
//                        .build();
//                mAdapter.updateOptions(options);
//                mAdapter.notifyDataSetChanged();
//            }
//        });
        return view;
    }
}