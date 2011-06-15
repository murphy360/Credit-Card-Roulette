package com.andrios.creditcardroulette;


import java.util.ArrayList;
import java.util.Comparator;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;

public class PlayerSelectActivity extends Activity {

	Button addBTN, playBTN;
	ViewFlipper flipper;//Used to Show animation between Back / Front of card.
	ArrayList<String> contacts, available, selected;
	ListView contactsLV, availableLV;
	ArrayAdapter<String> contactsAdapter, availableAdapter;

	GoogleAnalyticsTracker tracker;
	AdView adView;
	AdRequest request;
	
	/** Called when the activity is first created. */
	 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerselectactivity);
        
        setConnections();
        queryContacts();
        
        setAdapters();
        setOnClickListeners();
        setTracker();
        adView = (AdView)this.findViewById(R.id.playerSelectAdView);
	      
	    request = new AdRequest();
		request.setTesting(false);
		adView.loadAd(request);
    }

    private void queryContacts(){
    	ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                   null, null, null, null);
        if (cur.getCount() > 0) {
	   	    while (cur.moveToNext()) {
	   	        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
	   	        String name = cur.getString(
	                           cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	   	        
	   	        
	   	        contacts.add(name);
            }
    	}
    }


	private void setConnections() {
		// TODO Auto-generated method stub
		addBTN = (Button) findViewById(R.id.playerSelectActivityAddBTN);
		playBTN = (Button) findViewById(R.id.playerSelectActivityPlayBTN);
		flipper = (ViewFlipper) findViewById(R.id.viewFlipper1); 
		flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
	    flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));  
		
	    contactsLV = (ListView) findViewById(R.id.playerSelectActivityContactsListView);
	    availableLV = (ListView) findViewById(R.id.playerSelectActivityAvailableListView);
	    
	    contacts = new ArrayList<String>();
	    available = new ArrayList<String>();
	}
	

	private void setAdapters(){
		contactsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
		contactsLV.setAdapter(contactsAdapter);
		contactsAdapter.sort(new Comparator<String>() {

			public int compare(String object1, String object2) {
				return object1.compareToIgnoreCase(
						object2);
			}

		});
		contactsAdapter.setNotifyOnChange(true);
		
		availableAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, available);
		availableLV.setAdapter(availableAdapter);
	
		availableAdapter.setNotifyOnChange(true);
		
		
	}
	
	private void setOnClickListeners() {
		// TODO Auto-generated method stub
		addBTN.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(addBTN.getText().equals("Select Players")){
					addBTN.setText("View Selected");
				}else{
					addBTN.setText("Select Players");
				}
				flipper.showNext();
				
			}
			
		});
		
		playBTN.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(available.size() < 2){
					Toast.makeText(PlayerSelectActivity.this,
							"Must choose at least two players",
							Toast.LENGTH_SHORT).show();
				}else{
					Intent intent = new Intent(v.getContext(),
							RouletteBasicActivity.class);
					intent.putExtra("players", available);
					
					startActivity(intent);
				}
				
				
			}
			
		});
		
		contactsLV.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int row,
					long arg3) {
				 

				String s = (String) contactsAdapter.getItem(row);
			
				contactsAdapter.remove(s);
				availableAdapter.add(s);
				availableAdapter.sort(new Comparator<String>() {

					public int compare(String object1, String object2) {
						return object1.compareToIgnoreCase(
								object2);
					}

				});
			
				
			}

		});
		
		availableLV.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int row,
					long arg3) {
				 

				String s = (String) availableAdapter.getItem(row);
			
				availableAdapter.remove(s);
				contactsAdapter.add(s);
				contactsAdapter.sort(new Comparator<String>() {

					public int compare(String object1, String object2) {
						return object1.compareToIgnoreCase(
								object2);
					}

				});
			
				
			}

		});
	}
	
	public void onResume(){
		super.onResume();
		tracker.trackPageView("Player Select");
	}
	
	public void onPause(){
		super.onPause();
		tracker.dispatch();
	}
	
	private void setTracker() {
		tracker = GoogleAnalyticsTracker.getInstance();

	    // Start the tracker in manual dispatch mode...
	    tracker.start("UA-23366060-4", this);
	    
		
	}
	
	
}
