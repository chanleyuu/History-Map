package com.example.historymap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static java.lang.Integer.parseInt;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    final private int REQUEST_COARSE_ACCESS = 123;
    boolean permissionGranted = false;
    LocationManager lm;
    LocationListener locationListener;
    DatabaseHelper db;
    CustomInfoWindowAdapter adapter;

    ArrayList<location> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locations = new ArrayList<location>();
        // populate the arrays based on button clicked
        String choice = getIntent().getStringExtra("choice");
        switch (choice){
            case "restaurants":
                addRestaurants();
                break;
            case "hotels":
                addHotels();
                break;
            case "history":
                addHistorical();
                break;
            case "pubs":
                addPubs();
                break;
            case "coffee":
                addCoffeeShops();
                break;
            case "shopping":
                addShopping();
                break;
        }

    }


    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },REQUEST_COARSE_ACCESS);
            return;
        } else {
            permissionGranted = true;
        }
        if (permissionGranted) {
            lm.removeUpdates(locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_ACCESS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                } else {
                    permissionGranted = false;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        db = new DatabaseHelper(this);

        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },REQUEST_COARSE_ACCESS);
            return;

        } else {
            permissionGranted = true;
        }
        if (permissionGranted) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0, locationListener);
        }

        //double locs[] = { 0.0, 0.0 };
        //boolean e = db.addlocation(35.0, -112.2, "Desert Location");
        //if (e) {

       // LatLng p = new LatLng(locs[0], locs[1]);
       // mMap.addMarker(new MarkerOptions().position(p).title("Delivery Location")
          //      .icon(BitmapDescriptorFactory
            //            .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        location loc = new location();
        loc.name = "The invention of history";
        loc.locx = 54.993505;
        loc.locy = -7.33697;
        loc.description = "It was at this place that" +
                " Johnathan Swift, the inventor of the Swift programming language" +
                " got the idea to write" +
                " things that happened down";
        loc.image = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Jonathan_Swift_by_Charles_Jervas_detail.jpg/330px-Jonathan_Swift_by_Charles_Jervas_detail.jpg";
        locations.add(loc);
        //  loc = db.getlocation(location.getLatitude(), location.getLongitude());
        //            .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        location loc2 = new location();
        loc2.name = "The death of Cu Culainn ";
        loc2.locx = 53.974505;
        loc2.locy = 6.465919;
        loc2.description = "It was at this aproximate place that" +
                " The hero Cu Chulainn died after being struck " +
                "by one of three spears each fortold to slay a king " +
                "he tied himself to a rock so that he may die standing up.";
        loc2.image = "https://upload.wikimedia.org/wikipedia/commons/4/4a/Cuchulainn%27s_death%2C_illustration_by_Stephen_Reid_1904.jpg";
        locations.add(loc2);
        //  }
        //  }
        //  for (int i = 0; i < locs.size(); i++){

        for (int i = 0; i < locations.size(); i++){
            LatLng q = new LatLng(locations.get(i).locx,locations.get(i).locy);
            mMap.addMarker(new MarkerOptions().position(q).title(locations.get(i).name).snippet(locations.get(i).description)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }

        LatLng q = new LatLng(loc.locx, loc.locy);
        LatLng r = new LatLng(loc2.locx, loc2.locy);
        MarkerOptions markeropt = new MarkerOptions();
        MarkerOptions markeropt2 = new MarkerOptions();
        markeropt = markeropt.position(q).title(loc.name)
                .snippet(loc.description)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
               /*    Marker marker = mMap.addMarker(new MarkerOptions().position(q).title(loc.name)
                           .snippet(loc.description)
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); */
        markeropt2 = markeropt2.position(r).title(loc2.name)
                .snippet(loc2.description)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        adapter = new CustomInfoWindowAdapter(this, loc.image);
        //CustomInfoWindowAdapter adapter2 = new CustomInfoWindowAdapter(this, loc2.image);

        mMap.setInfoWindowAdapter(adapter);
        //mMap.setInfoWindowAdapter(adapter2);

        mMap.addMarker(markeropt).showInfoWindow();
        mMap.addMarker(markeropt2);

    }

    @Override
    public boolean onMarkerClick(Marker marker){
        System.out.println(marker.getId());
        String id = marker.getId();
        int index = parseInt(id.substring(1, id.length()));
    /*
        if(marker.getId().equals("m0")){
            try {
                adapter.SetImage("https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Jonathan_Swift_by_Charles_Jervas_detail.jpg/330px-Jonathan_Swift_by_Charles_Jervas_detail.jpg");
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if(marker.getId().equals("m1")){
            try {
                adapter.SetImage("https://upload.wikimedia.org/wikipedia/commons/4/4a/Cuchulainn%27s_death%2C_illustration_by_Stephen_Reid_1904.jpg");
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } */
        try {
            adapter.SetImage(locations.get(index).image);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        marker.showInfoWindow();
        return true;
    }

    public void addLocation(double x, double y, String name, String description, String url){
        db = new DatabaseHelper(this);
        location loc = new location();
        loc.type = "Restaurants";
        loc.name = name;
        loc.description = description;
        loc.locx = x;
        loc.locy = y;
        loc.image = url;
       // db.addlocation(loc);
        locations.add(loc);
    }

    // populates the arrays with restaurants
    public void addRestaurants(){
        addLocation(54.9995204463077,    -7.319964908155055,    "The Exchange",                     "4.6 (682)\nModern European restaurant\nQueens Quay, Londonderry BT48 7AY\nOpening Hours:  12–2:30pm, 5–10pm\nexchangerestaurant.com\n028 7127 3990", "https://3qp9ou1uhqfi24p3t86az415-wpengine.netdna-ssl.com/wp-content/uploads/2016/05/Exchange-Part-2-14-scaled.jpg");
        addLocation(55.01840589321865,   -7.325598487625408,    "Scarpello & Co",                   "4.9 (37)\nPizza restaurant\n22B Buncrana Rd, Londonderry BT48 8AB\nOpening Hours: 10am - 10pm\nscarpelloandco.ie\n028 7121 1046", "http://www.scarpelloandco.com/wp-content/uploads/2020/11/home-2.jpg");
        addLocation(55.004088041480074,  -7.321582661592111,    "Quaywest Wine Bar & Restaurant",   "4.5 (1,307)\nRestaurant\n28 Boating Club Ln, Londonderry BT48 7QB\nOpening Hours: 4–10pm\nquaywestrestaurant.com\n028 7137 0977", "https://www.quaywestrestaurant.com/wp-content/uploads/2018/09/IMG_7019-675x225.jpg");
        addLocation(54.997736567183274,  -7.322765322966934,    "Mekong Street Food",               "4.9 (50)\nRestaurant\n7-8 Magazine St, Londonderry BT48 6HJ\nOpening Hours: 3pm – 9pm\nfacebook.com/MekongSF\n07835 330360", "https://scontent.fman2-2.fna.fbcdn.net/v/t31.0-8/23334032_445970205798873_6817487156220927503_o.jpg?_nc_cat=111&ccb=2&_nc_sid=730e14&_nc_ohc=msFPCRW9_cMAX8mlr4Y&_nc_ht=scontent.fman2-2.fna&oh=e33b0d1577ff85b24d5f9e13ad662470&oe=5FF9BFD6");
        addLocation(55.00231509478489,   -7.322757792279522,    "Guapo Fresh Mexican",              "4.8 (577)\nBurrito restaurant\n69 Strand Rd, Londonderry BT48 7AD\nOpening Hours: 12–9pm\n028 7136 5585", "https://media-cdn.tripadvisor.com/media/photo-o/04/0e/9d/6e/guapo.jpg");
        addLocation(55.01082716489911,   -7.318217048508025,    "Chillis Indian Restaurant",        "4.5 (105)\nIndian restaurant\n145 Strand Rd, Londonderry BT48 7PW\nOpening Hours: 5pm - 11pm\nchillisrestaurant.net\n028 7126 2050", "https://c1.staticflickr.com/1/634/31761938111_f1317e9cf9_h.jpg");
        addLocation(55.015406001668126,  -7.31238055858301,     "Caterina’s bistro",                "4.6 (19)\nRestaurant\n15 Culmore Rd, Londonderry BT48 8JB\nOpening Hours:  12–9:30pm", "https://scontent.fman2-2.fna.fbcdn.net/v/t1.0-9/90480344_3796740220398939_3102690374699712512_o.jpg?_nc_cat=111&ccb=2&_nc_sid=8bfeb9&_nc_ohc=8-o9c2a8LX0AX_bkqUq&_nc_ht=scontent.fman2-2.fna&oh=9ee6751d755533c77ac14e3501f1600f&oe=5FF73983");
        addLocation(54.99799325915123,   -7.321525438842466,    "Castle Street Social",             "5.0 (12)\nRestaurant\n12-14 Castle St, Londonderry BT48 6HQ\nOpening Hours: 12–9pm\n028 7137 2888", "https://www.ukphotoarchive.org.uk/img/s/v-10/p3222627568-3.jpg");
    }

    // populates the arrays with hotels
    public void addHotels(){
        addLocation(54.99607644345425,   -7.322795202277289,    "Maldron Hotel Derry",          "4.4 (1,238)\nButcher St, Londonderry BT48 6HL\n028 7137 1000", "");
        addLocation(54.99966086801288,   -7.321643721950678,    "Holiday Inn Express Derry",    "4.6 (168)\n31 Strand Rd, Londonderry BT48 7BL\nihg.com\n028 7116 2400", "");
        addLocation(55.01457091089826,   -7.312483272041897,    "Da Vinci's Hotel",             "4.4 (288)\n15 Culmore Rd, Londonderry BT48 8JB\ndavincishotel.com\n028 7127 9111", "");
        addLocation(54.999315710612656,  -7.3229031569087475,   "Hogg & Mitchell Apartments",   "4.4 (70)\n15 Great James St, Londonderry BT48 7AB\nhoggandmitchells.co.uk\n028 7187 8028", "");
        addLocation(54.99923957380815,   -7.3203926405975555,   "City Hotel Derry",             "4.3 (1,492)\nQueens Quay, Londonderry BT48 7AS\ncityhotelderry.com\n028 7136 5800", "");
        addLocation(54.99478259617965,   -7.3230446427488625,   "Bishop's Gate Hotel Derry",    "4.7 (1,041)\n24 Bishop St, Londonderry BT48 6PP\nbishopsgatehotelderry.com\n028 7114 0300", "");
        addLocation(55.01002805936406,   -7.279739576925149,    "The Waterfoot Hotel",          "4.1 (752)\nCaw Roundabout, Londonderry BT47 6TB\nwaterfoothotel.com\n028 7134 5500", "");
        addLocation(54.9962053664103,    -7.3208659986431766,   "Shipquay Hotel",               "4.7 (339)\n15-17 Shipquay St, Londonderry BT48 6DJ\nshipquayhotel.com\n028 7126 7266", "");
    }

    // populates the arrays with historical places
    public void addHistorical(){
        addLocation(54.99518412768551, -7.323958860571264, "The Derry Walls",           "4.7 (1,778)\nThe Diamond, Londonderry BT48 6HW\nOpen 24 hours\nthederrywalls.com\n07894 534553", "");
        addLocation(54.99658118925993, -7.325357259360941, "Bloody Sunday Monument",    "4.5 (112)\n29-37 Joseph Pl, Londonderry BT48 6LH\nOpen 24 hours", "");
        addLocation(54.99343650072822, -7.321219709173047, "Church Bastion",            "4.0 (1)\n222 The Fountain, Londonderry BT48 6JP\nOpen now:  Open 24 hours", "");
        addLocation(54.99576408872815, -7.326773835718739, "Free Derry Corner",         "4.7 (1,237)\nLondonderry BT48 9DR\nOpen 24 hours", "");
        addLocation(54.99701283319828, -7.319891463775096, "Shipquay Gate",             "4.8 (13)\n1A Shipquay Pl, Londonderry BT48 6DH\nOpen now:  Open 24 hours", "");
    }

    // populates the arrays with pubs
    public void addPubs(){
        addLocation(54.996488079025255,  -7.323344094983625,    "The Rocking Chair Bar",        "4.4 (197)\n15-17 Waterloo St, Londonderry BT48 6HA\nOpening Hours  11am – 1am\nrockingchair.online\n028 7128 1200", "");
        addLocation(54.9952559894251,    -7.319138853471543,    "Badgers Bar and Restaurant",   "4.6 (635)\n18 Orchard St, Londonderry BT48 6EG\nOpening Hours: 12pm – 1am\n028 7136 3306", "");
        addLocation(54.99653237153532,   -7.318288726253522,    "Blackbird",                    "4.5 (890)\n24 Foyle St, Londonderry BT48 6AL\nOpening Horus: 11:30am – 1am\nblackbirdderry.com\n028 7136 2111", "");
        addLocation(54.997050999382985,  -7.322201894995344,    "Dungloe Bar",                  "4.4 (567)\n41-43 Waterloo St, Londonderry BT48 6HD\nOpening Hours: 11:30am – 1am\n028 7126 7716", "");
        addLocation(54.997077933376296,  -7.319473961091456,    "Gainsborough Bar",             "4.3 (106)\n6 Shipquay Pl, Londonderry BT48 6DF\nOpen 24 hours", "");
        addLocation(54.99747116063894,   -7.321771032542148,    "Peadar O'Donnell's",           "4.6 (1,111)\n63 Waterloo St, Londonderry BT48 6HD\nOpening Hours: 11:30am – 1:30am\npeadars.com\n028 7126 7295", "");
        addLocation(54.996929886332865,  -7.32019860382748,     "River Inn",                    "4.3 (381)\n34-38 Shipquay St, Londonderry BT48 6DW\nOpening Hours: 11:30am – 1:30am\nriverinn1684.com\n028 7137 1965", "");
        addLocation(54.99692975170105,   -7.322197361028215,    "The Castle Bar",               "4.4 (72)\n26 Waterloo St, Londonderry BT48 6HF\nOpening Hours: 12:30pm – 1:30am\nthe-castle-bar.poi.place\n028 7136 5013", "");
    }

    // populates the arrays with coffee shops
    public void addCoffeeShops(){
        addLocation(54.99774846214825,   -7.319748637770263,    "Warehouse No 1 Ltd",                           "4.4 (163)\n1-3 Guildhall St, Londonderry BT48 6BB\nOpening Hours: 9am – 9:30pm\nthewarehousederry.com\n028 7126 4798", "");
        addLocation(54.99490041815157,   -7.317949437978594,    "Costa",                                        "4.2 (84)\n19 Orchard St, Londonderry BT48 6XY\nOpening Hours: 8am – 9pm\ncostaireland.ie\n028 7136 5556", "");
        addLocation(54.99536857056998,   -7.3099874839065055,   "Leprechaun Bakery & Coffee Shop",              "4.4 (50)\n41 Clooney Terrace, Londonderry BT47 6AP\n028 7134 5141", "");
        addLocation(55.00660514467516,   -7.319545046987329,    "Patricia's Coffee House",                      "4.6 (207)\nUnit 1a, Atlantic Quay, 100-114 Strand Rd, Londonderry BT48 7NR\nOpening Hours: 9am - 5pm\n028 7137 0302", "");
        addLocation(54.99474999112457,   -7.318566086914426,    "Starbucks",                                    "4.3 (322)\nLondenderry Foyleside, Londonderry BT48 6XY\nOpening Hours: 10am – 5:30pm\nstarbucks.co.uk\n028 7136 8113", "");
        addLocation(55.000388270971705,  -7.322351332649952,    "The Coffee Tree",                              "4.9 (154)\n49 Strand Rd, Londonderry BT48 7BN\nOpening Hours: 10:30am–4am", "");
        addLocation(54.99693931503422,   -7.320985231578226,    "The Cottage Craft Gallery and Coffee Shop",    "4.9 (33)\nUnit 21 The, Shipquay St, Londonderry BT48 6AR\nOpening Hours: 8:30am – 6pm\n07783 882521", "");
        addLocation(55.00210962855509,   -7.322337244267519,    "The Hatter Express Derry",                     "5.0 (3)\n70 Strand Rd, Londonderry BT48 7AJ\nOpening Hours: 8am – 5pm\n028 7187 8034", "");
    }

    // populates the arrays with shopping centres
    public void addShopping(){
        addLocation(54.994730956866356,  -7.319163705980926, "Foyleside Shopping Centre",   "4.4 (3,376)\n19 Orchard St, Londonderry BT48 6XY\n9am–7pm\nfoyleside.co.uk\n028 7137 7575", "");
        addLocation(54.989082343822005,  -7.296606059240908, "Lisnagelvin Shopping Centre", "4.1 (1,484)\nLisnagelvin Rd, Londonderry BT47 6DF\nOpening Hours: 8am – 8pm\nlisnagelvinsc.com\n028 7132 9409", "");
        addLocation(55.000245374566056,  -7.321586821056816, "Quayside Shopping Centre",    "4.1 (849)\n42 Strand Rd, Londonderry BT48 7PX\nOpening Hours: 7am – 9:45pm\nquaysidecentre.co.uk\n028 7137 4037", "");
        addLocation(54.99578665086524,   -7.320638311365454, "Richmond Shopping Centre",    "4.2 (1,739)\nFerryquay St, Londonderry BT48 6QP\nOpening Hours: 8:30am – 9pm\nrichmondcentre.co.uk\n028 7126 0525", "");
    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {
                Toast.makeText(getBaseContext(),
                        "Current Location : Lat: " + location.getLatitude() +
                                " Lng: " + location.getLongitude(), Toast.LENGTH_LONG).show();
                LatLng p = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(p).title("Current Location")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p, 12.0f));
                ArrayList<location> locs = new ArrayList<location>();
             //   for (int i = -10; i < 10; i++) {
             //       for(int e = -10; e < 10; e++) {
                /*
                        location loc = new location();
                        loc.name = "The invention of history";
                        loc.locx = 54.993505;
                        loc.locy = -7.33697;
                        loc.description = "It was at this place that" +
                                " Johnathan Swift inventor of the Swift programming language" +
                                " got the idea to write" +
                                " things that happened down";
                      //  loc = db.getlocation(location.getLatitude(), location.getLongitude());
                        if (loc != null) {
                            locs.add(loc);
                        }
                  //  }
              //  }
              //  for (int i = 0; i < locs.size(); i++){
                    LatLng q = new LatLng(loc.locx, loc.locy);
                       MarkerOptions markeropt = new MarkerOptions();
                       markeropt = markeropt.position(q).title(loc.name)
                               .snippet(loc.description)
                               .icon(BitmapDescriptorFactory
                                       .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                   Marker marker = mMap.addMarker(new MarkerOptions().position(q).title(loc.name)
                           .snippet(loc.description)
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(this);
                mMap.setInfoWindowAdapter(adapter);

                mMap.addMarker(markeropt).showInfoWindow(); */
                }
           // }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }


    }
}