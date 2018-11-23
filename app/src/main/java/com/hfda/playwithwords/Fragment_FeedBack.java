package com.hfda.playwithwords;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_FeedBack extends Fragment implements View.OnClickListener, RatingBar.OnRatingBarChangeListener
{
    RatingBar mRatingBar;
    TextView tvRatingScale;
    EditText txtFeedBack;
    Button btnSendFeedBack;
    public Fragment_FeedBack() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment__feedback, container, false);
        mRatingBar=v.findViewById(R.id.ratingBar);
        tvRatingScale=v.findViewById(R.id.tvRatingScale);
        txtFeedBack=v.findViewById(R.id.txtFeedback);
        btnSendFeedBack=v.findViewById(R.id.btnSendFeedBack);
        btnSendFeedBack.setOnClickListener(this);
        mRatingBar.setOnRatingBarChangeListener(this);
        return v;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
    {
        tvRatingScale.setText(String.valueOf(rating));
        switch ((int) ratingBar.getRating()) {
            case 1:
                tvRatingScale.setText("Very bad!");
                break;
            case 2:
                tvRatingScale.setText("Need some improvement!");
                break;
            case 3:
                tvRatingScale.setText("Good!");
                break;
            case 4:
                tvRatingScale.setText("Great!");
                break;
            case 5:
                tvRatingScale.setText("Awesome, I really love it!");
                break;
            default:
                tvRatingScale.setText("");
        }
    }
    @Override
    public void onClick(View v)
    {
        if (txtFeedBack.getText().toString().isEmpty())
        {
            Toast.makeText(getContext(), "Please fill in the feedback text box!", Toast.LENGTH_SHORT).show();
        }
        else
        {
           txtFeedBack.setText("");
            mRatingBar.setRating(0);
            tvRatingScale.setText("Rating");
            Toast.makeText(getContext(), "Thanks for sharing your feedback!", Toast.LENGTH_SHORT).show();
        }
    }
}
