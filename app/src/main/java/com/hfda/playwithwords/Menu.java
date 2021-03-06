package com.hfda.playwithwords;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Visibility;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Menu extends AppCompatActivity implements fromFragToContainer
{
    public static  boolean muteSound=false;

    //First We Declare Titles And Icons For Our Navigation Drawer List View
    String TITLES[] = {"HOME", "RANKING", "ABOUT US", "FEED BACK","SETTING", "LOGOUT"};
    //This Icons And Titles Are holded in an Array as you can see
    int ICONS[] = {R.drawable.ic_home_icon, R.drawable.ic_buffoon, R.drawable.ic_about_us,R.drawable.ic_love,R.drawable.ic_setting ,R.drawable.ic_logout};

    //Similarly we Create a String Resource for the UserName, Point, Rank  in the header view

    //And we also create a int resource for profile picture in the header view
    int profile = R.drawable.logo;//Hình user

    private Toolbar toolbar;                              // Declaring the Toolbar Object
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    FloatingActionButton fab;
    int out = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Fragment fragmentHome = new FragmentHome();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, fragmentHome);
        ft.addToBackStack("HOME");
        ft.commit();
        MainActivity.readUserInfo();

        setContentView(R.layout.activity_menu);
        //Chạy nhạc nền trên mwnd hình menu
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        mediaPlayer=MediaPlayer.create(this,R.raw.musbackground);
        Intent intent1 = getIntent();
        if(intent1.getStringExtra(SignInFragment.CHECKMUSIC).equals("SOUND")||
                intent1.getStringExtra(MainActivity.CHECKMUSIC).equals("SOUND")||
                intent1.getStringExtra(Result.CHECKMUSIC).equals("SOUND")||
                intent1.getStringExtra(Introduction.CHECKMUSIC).equals("SOUND")||
                intent1.getStringExtra(Round.CHECKMUSIC).equals("SOUND"))
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
    /* Assinging the toolbar object ot the view
    and setting the the Action bar to our toolbar
     */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

       // mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        long score = MainActivity.mUser.get(MainActivity.indexUser(MainActivity.userName)).getTotalScore();
        int rank=FragmentRankings.userRanking(MainActivity.userName);
        String sttRank;
        if(rank==1) sttRank=rank+"st";
        else if(rank==2) sttRank=rank+"nd";
        else if(rank==3) sttRank=rank+"rd";
        else
            sttRank=rank+"th";
        mAdapter = new MyAdapter(TITLES, ICONS, MainActivity.userName, "Total Score: " + score, sttRank, profile,this); // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view username, header view point,header view rank
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        final GestureDetector mGestureDetector = new GestureDetector(Menu.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }

        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent)
            {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());

                if(child!=null && mGestureDetector.onTouchEvent(motionEvent))
                {
                    Drawer.closeDrawers();
                   // Toast.makeText(Menu.this,"The Item Clicked is: "+recyclerView.getChildPosition(child),Toast.LENGTH_SHORT).show();
                    onTouchDrawer(mRecyclerView.getChildPosition(child));
                    return true;
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent)
            {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b)
            {

            }
        });

        //floating button point -------
        fab = findViewById(R.id.myFAB);
        final Intent intent = new Intent(this, org.tensorflow.demo.DetectorActivity.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }


        }; // Drawer Toggle Object Made*/
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
    }

    public void onTouchDrawer(final int childPosition)
    {

        switch (childPosition)
        {
            case 1:
               fab.show();
                Fragment fragment3=new FragmentHome();
                replaceFragment(fragment3,"HOME");
                out=0;
                break;
            case 2:
                fab.hide();
                Fragment fragment2=new FragmentRankings();
                replaceFragment(fragment2,"RANKING");
                out=0;
                break;

            case 3:
                fab.hide();
                Fragment fragment1=new FragmentAboutUs();
                replaceFragment(fragment1,"ABOUT");
                out=0;
                break;
            case 4:
                fab.hide();
                Fragment fragment4=new Fragment_FeedBack();
                replaceFragment(fragment4,"FEED BACK");
                out=0;
                break;
            case 5:
                fab.hide();
                Fragment fragment5=new Fragment_Setting();
                replaceFragment(fragment5,"SETTING");
                out=0;
                break;
            case 6:

                final Button btnYesLogOut;
                final Button btnNoLogOut;
                GifImageView gifImageView;

                final Dialog dialog=new Dialog(this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_logout);

                gifImageView=dialog.findViewById(R.id.GifImageViewLogout);
                gifImageView.setVisibility(View.VISIBLE);
                gifImageView.setGifImageResource(R.drawable.gif_rabbit_dialog);
                btnYesLogOut=dialog.findViewById(R.id.btnYesLogout);
                btnNoLogOut=dialog.findViewById(R.id.btnNoLogout);
                //Khong muon thoat
                btnNoLogOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                //Muon LogOut
                btnYesLogOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Chuyen qua man hinh LogOut, Chao tam biet!
                        Intent intent = new Intent(getApplicationContext(),LogoutActivity.class);
                        startActivity(intent);
                        // đồng thời xóa thông tin lưu trữ trong database SQLite
                        SQLiteOpenHelper UserDB = new UserLogedIn(getApplicationContext());
                        SQLiteDatabase db = UserDB.getReadableDatabase();
                        long delete = db.delete("USER", null, null);
                        finish();
                    }
                });

                dialog.show();
                break;
            default:
                break;
        }
    }
    /*--------------------------------------------------------------------------------------------*/
    public void replaceFragment(Fragment fragment, String tag)
    {

        //Get current fragment placed in container
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame);
        //Prevent adding same fragment on top
        if (currentFragment.getClass() == fragment.getClass()) {
            return;
        }
        //If fragment is already on stack, we can pop back stack to prevent stack infinite growth
        if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
            getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        //Otherwise, just replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(tag)
                .replace(R.id.frame, fragment)
                .commit();

    }
    @Override
    public void onBackPressed()
    {
        int fragmentsInStack = getSupportFragmentManager().getBackStackEntryCount();
        while (fragmentsInStack > 1)
        { // If we have more than one fragment, pop back stack
            getSupportFragmentManager().popBackStack();
            fragmentsInStack--;
            fab.show();
        }
        if (fragmentsInStack == 1)
        {
           // Finish activity, if only one fragment left, to prevent
            //leaving empty screen
            Context context = getApplicationContext();
            CharSequence text = "Press back again to exit!";
            out++;
            int duration = Toast.LENGTH_SHORT;
            if(out<2)
            {
                Toast toast = Toast.makeText(context,text, duration);
                toast.show();
            }
            if(out==2)
                finish();
        }
        else
            super.onBackPressed();
    }
    @Override
    public void Action(String action)
    {
        if(action.equals("SOUND")){
            mediaPlayer=MediaPlayer.create(this,R.raw.musbackground);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
        if(action.equals("MUTE_SOUND")){

            Menu.muteSound=true;
            mediaPlayer.stop();
        }
        if(action.equals("UP")){
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        }
        if(action.equals("DOWN")){
            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        finish();
    }
    @Override
    public void onStop()
    {
        super.onStop();
        //Dừng nhạc nền khi thoát khỏi menu
        mediaPlayer.stop();
        finish();
    }


}



