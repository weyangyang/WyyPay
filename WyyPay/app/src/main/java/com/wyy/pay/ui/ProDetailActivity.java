package com.wyy.pay.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyy.pay.R;
import com.wyy.pay.bean.ProductBean;
import com.wyy.pay.tackphoto.TakePhoto;
import com.wyy.pay.utils.BaseOptions;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.ClearEditText;

import java.io.File;
import java.io.IOException;

import static xtcore.utils.FileUtils.chmod;


public class ProDetailActivity extends BaseActivity implements View.OnClickListener {
private ClearEditText editBarCode;//输入条码
private ClearEditText editProName;//输入商品名称
private Button btnScanBarCode;//扫码按钮
private ClearEditText editProPrice;//输入商品价格
    private TextView tvProCategory;//选择分类
    private ImageView ivProImg;//商品图片
    private Button btnProSave;//保存商品
    private ProductBean mProductBean;//商品数据bean
    private boolean isEdit = false;
    private TakePhoto mTakePhoto;
    private String SP_PRODUCT_PIC_PATH = "product_pic";
    private static String PRODUCT_PIC_NAME = "product_pic.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_product_detail);
        super.onCreate(savedInstanceState);
       // SP_PRODUCT_PIC_PATH+= 商品名称md5_6
        //PRODUCT_PIC_NAME+=商品名称md5_6
        initView();
        initData();
        initListener();

    }

    @Override
    public void initView() {
        tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
        editBarCode = (ClearEditText) findViewById(R.id.editBarCode);
        editProName = (ClearEditText) findViewById(R.id.editProName);
        btnScanBarCode = (Button) findViewById(R.id.btnScanBarCode);
        tvProCategory = (TextView) findViewById(R.id.tvProCategory);
        editProPrice = (ClearEditText) findViewById(R.id.editProPrice);
        ivProImg = (ImageView) findViewById(R.id.ivProImg);
        btnProSave = (Button) findViewById(R.id.btnProSave);
        Intent intent = getIntent();
       int from =  intent.getIntExtra(ConstantUtils.INTENT_KEY_FROM_ACTIVITY_TYPE,ConstantUtils.FROM_POPUP_WINDOW_ADD_PRODUCT);
        if(ConstantUtils.FROM_PRODUCT_MANAGE_ACTIVITY_PRODUCT_LIST_ITEM == from){
            //来自pro list item 点击
            mProductBean = (ProductBean) intent.getSerializableExtra(ConstantUtils.INTENT_KEY_PRODUCT_BEAN);
            tvNavTitle.setText("商品详情");
            tvNavRight.setVisibility(View.VISIBLE);
            tvNavRight.setText("编辑");
            setViewState(false);
            if(mProductBean!=null){
                editProName.setText(mProductBean.getProName());
                editBarCode.setText(mProductBean.getProNo());
                tvProCategory.setText(mProductBean.getCategoryName());
                editProPrice.setText(String.valueOf(mProductBean.getProPrice()));
                ImageLoader.getInstance().displayImage(mProductBean.getImgUrl(),ivProImg, BaseOptions.getInstance().getProductImgOptions());

            }

        }else if(ConstantUtils.FROM_POPUP_WINDOW_ADD_PRODUCT == from){
            //来自popupwindow add button 点击
            tvNavTitle.setText("新增商品");
            tvNavRight.setVisibility(View.GONE);
            setViewState(true);
        }
    }
    /**
     * @author liyusheng 通过照相机和图库截图并显示图片
     * @param fileName
     *            截图后保存的文件名
     *  @param spKey
     *              保存文件path在sp中的key
     * @param showView
     *            用于显示的ImageView对象
     * @param picWidth
     *            截图时的图片宽（dp)
     * @param picHeight
     *            截图时的图片高（dp)
     */
    private void showPic(final String spKey, String fileName, final ImageView showView, int picWidth, int picHeight) {
        if (TextUtils.isEmpty(fileName) && null == showView && picWidth == 0 && picHeight == 0) {
            return;
        }
        File cardPicFile = createFile(fileName);
        mTakePhoto = new TakePhoto(this, new TakePhoto.PhotoResult() {
            @Override
            public void onPhotoResult(File outputPath) {
                // String strPath = "file://" + outputPath.getAbsolutePath();
                String strPath = outputPath.getAbsolutePath();
                // Bitmap bitmap = BitmapFactory.decodeFile(strPath);
                // showView.setImageBitmap(bitmap);
                xtcore.utils.PreferenceUtils.setPrefString(ProDetailActivity.this, spKey, strPath);
                ImageLoader.getInstance().displayImage("file://" + strPath, showView,
                        BaseOptions.getInstance().getProductImgOptions());
            }
        }, cardPicFile);
        int h = Utils.dip2px(this, picHeight);
        int w = Utils.dip2px(this, picWidth);
        mTakePhoto.setOutHeight(h);
        mTakePhoto.setOutWidth(w);
        mTakePhoto.start();
    }
    /**
     * 判断sdcard是否可用
     *
     * @param
     * @return
     */
    private boolean sdCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    private File createFile(String fileName) {
        File photoFile = null;
        if (!sdCardAvailable()) {
            photoFile = new File(getFilesDir(), fileName);
        } else {
            photoFile = new File(Environment.getExternalStorageDirectory(), fileName);
        }
        try {
            photoFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chmod(photoFile.getAbsolutePath(), "777");
        return photoFile;
    }
    private void setViewState(boolean isCanInput) {
        int type ;
        if(isCanInput){
            type = InputType.TYPE_CLASS_TEXT;
            btnProSave.setVisibility(View.VISIBLE);
            btnScanBarCode.setVisibility(View.VISIBLE);
        }else {
            type = InputType.TYPE_NULL;
            btnProSave.setVisibility(View.GONE);
            btnScanBarCode.setVisibility(View.GONE);
        }
        editProPrice.setFocusable(isCanInput);
        editProPrice.setFocusableInTouchMode(isCanInput);
        isNeedRequestFocus(editProPrice,isCanInput);
        tvProCategory.setEnabled(isCanInput);
        editProName.setFocusable(isCanInput);
        editProName.setFocusableInTouchMode(isCanInput);
        isNeedRequestFocus(editProName,isCanInput);
        editBarCode.setFocusable(isCanInput);
        editBarCode.setFocusableInTouchMode(isCanInput);
        isNeedRequestFocus(editBarCode,isCanInput);
        ivProImg.setEnabled(isCanInput);
//        editBarCode.setInputType(type);
//        editProPrice.setInputType(type);
//        editProName.setInputType(type);
    }
    private void isNeedRequestFocus(EditText editText , boolean isNeed){
        if(isNeed){
            editText.requestFocus();
        }
    }
    private void hideSoftInputFromWindow() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editBarCode.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editProPrice.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editProName.getWindowToken(), 0);
    }
    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        tvNavLeft.setOnClickListener(this);
        btnProSave.setOnClickListener(this);
        ivProImg.setOnClickListener(this);
        tvProCategory.setOnClickListener(this);
        btnScanBarCode.setOnClickListener(this);
        tvNavRight.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mTakePhoto != null) {
            mTakePhoto.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvNavLeft://返回
                ProDetailActivity.this.finish();
                break;
            case R.id.btnProSave://保存商品
                //更新数据库
                ProDetailActivity.this.finish();
                break;
            case R.id.ivProImg: //设置商品图片
                String proName = editProName.getText().toString().trim();
                if(TextUtils.isEmpty(proName)){
                    Toast.makeText(ProDetailActivity.this,"商品名称不能为空，请先输入商品名称！",Toast.LENGTH_SHORT).show();
                    return;
                }
                String tempMd5 = Utils.get6MD5WithString(proName);
                showPic(SP_PRODUCT_PIC_PATH+tempMd5,tempMd5+PRODUCT_PIC_NAME, ivProImg, 200, 200);
                break;
            case R.id.tvProCategory: //选择分类
                hideSoftInputFromWindow();
                break;
            case R.id.btnScanBarCode: //扫码获取条码
                break;
            case R.id.tvNavRight: //编辑
                isEdit =!isEdit;
                if(isEdit){
                    tvNavRight.setText("完成");
                    setViewState(true);
                }else {
                    tvNavRight.setText("编辑");
                    setViewState(false);
                    hideSoftInputFromWindow();

                }
                break;
        }
    }
}
