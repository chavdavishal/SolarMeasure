package com.example.navigation;

import static android.os.Build.VERSION.SDK_INT;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.getbase.floatingactionbutton.BuildConfig.APPLICATION_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import androidx.camera.core.ImageCapture;
import android.Manifest;
import android.graphics.Matrix;
import android.view.TextureView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface,ProductInterface{

    ImageView cover;
    FloatingActionButton fab;

    private final int GALLERY_REQ_CODE = 1000;
    Button logout,btnFeedback;
    TextureView textureView;

    private PermissionManager permissionManager;
    private String[] permissions = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private ImageCapture imgCap;
    MeowBottomNavigation bottomNavigation;
    RelativeLayout home_layout,profile_layout;
    ConstraintLayout camera_layout;
    private Uri mPhotoUri;




    private Status status = Status.MODE_CHOICE;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int ACTIVITY_REQUEST_CODE = 101;

    private Activity thisActivity = this;
    private ImageButton cameraButton;
    private ImageButton galleryButton;
    private ImageView pictureView;
    private CameraRulerView drawView;
    private FloatingActionsMenu newMeasureButton;
    private com.getbase.floatingactionbutton.FloatingActionButton confirmButton;
    private Menu refsMenu;
    private RelativeLayout modeChoiceLayout;
    private Toolbar toolbar;
    private View discriptorText;
    private View cameraLabel;
    private View galleryLabel;
    private Bitmap photo;
    String mCurrentPhotoPath;


    DisplayMetrics displayMetrics = new DisplayMetrics();
    private String referenceObjectShape = "circle";
    private String referenceObjectName = "";
    private float referenceObjectSize = 1;

    // These matrices will be used to move and zoom image
    Matrix tmpMatrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    Matrix zoomMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    @Override
    public void onItemClick() {
        objectRvpAdapter=new RVPAdapter(objectDatabaseHelper.getAllProductData(),this);
        objectRecyclerView.setHasFixedSize(true);

        objectRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        objectRecyclerView.setAdapter(objectRvpAdapter);

    }

    public void setdesc(){

        int[] productimages = {R.drawable.im1,R.drawable.im2,R.drawable.im3,R.drawable.im4,
                R.drawable.im5,R.drawable.im6,R.drawable.im7,R.drawable.im8}  ;
        String [] desc= getResources().getStringArray(R.array.discription);

        for(int i = 0;i<productimages.length;i++){
            objectproductClassList.add(new DescModel(productimages[i],desc[i]));
        }
    }

    @Override
    public void getData(int pos) {
        setdesc();
  /*  Bitmap bitmap=objectDatabaseHelper.getAllProductData().get(pos).getImage();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();*/



        Intent intent=new Intent(MainActivity.this,ProductView.class);
        intent.putExtra("name",objectDatabaseHelper.getAllProductData().get(pos).getPname());
        intent.putExtra("price",objectDatabaseHelper.getAllProductData().get(pos).getPprice());
        intent.putExtra("watts",objectDatabaseHelper.getAllProductData().get(pos).getPwatts());

        intent.putExtra("image",objectproductClassList.get(pos).getImages());
        intent.putExtra("desc",objectproductClassList.get(pos).getDesc());
        startActivity(intent);
    }

    enum Status {
        MODE_CHOICE,
        REFERENCE,
        MEASUREMENT
    }



    private DBHelper objectDatabaseHelper;
    private RecyclerView objectRecyclerView;

    private RVAdapter objectRvAdapter;

    private RVPAdapter objectRvpAdapter;
    ArrayList<DescModel> objectproductClassList = new ArrayList<>();






    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objectRecyclerView = findViewById(R.id.imagesRV);

        objectDatabaseHelper=new DBHelper(this);

        objectRvAdapter=new RVAdapter(objectDatabaseHelper.getAllImagesData(),  this);
        objectRecyclerView.setHasFixedSize(true);

        objectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        objectRecyclerView.setAdapter(objectRvAdapter);






        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        cameraButton = (ImageButton) findViewById(R.id.from_camera_button);
        galleryButton = (ImageButton) findViewById(R.id.from_gallery_button);
        pictureView = (ImageView) findViewById(R.id.pictureView);
        confirmButton = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.confirm_reference);
        modeChoiceLayout = (RelativeLayout) findViewById(R.id.camera_ruler_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        discriptorText = findViewById(R.id.camera_gallery_choice_text);
        cameraLabel = findViewById(R.id.camera_button_label);
        galleryLabel = findViewById(R.id.gallery_button_label);

        newMeasureButton = (FloatingActionsMenu) findViewById(R.id.new_measure_fam);
        com.getbase.floatingactionbutton.FloatingActionButton newTetragonButton = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.new_tetragon_fab);
        com.getbase.floatingactionbutton.FloatingActionButton newTriangleButton = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.new_triangle_fab);
        com.getbase.floatingactionbutton.FloatingActionButton newCircleButton = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.new_circle_fab);
        com.getbase.floatingactionbutton.FloatingActionButton newLineButton = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.new_line_fab);

        drawView = new CameraRulerView(getBaseContext(), toolbar, this);
        drawView.ctxStatus = status;
        drawView.setVisibility(GONE);
        modeChoiceLayout.addView(drawView);

        pictureView.setImageMatrix(new Matrix());

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotoUri = FileProvider.getUriForFile(MainActivity.this, APPLICATION_ID + ".provider", createImageFile());
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.setClipData(ClipData.newRawUri("A photo", mPhotoUri));
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SDK_INT >= Build.VERSION_CODES.M) {
                    // check if we have the permission we need -> if not request it and turn on the light afterwards
                    if (ContextCompat.checkSelfPermission(thisActivity,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(thisActivity,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                        return;
                    }
                }
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawView.reference instanceof Polygon
                        && ((Polygon) drawView.reference).isSelfIntersecting()) {
                    Toast.makeText(getBaseContext(), getString(R.string.reference_self_intersecting), Toast.LENGTH_LONG).show();
                } else {
                    setReference();
                }
            }
        });

        newTetragonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMeasureButton.collapseImmediately();
                drawView.measure = drawView.newTetragon();
                drawView.invalidate();
            }
        });

        newTriangleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMeasureButton.collapseImmediately();
                drawView.measure = drawView.newTriangle();
                drawView.invalidate();
            }
        });

        newCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMeasureButton.collapseImmediately();
                drawView.measure = drawView.newCircle();
                drawView.invalidate();
            }
        });

        newLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMeasureButton.collapseImmediately();
                drawView.measure = drawView.newLine();
                drawView.invalidate();
            }
        });

        overridePendingTransition(0, 0);




        cover = findViewById(R.id.coverimg);
        fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iGallary = new Intent(Intent.ACTION_PICK);
                iGallary.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallary, GALLERY_REQ_CODE);

            }
        });
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(MainActivity.this,ActivityLogin.class);
                startActivities(intent1);
                finish();
                Toast.makeText(MainActivity.this,"Successfully",Toast.LENGTH_SHORT).show();
            }

            private void startActivities(Intent intent1) {
            }
        });

        btnFeedback=findViewById(R.id.btnFeedback);
        btnFeedback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, Feedback.class);
                startActivities(new Intent[]{intent});
            }
        });

        permissionManager = PermissionManager.getInstance(this);



        //main_layout = findViewById(R.id.main_layout);

        camera_layout = findViewById(R.id.camera_layout);
        home_layout = findViewById(R.id.home_layout);
        profile_layout = findViewById(R.id.profile_layout);
       // main_layout = findViewById(R.id.main_layout);

        camera_layout.setVisibility(View.GONE);
        home_layout.setVisibility(View.VISIBLE);
        profile_layout.setVisibility(View.GONE);


        bottomNavigation = findViewById(R.id.bottomNavigation);


        bottomNavigation.show(2,true);


        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.baseline_photo_camera_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.baseline_person_24));


        meownavigation();

    }



    private void meownavigation(){

        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                switch (model.getId()){

                    case 1:

                        camera_layout.setVisibility(View.VISIBLE);
                        home_layout.setVisibility(View.GONE);
                        profile_layout.setVisibility(View.GONE);

                        //main_layout.setBackgroundColor(Color.parseColor("#53281B"));

                        break;


                    case 2:

                        camera_layout.setVisibility(View.GONE);
                        home_layout.setVisibility(View.VISIBLE);
                        profile_layout.setVisibility(View.GONE);

                        //main_layout.setBackgroundColor(Color.parseColor("#4CAF50"));

                        break;


                    case 3:


                        camera_layout.setVisibility(View.GONE);
                        home_layout.setVisibility(View.GONE);
                        profile_layout.setVisibility(View.VISIBLE);

                        //main_layout.setBackgroundColor(Color.parseColor("#FF5722"));

                        break;

                }


                return null;
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchPoint = drawView.clickInTouchpoint(event);
        if (mode == NONE && touchPoint >= 0) { //click in touchpoint while no other gesture active
            drawView.activeTouchpoint = touchPoint;
            drawView.executeTouch(event);
        } else if (drawView.activeTouchpoint >= 0) { //further movements of grabbed touchpoint
            drawView.executeTouch(event);
        } else { //not in touchpoint or gesture already active
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    if (drawView.reference != null) {
                        drawView.reference.endMove();
                    }
                    if (drawView.measure != null) {
                        drawView.measure.endMove();
                    }
                    mode = NONE;
                    savedMatrix.set(tmpMatrix);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        tmpMatrix.set(savedMatrix);
                        tmpMatrix.postTranslate(event.getX() - start.x,
                                event.getY() - start.y);
                        if (drawView.reference != null) {
                            drawView.reference.move(event.getX() - start.x,
                                    event.getY() - start.y);
                        }
                        if (drawView.measure != null) {
                            drawView.measure.move(event.getX() - start.x,
                                    event.getY() - start.y);
                        }
                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            tmpMatrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            tmpMatrix.postScale(scale, scale, mid.x, mid.y);
                            zoomMatrix.setScale(scale, scale, mid.x, mid.y);
                            if (drawView.reference != null) {
                                drawView.reference.zoom(zoomMatrix);
                                setScale();
                            }
                            if (drawView.measure != null) {
                                drawView.measure.zoom(zoomMatrix);
                            }
                        }
                    }
                    break;
            }
            drawView.invalidate();
            pictureView.setImageMatrix(tmpMatrix);
        }
        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt((x * x + y * y));
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        refsMenu = menu;

        //add active user defined objects




        if (referenceObjectShape.equals("tetragon")) {
            drawView.reference = new Tetragon(new Point(400, 400), new Point(800, 400), new Point(800, 800), new Point(400, 800));
        } else if (referenceObjectShape.equals("line")) {
            drawView.reference = new Line(new Point(400, 400), new Point(800, 800));
        }
        drawView.invalidate();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int itemId = item.getItemId();

        toolbar.setSubtitle(referenceObjectName);

        if (referenceObjectShape.equals("circle") && !(drawView.reference instanceof Circle)) {
            drawView.reference = drawView.newCircle();
        } else if (referenceObjectShape.equals("tetragon") && !(drawView.reference instanceof Tetragon)) {
            drawView.reference = drawView.newTetragon();
        } else if (referenceObjectShape.equals("line") && !(drawView.reference instanceof Line)) {
            drawView.reference = drawView.newLine();
        }

        drawView.invalidate();
        return false;
    }

    /**
     * Receive response from external camera and gallery apps.
     *
     * @param requestCode code of the request sent to the activity
     * @param resultCode result code returned by the activity
     * @param data data returned by the activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputStream stream = null;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                try {
                    if (photo != null) {
                        photo.recycle();
                    }
                    stream = getContentResolver().openInputStream(data.getData());
                    photo = BitmapFactory.decodeStream(stream);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            pictureView.post(new Runnable() {
                                @Override
                                public void run() {
                                    computeTransformation(photo.getWidth(), photo.getHeight());
                                }
                            });
                        }
                    }).start();

                    pictureView.setImageBitmap(photo);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (requestCode == ACTIVITY_REQUEST_CODE) {
                pictureView.setImageBitmap(null);

                // Image saved to a generated MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                pictureView.setImageURI(mPhotoUri);
                final Drawable d = pictureView.getDrawable();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        pictureView.post(new Runnable() {
                            @Override
                            public void run() {
                                computeTransformation(d.getIntrinsicWidth(), d.getIntrinsicHeight());
                            }
                        });
                    }
                }).start();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    pictureView.post(new Runnable() {
                        @Override
                        public void run() {
                            startImageFragment();
                        }
                    });
                }
            }).start();
        } else {
            if (resultCode != RESULT_CANCELED) {
                if (requestCode == ACTIVITY_REQUEST_CODE) {
                    Log.e("Camera App crashed.", "Returned result code: " + resultCode);
                    Toast.makeText(this, "camera_crash", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("Gallery App crashed.", "Returned result code: " + resultCode);
                    Toast.makeText(this, "gallery_crash", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Computes how a picture should be transformed to best fit the space available in the app.
     * Sets the transformation matrix of pictureView accordingly.
     *
     * @param picWidth  original width of the image
     * @param picHeight original height of the image
     */

    private void computeTransformation(float picWidth, float picHeight) {
        float height = picHeight;
        float width = picWidth;

        Matrix matrix = new Matrix();
        if (height < width) {
            matrix.postRotate(90, 0f, 0f);
            //noinspection SuspiciousNameCombination
            height = width;
            //noinspection SuspiciousNameCombination
            width = picHeight;
            matrix.postTranslate(width, 0f);
        }
        float scaleW = displayMetrics.widthPixels / width;
        float scaleH = modeChoiceLayout.getHeight() / height;
        float scale = Math.max(scaleW, scaleH);
        height *= scale;
        width *= scale;
        matrix.postScale(scale, scale);

        if (scaleW < scaleH) { //width overscaled
            matrix.postTranslate(-(width - displayMetrics.widthPixels) / 2, 0f);
        } else if (scaleH < scaleW) { //height overscaled
            matrix.postTranslate(0f, -(height - modeChoiceLayout.getHeight()) / 2);
        }
        savedMatrix = matrix;
        pictureView.setImageMatrix(matrix);
    }

    /**
     * Starts the first page of the actual camera ruler functionality, the reference selection
     * and confirmation. Hides the explanatory text and the buttons of the source selection phase.
     * Shows the picture view and fills it with the picture fetched from the external app.
     * Shows the view for drawing shapes on top of the picture view, the button for confirming
     * the reference object and the menu for selecting the reference object.
     */
    public void startImageFragment() {
        status = Status.REFERENCE;
        drawView.ctxStatus = status;
        cameraButton.setVisibility(GONE);
        cameraButton.setClickable(false);
        galleryButton.setVisibility(GONE);
        galleryButton.setClickable(false);
        discriptorText.setVisibility(GONE);
        cameraLabel.setVisibility(GONE);
        galleryLabel.setVisibility(GONE);
        pictureView.setVisibility(VISIBLE);
        drawView.setVisibility(VISIBLE);
        drawView.setClickable(true);
        drawView.bringToFront();
        confirmButton.setVisibility(VISIBLE);
        toolbar.setTitle(R.string.reference_phase_title);
        toolbar.setSubtitle(referenceObjectName);
    }

    /**
     * Starts the second phase of teh main camera ruler functionality, the measurement. Computes the
     * real world unit to pixel ration from the size of the reference shape and the active reference
     * object. Hides the reference confirmation button and reference object selection menu. Shows
     * the action button group for measurement shape selection. Sets the current measure to a line.
     * Forces the draw view to redraw.
     */
    public void setReference() {
        setScale();

        status = Status.MEASUREMENT;
        drawView.ctxStatus = status;
        confirmButton.setVisibility(GONE);
        newMeasureButton.setVisibility(VISIBLE);
        drawView.measure = drawView.newLine();
        drawView.reference.active = false;
        toolbar.setTitle(R.string.measurement_phase_title);
        toolbar.setSubtitle(referenceObjectName);
        drawView.invalidate();
    }

    private void setScale() {
        if (drawView.reference instanceof Circle) {
            drawView.scale = referenceObjectSize / (((Circle) drawView.reference).radius * 2);
        } else if (drawView.reference instanceof Line) {
            drawView.scale = referenceObjectSize / ((Line) drawView.reference).getLength();
        } else {
            drawView.scale = (float) Math.sqrt(referenceObjectSize / ((Polygon) drawView.reference).getArea());
        }
    }

    @Override
    public void onBackPressed() {
        if (status == Status.REFERENCE) {
            status = Status.MODE_CHOICE;
            drawView.ctxStatus = status;
            cameraButton.setVisibility(VISIBLE);
            cameraButton.setClickable(true);
            galleryButton.setVisibility(VISIBLE);
            galleryButton.setClickable(true);
            discriptorText.setVisibility(VISIBLE);
            cameraLabel.setVisibility(VISIBLE);
            galleryLabel.setVisibility(VISIBLE);
            drawView.setVisibility(GONE);
            drawView.setClickable(false);
            pictureView.setVisibility(GONE);
            pictureView.setImageURI(Uri.EMPTY);
            confirmButton.setVisibility(GONE);
            toolbar.setSubtitle("");
        } else if (status == Status.MEASUREMENT) {
            status = Status.REFERENCE;
            drawView.ctxStatus = status;
            newMeasureButton.collapseImmediately();
            newMeasureButton.setVisibility(GONE);
            drawView.measure = null;
            drawView.reference.active = true;
            drawView.invalidate();
            confirmButton.setVisibility(VISIBLE);
            toolbar.setTitle(R.string.reference_phase_title);
            toolbar.setSubtitle(referenceObjectName);
        } else {
            super.onBackPressed();
        }
    }





    /**
     * Sets every item in the menu of available reference objects to be visible.
     */


    /**
     * Sets every item in the menu of available reference objects to be invisible.
     */


    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(getFilesDir(), "images/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }





}