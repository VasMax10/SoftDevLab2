package com.example.softdevlab2.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.directions.route.*
import com.example.softdevlab2.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment(), OnMapReadyCallback,
    GoogleApiClient.OnConnectionFailedListener, RoutingListener {
    //google map object
    private var mMap: GoogleMap? = null
    private lateinit var mapViewModel: MapViewModel

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    //current and destination location objects
    var myLocation: Location? = null
    var destinationLocation: Location? = null
    protected var start: LatLng? = LatLng(32.7141, -117.1538)
    protected var end: LatLng? = null
    var locationPermission = false

    //polyline object
    private var polylines: MutableList<Polyline>? = null

    private var brand : String? = ""
    private var city : String? = ""

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        mapViewModel =
                ViewModelProvider(this).get(MapViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_map, container, false)

        brand = arguments?.getString("brand")
        city = arguments?.getString("city")

        val supportMap = childFragmentManager.findFragmentById(R.id.mapField) as  SupportMapFragment

        supportMap.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            requireActivity()
        )

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun requestPermision() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        } else {
            locationPermission = true
        }
    }


    //to get user location
    private fun getMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermision()
            return
        }
        mMap!!.isMyLocationEnabled = true
        mMap!!.setOnMyLocationChangeListener { location ->
            myLocation = location
            val ltlng = LatLng(location.latitude, location.longitude)

//            mMap!!.animateCamera(cameraUpdate)
        }

        start = myLocation?.let { LatLng(it.latitude, it.longitude) }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap!!.setMinZoomPreference(3.0F)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
            LatLng(32.7141, -117.1538), 4f)
        mMap!!.moveCamera(cameraUpdate)
        mMap!!.uiSettings.isZoomControlsEnabled = true

        getMyLocation()

        val gc = Geocoder(context)
        val addresses = gc.getFromLocationName(city, 1);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    if (location != null) {
                        start = LatLng(location.latitude, location.longitude)
                    }
                    end = LatLng(addresses[0].latitude, addresses[0].longitude)
                    Findroutes(start, end)
                }
    }

    // function to find Routes.
    fun Findroutes(Start: LatLng?, End: LatLng?) {
        if (Start == null)
            Toast.makeText(context, "From is null", Toast.LENGTH_LONG).show()
        if (End == null)
            Toast.makeText(context, "End is null", Toast.LENGTH_LONG).show()
        if (Start == null || End == null) {
            Toast.makeText(context, "Unable to get location", Toast.LENGTH_LONG).show()
        } else {
            val routing = Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(Start, End)
                .key("AIzaSyAX6zFvXFj7O90ePH1-3_EAF1Ay55wF-T0") //also define your api key here.
                .build()
            routing.execute()
        }
    }

    //Routing call back functions.
    override fun onRoutingFailure(e: RouteException) {
//        val parentLayout: View = findViewById(R.id.content)
//        val snackbar = Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG)
//        snackbar.show()
        //        Findroutes(start,end);
    }

    override fun onRoutingStart() {
        Toast.makeText(context, "Finding Route...", Toast.LENGTH_LONG).show()
    }

    //If Route finding success..
    @SuppressLint("ResourceType")
    override fun onRoutingSuccess(route: ArrayList<Route>, shortestRouteIndex: Int) {
        val center = CameraUpdateFactory.newLatLng(start)
//        val zoom = CameraUpdateFactory.zoomTo(16f)
        if (polylines != null) {
            polylines!!.clear()
        }
        val polyOptions = PolylineOptions()
        var polylineStartLatLng: LatLng? = null
        var polylineEndLatLng: LatLng? = null
        polylines = ArrayList()
        //add route(s) to the map using polyline
        for (i in 0 until route.size) {
            if (i == shortestRouteIndex) {
                polyOptions.color(resources.getColor(R.color.colorPrimary))
                polyOptions.width(7f)
                polyOptions.addAll(route[shortestRouteIndex].points)
                val polyline = mMap!!.addPolyline(polyOptions)
                polylineStartLatLng = polyline.points[0]
                val k = polyline.points.size
                polylineEndLatLng = polyline.points[k - 1]
                (polylines as ArrayList<Polyline>).add(polyline)
            } else {
            }
        }

        //Add Marker on route starting position
        val startMarker = MarkerOptions()
        startMarker.position(polylineStartLatLng!!)
        startMarker.title("My Location")
        mMap!!.addMarker(startMarker)

        //Add Marker on route ending position
        val endMarker = MarkerOptions()
        endMarker.position(polylineEndLatLng!!)
        endMarker.title(brand)
        mMap!!.addMarker(endMarker)
    }

    override fun onRoutingCancelled() {
        Findroutes(start, end)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Findroutes(start, end)
    }

    companion object {
        //to get location permissions.
        private const val LOCATION_REQUEST_CODE = 23
    }
}
