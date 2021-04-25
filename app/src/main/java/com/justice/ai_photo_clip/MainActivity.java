package com.justice.ai_photo_clip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.justice.ai_photo_clip.utils.BitmapUtils;
import com.justice.ai_photo_clip.utils.FileUtils;
import com.justice.ai_photo_clip.utils.L;
import com.justice.ai_photo_clip.utils.LoadingUtil;
import com.justice.ai_photo_clip.utils.MessageUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity implements FiltersListFragment.FiltersListFragmentListener, EditImageFragment.EditImageFragmentListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String IMAGE_NAME = "dog.jpg";

    public static final int SELECT_GALLERY_IMAGE = 101;
    public static int OPEN_IMAGE_REQUEST_CODE = 49018;

    @BindView(R.id.image_preview)
    ImageView imagePreview;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.iv_camera)
    ImageButton ivCameraButton;

    Bitmap originalImage;
    // to backup image with filter applied (필터를 적용하여 이미지를 백업하려면)
    Bitmap filteredImage;

    // the final image after applying (적용 후 최종 영상)
    // brightness, saturation, contrast (밝기, 채도, 대비)
    Bitmap finalImage;

    FiltersListFragment filtersListFragment;
    EditImageFragment editImageFragment;

    private Uri mCameraUri = null;
    // modified image values (수정된 영상 값)
    int brightnessFinal = 0;
    float saturationFinal = 1.0f;
    float contrastFinal = 1.0f;

    private LankManager mLankManager;

    // load native image filters library (기본 이미지 필터 라이브러리 로드)
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        L.i("[MainActivity]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.activity_title_main));

        mLankManager = new LankManager();

        loadImage();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // adding filter list fragment (필터 목록 조각 추가)
        filtersListFragment = new FiltersListFragment();
        filtersListFragment.setListener(this);

        // adding edit image fragment (이미지 편집 조각 추가)
        editImageFragment = new EditImageFragment();
        editImageFragment.setListener(this);

        adapter.addFragment(filtersListFragment, getString(R.string.tab_filters));
        adapter.addFragment(editImageFragment, getString(R.string.tab_edit));

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFilterSelected(Filter filter) {
        // reset image controls (이미지 컨트롤 재설정)

        L.i(":::::필터 이미지 클릭.... " + filter.getName());
        resetControls();

        // applying the selected filter (선택한 필터 적용)
        filteredImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
        // preview filtered image (필터링된 이미지 미리 보기)
        imagePreview.setImageBitmap(filter.processFilter(filteredImage));

        finalImage = filteredImage.copy(Bitmap.Config.ARGB_8888, true);
    }

    @Override
    public void onBrightnessChanged(final int brightness) {
        brightnessFinal = brightness;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        imagePreview.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onSaturationChanged(final float saturation) {
        saturationFinal = saturation;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        imagePreview.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onContrastChanged(final float contrast) {
        contrastFinal = contrast;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(contrast));
        imagePreview.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
        // once the editing is done i.e seekbar is drag is completed, (일단 편집이 완료되면, 즉, 탐색 막대가 드래그 완료되고,)
        // apply the values on to filtered image (값을 필터링된 이미지에 적용)
        final Bitmap bitmap = filteredImage.copy(Bitmap.Config.ARGB_8888, true);

        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        myFilter.addSubFilter(new ContrastSubFilter(contrastFinal));
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));
        finalImage = myFilter.processFilter(bitmap);
    }

    /**
     * Resets image edit controls to normal when new filter
     * is selected
     */
    private void resetControls() {
        if (editImageFragment != null) {
            editImageFragment.resetControls();
        }
        brightnessFinal = 0;
        saturationFinal = 1.0f;
        contrastFinal = 1.0f;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    // load the default image from assets on app launch (앱 실행 시 자산에서 기본 이미지 로드)
    private void loadImage() {
        originalImage = BitmapUtils.getBitmapFromAssets(this, IMAGE_NAME, 300, 300);
        filteredImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
        finalImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
        imagePreview.setImageBitmap(originalImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_open) {
            openImageFromGallery();
            return true;
        }

        if (id == R.id.action_save) {
            saveImageToGallery();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override //사진찍는부분
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.i("::::::resultCode " + resultCode + " requestCode : " + requestCode);
        if (resultCode == RESULT_OK && requestCode == SELECT_GALLERY_IMAGE) {
            Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);

            // clear bitmap memory (비트맵 메모리 지우기)
            originalImage.recycle();
            finalImage.recycle();
            finalImage.recycle();

            originalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            filteredImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
            finalImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
            imagePreview.setImageBitmap(originalImage);
            bitmap.recycle();

            // render selected image thumbnails (선택한 이미지 미리 보기 렌더링)
            filtersListFragment.prepareThumbnail(originalImage);
            String convertUrl = FileUtils.getFilePathFromURI(this, data.getData());
            L.i("::convertUrl : " + convertUrl);
            sendToImage(convertUrl);
        } else if (resultCode == RESULT_OK && requestCode == OPEN_IMAGE_REQUEST_CODE) {
            L.i("resultCode " + resultCode + " requestCode : " + requestCode);
            Uri uri = null;

            if (mCameraUri != null) {


                uri = mCameraUri;
                L.i("::::uri : " + FileUtils.getFilePathFromURI(this, uri));

                String convertUrl = FileUtils.getFilePathFromURI(this, uri);

                Bitmap bitmap = BitmapUtils.getBitmapFromCamera(this, convertUrl, 800, 800);

                L.i(":::bitmap " + bitmap);

                // clear bitmap memory (비트맵 메모리 지우기)
                originalImage.recycle();
                finalImage.recycle();
                finalImage.recycle();

                originalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                filteredImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
                finalImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
                imagePreview.setImageBitmap(originalImage);
                bitmap.recycle();

                // render selected image thumbnails (선택한 이미지 미리 보기 렌더링)
                filtersListFragment.prepareThumbnail(originalImage);

                sendToImage(convertUrl);

                ivCameraButton.setBackgroundResource(R.drawable.ic_camera_off);
            }


        }
    }

    private void sendToImage(String path) { //네이버 API로 날리는부분
        L.d("sendToImage " + path);
        //서버로 전달 하기위한 로직.
        LoadingUtil.showLoadingBar(MainActivity.this);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File imageFile = new File(path);
        Uri uris = Uri.fromFile(imageFile);
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uris.toString());
        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        String imageName = imageFile.getName();

        builder.addFormDataPart("image", imageName, RequestBody.create(MediaType.parse("image/jpeg"), imageFile));


        RequestBody requestBody = builder.build();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient imageUploadClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        Map<String, String> header = new HashMap<>();

        header.put("X-NCP-APIGW-API-KEY-ID", "zbfr7hhqwz");
        header.put("X-NCP-APIGW-API-KEY", "5VfwgmbOLysme9d4dc3sk3wSbHFF5eRqGak0NiSl");
        header.put("Content-Type", "multipart/form-data");
        Headers headerbuild = Headers.of(header);
        Request request = new Request.Builder()
                .headers(headerbuild)
                .url("https://naveropenapi.apigw.ntruss.com/vision-obj/v1/detect")
                .method("POST", requestBody)
                .build();


        imageUploadClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LoadingUtil.hideLoadingBar();
                L.e("error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LoadingUtil.hideLoadingBar();
                String responseData = response.body().string();
                L.e("onResponse: " + responseData);
                try {
                    if (response.code() == 200) {
                        List<String> list = new ArrayList<>();
                        JsonParser parser = new JsonParser();
                        Object obj = parser.parse(responseData);
                        JsonObject jsonObj = (JsonObject) obj;

                        //predictions array 파싱
                        JsonArray array = jsonObj.getAsJsonArray("predictions");

                        //파싱한 predictions 값 내부의 사물 인식값인 detection_names을 확인하도록한다.
                        for (int i = 0; i < array.size(); i++) {
                            L.i("::::array " + array.get(i).getAsJsonObject());
                            JsonObject asJsonObject = array.get(i).getAsJsonObject();
                            JsonArray detection_names = asJsonObject.getAsJsonArray("detection_names");

                            for (JsonElement item : detection_names) {
                                //detection_names을 항목.
                                list.add(item.getAsString());
                            }
                        }

                        setFilter(list);


                    }
                } catch (Exception e) {
                    L.i(":::E : " + e.getMessage());
                }


            }
        });
    }

    private void setFilter(List<String> list) {
        if (list.size() > 0) {
            Observable.fromIterable(list)
                    .distinct()
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        L.i("::::response : " + response.size());
                        int important = -1;
                        String key = null;
                        for (String item : response) {
                            if (mLankManager.getCoCoMap().containsKey(item)) {
                                if (important == -1) {
                                    //우선순위 최초설정.
                                    key = item;
                                    important = mLankManager.getCoCoMap().get(key).getImportant();
                                } else {
                                    if (mLankManager.getCoCoMap().get(item).getImportant() < important) {
                                        //더 우선순위..
                                        key = item;
                                        important = mLankManager.getCoCoMap().get(key).getImportant();
                                    }

                                }

                            }
                        }

                        if (key == null) {
                            MessageUtils.showLongToastMsg(getApplicationContext(), "우선순위 값을 찾을수 없습니다.");
                            return;
                        }


                        L.i(":::우선순위 Key 값 : " + key);
                        String keyName = mLankManager.getCoCoMap().get(key).getName();
                        int filter = mLankManager.getCoCoMap().get(key).getFilter();
                        MessageUtils.showLongToastMsg(getApplicationContext(), keyName + " 가 인식되었습니다.");

                        L.i(":::필터:: " + filter);
                        if (filter != -1) {
                            List<ThumbnailItem> ThumbnailItemList = filtersListFragment.getThumbnailItemList();
                            filtersListFragment.onFilterSelected(ThumbnailItemList.get(filter).filter);
                        }

                    }, error -> {
                        L.i(":::::error : " + error);
                    });
        }

    }

    private void openImageFromGallery() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, SELECT_GALLERY_IMAGE);
                        } else {
                            Toast.makeText(getApplicationContext(), "Permissions are not granted!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    /*
     * saves image to camera gallery (이미지를 카메라 갤러리에 저장)
     * */
    private void saveImageToGallery() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            final String path = BitmapUtils.insertImage(getContentResolver(), finalImage, System.currentTimeMillis() + "_profile.jpg", null);
                            if (!TextUtils.isEmpty(path)) {
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Image saved to gallery!", Snackbar.LENGTH_LONG)
                                        .setAction("OPEN", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                openImage(path);
                                            }
                                        });

                                snackbar.show();
                            } else {
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Unable to save image!", Snackbar.LENGTH_LONG);

                                snackbar.show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Permissions are not granted!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    // opening image in default image viewer app (기본 이미지 뷰어 앱에서 이미지 열기)
    private void openImage(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path), "image/*");
        startActivity(intent);
    }

    @OnClick(R.id.iv_camera)
    void clickedCamera() {
        getCameraIntent();
    }

    private void getCameraIntent() {
        Dexter.withActivity(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            String folderName = "Filters";// 폴더명
                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName;
                            File fileFolderPath = new File(path);
                            if (!fileFolderPath.exists()) {
                                if (false == fileFolderPath.mkdir()) {
                                    L.d("[getCameraIntent] mkdir fail.");
                                    return;
                                }
                            }

                            String url = "instagram_" + String.valueOf(System.currentTimeMillis()) + ".jpg";

                            Uri outputFileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", new File(path, url));

                            List<ResolveInfo> resolvedIntentActivities = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                            for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                                String packageName = resolvedIntentInfo.activityInfo.packageName;
                                grantUriPermission(packageName, outputFileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                            mCameraUri = outputFileUri;
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                            startActivityForResult(intent, OPEN_IMAGE_REQUEST_CODE);

                        } else {
                            Toast.makeText(getApplicationContext(), "Permissions are not granted!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

}
