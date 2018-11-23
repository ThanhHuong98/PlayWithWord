package com.hfda.playwithwords;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class Menu extends AppCompatActivity
{

   /* private boolean mIsBound = false;
    private MusicService mServ;

    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.ServiceBinder binder = (MusicService.ServiceBinder)service;
            mServ =binder.getService();
            mIsBound=true;
        }

        public void onServiceDisconnected(ComponentName name) {
            mIsBound=false;
        }
    };*/
    /*---------------------------------------------------------------------------*/
    //First We Declare Titles And Icons For Our Navigation Drawer List View
    String TITLES[] = {"HOME", "RANKING", "ABOUT US", "FEED BACK", "LOGOUT"};
    //This Icons And Titles Are holded in an Array as you can see
    int ICONS[] = {R.drawable.ic_home_icon, R.drawable.ic_buffoon, R.drawable.ic_about_us,R.drawable.ic_love ,R.drawable.ic_logout};

    //Similarly we Create a String Resource for the UserName, Point, Rank  in the header view
    //Sau nany doc tu database vao
    String userName = "Thanh Huong";
    String point = "250";
    String rank = "2nd";
    //And we also create a int resource for profile picture in the header view
    int profile = R.drawable.logo;//Hình user

    private Toolbar toolbar;                              // Declaring the Toolbar Object
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Fragment fragmentHome = new FragmentHome();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, fragmentHome);
        ft.addToBackStack("HOME");
        ft.commit();

        setContentView(R.layout.activity_menu);

    /* Assinging the toolbar object ot the view
    and setting the the Action bar to our toolbar
     */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

       // mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter(TITLES, ICONS, userName, point, rank, profile,this); // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view username, header view point,header view rank;
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
                Fragment fragment3=new FragmentHome();
                replaceFragment(fragment3,"HOME");
                break;
            case 2:

                Fragment fragment2=new FragmentRankings();
                replaceFragment(fragment2,"RANKING");
                break;

            case 3:
                Fragment fragment1=new FragmentAboutUs();
                replaceFragment(fragment1,"ABOUT");
                break;
            case 4:
                Fragment fragment4=new Fragment_FeedBack();
                replaceFragment(fragment4,"FEED BACK");
                break;
            case 5:
                //Dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure, you want to Log-out");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1)
                            {
                                //Doi Activity SignIn cua Huan
                               /*Intent intent = new Intent(getApplicationContext(),Login.class);
                               startActivity(intent);*/

                                //Toast.makeText(MainActivity.this,"Return SignIn",Toast.LENGTH_LONG).show();

                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                       /* Fragment fragment3 = new FragmentHome();
                        replaceFragment(fragment3,"HOME");*/

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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
        }
        if (fragmentsInStack == 1) { // Finish activity, if only one fragment left, to prevent
            /*Intent intent = new Intent(getApplicationContext(),MainSignIn_SignUpActivity.class);
            startActivity(intent);*/
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        finish();
    }
  /*  @Override
    protected void onStart()
   {
       super.onStart();
        // Tạo đối tượng Intent cho MusicService.
      Intent intent = new Intent(Menu.this,MusicService.class);
        // Gọi method bindService(..) để giàng buộc dịch vụ với giao diện.
            this.bindService(intent, Scon, Context.BIND_ADJUST_WITH_ACTIVITY);
            startService(intent);
    }*/
}



