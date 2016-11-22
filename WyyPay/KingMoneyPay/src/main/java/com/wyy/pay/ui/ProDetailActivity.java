package com.wyy.pay.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.wyy.pay.bean.TableGoodsDetailBean;
import com.wyy.pay.tackphoto.TakePhoto;
import com.wyy.pay.utils.BaseOptions;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.ClearEditText;

import java.io.File;
import java.io.IOException;
import java.util.List;

import xtcore.utils.PreferenceUtils;

import static xtcore.utils.FileUtils.chmod;


public class ProDetailActivity extends BaseActivity implements View.OnClickListener {
private ClearEditText editBarCode;//输入条码
private ClearEditText editProName;//输入商品名称
private Button btnScanBarCode;//扫码按钮
private ClearEditText editProPrice;//输入商品价格
private ClearEditText editProStock;//输入商品库存数量
    private TextView tvProCategory;//选择分类
    private ImageView ivProImg;//商品图片
    private Button btnProSave;//保存商品
    private TableGoodsDetailBean mTableGoodsDetailBean;//商品数据bean
    private boolean isEdit = false;
    private TakePhoto mTakePhoto;
    private String SP_PRODUCT_PIC_PATH = "product_pic";
    private static String PRODUCT_PIC_NAME = "product_pic.jpg";
    private  int fromPage;//来自哪个界面
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

    /**
     * 上传用户实名认证资料.
     *
    public void postUserVerify(HttpHeader header, UserVerifyRequestBean bean, NetReqCallBack mNetReqCallBack) {
        ParameterList paramsList = setHeaderParameter(header);
        paramsList.add(new ParameterList.StringParameter("id_category", bean.getIdCategory()));
        paramsList.add(new ParameterList.StringParameter("id_number", bean.getIdNumber()));
        paramsList.add(new ParameterList.StringParameter("real_name", bean.getRealName()));
        paramsList.add(new ParameterList.StringParameter("english_name", bean.getEnName()));
        paramsList.add(new ParameterList.StringParameter("face_image", ZipUtils.base64File(bean.getFileFace()
                .getAbsolutePath())));
        paramsList.add(new ParameterList.StringParameter("photo_id_image", ZipUtils.base64File(bean.getFileID()
                .getAbsolutePath())));
        super.post(Urls.BASE_URL + Urls.USER_VERIFY,paramsList, mNetReqCallBack);



    private void postUserVerify(String idPhotoPath, String userPhotoPath) {
    if (!SystemUtils.checkAllNet(this)) {
    DefaultToast.makeText(this, R.string.net_error, Toast.LENGTH_SHORT).show();
    return;
    }
    UserVerifyRequestBean requestBean = new UserVerifyRequestBean();
    requestBean.setRealName(tvNameInput.getText().toString().trim());
    requestBean.setEnName(tvEnNameInput.getText().toString().trim());
    requestBean.setIdNumber(edtInputIdnum.getText().toString().trim());
    requestBean.setIdCategory(strCardType);
    requestBean.setFileFace(new File(userPhotoPath));
    requestBean.setFileID(new File(idPhotoPath));
    if (dialog == null) {
    dialog = CustomProgressDialog.createLoadingDialog(this, getString(R.string.uploading_verify_data), false);
    }
    ExternalFactory
    .getInstance()
    .createUserVerify()
    .postVerifyStatus(UserUtils.getAccessTokenHeader(), dialog, requestBean,
    new AbsGetUserVerifyStatusData() {

    @Override
    public void getSuccData(UserVerifyStatusBean bean, String strUrl) {
    TableUser user = new TableUser();
    user.setUserID(UserUtils.getUid());
    user.setUnionKey();
    user.setVerifiStatus(bean.getStrVerifyStatus());
    user.insert(true, TableUser.COLUMN_UNION_KEY, user.unionKey);
    EventBus.getDefault().post(bean);
    runOnUiThread(new Runnable() {
    @Override
    public void run() {
    DefaultToast.makeText(VerifyActivity.this,
    getString(R.string.upload_verify_success), Toast.LENGTH_SHORT).show();
    finish();
    }
    });
    }

    }
     */
    @Override
    public void initView() {
        tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
        editBarCode = (ClearEditText) findViewById(R.id.editBarCode);
        editProName = (ClearEditText) findViewById(R.id.editProName);
        editProStock = (ClearEditText) findViewById(R.id.editProStock);
        btnScanBarCode = (Button) findViewById(R.id.btnScanBarCode);
        tvProCategory = (TextView) findViewById(R.id.tvProCategory);
        editProPrice = (ClearEditText) findViewById(R.id.editProPrice);
        ivProImg = (ImageView) findViewById(R.id.ivProImg);
        btnProSave = (Button) findViewById(R.id.btnProSave);
        Intent intent = getIntent();
        fromPage =  intent.getIntExtra(ConstantUtils.INTENT_KEY_FROM_ACTIVITY_TYPE,ConstantUtils.FROM_POPUP_WINDOW_ADD_PRODUCT);
        if(ConstantUtils.FROM_PRODUCT_MANAGE_ACTIVITY_PRODUCT_LIST_ITEM == fromPage){
            //来自pro list item 点击
            mTableGoodsDetailBean = (TableGoodsDetailBean) intent.getSerializableExtra(ConstantUtils.INTENT_KEY_PRODUCT_BEAN);
            tvNavTitle.setText("商品详情");
            tvNavRight.setVisibility(View.VISIBLE);
            tvNavRight.setText("编辑");
            setViewState(false);
            if(mTableGoodsDetailBean !=null){
                editProName.setText(mTableGoodsDetailBean.getGoodsName());
                editBarCode.setText(mTableGoodsDetailBean.getGoodsBarcode());
                editProStock.setText(String.valueOf(mTableGoodsDetailBean.getGoodsStockCount()));
                tvProCategory.setText(mTableGoodsDetailBean.getGoodsCName());
                editProPrice.setText(String.valueOf(mTableGoodsDetailBean.getGoodsPrice()));
                String imgPath = xtcore.utils.PreferenceUtils.getPrefString(this, SP_PRODUCT_PIC_PATH+Utils.get6MD5WithString(mTableGoodsDetailBean.getGoodsName()), "");
                if(TextUtils.isEmpty(imgPath)||imgPath.contains("http:")){
                    ImageLoader.getInstance().displayImage(mTableGoodsDetailBean.getGoodsImgUrl(),ivProImg, BaseOptions.getInstance().getProductClipImgOptions());
                }else {
                    ImageLoader.getInstance().displayImage(String.format("file://%s",imgPath),ivProImg, BaseOptions.getInstance().getProductClipImgOptions());
                }
            }

        }else if(ConstantUtils.FROM_POPUP_WINDOW_ADD_PRODUCT == fromPage){
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
                 String strPath = "file://" + outputPath.getAbsolutePath();
//                String strPath = outputPath.getAbsolutePath();
//                 Bitmap bitmap = BitmapFactory.decodeFile(strPath);
//                 showView.setImageBitmap(bitmap);
                ImageLoader.getInstance().displayImage(strPath,showView,BaseOptions.getInstance().getProductClipImgOptions());

                xtcore.utils.PreferenceUtils.setPrefString(ProDetailActivity.this, spKey, strPath);
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
    private static final int TO_SCANPAYACTIVITY_REQUEST_CODE =99;
    private static final int TO_PRO_CATEGORY_ACTIVITY_REQUEST_CODE =98;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK){
            return;
        }
        switch (requestCode){
            case TO_SCANPAYACTIVITY_REQUEST_CODE:
                String proNo = data.getStringExtra(ConstantUtils.INTENT_KEY_PRODUCT_NO);
                editBarCode.setText(proNo);
                return;
            case TO_PRO_CATEGORY_ACTIVITY_REQUEST_CODE:
                String categoryName = data.getStringExtra(ConstantUtils.INTENT_KEY_PRODUCT_CATEGORY);
                tvProCategory.setText(categoryName);
                return;
        }

        if (mTakePhoto != null) {
            mTakePhoto.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        Intent  intent= null;
        switch (v.getId()){
            case R.id.tvNavLeft://返回
                ProDetailActivity.this.finish();
                break;
            case R.id.btnProSave://保存商品
                //更新数据
                saveData2DB();

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
                  intent = new Intent(ProDetailActivity.this, ProCategoryActivity.class);
                intent.putExtra(ConstantUtils.INTENT_KEY_FROM_ACTIVITY_TYPE,ConstantUtils.FROM_PRO_DETAIL_ACTIVITY);
                startActivityForResult(intent,TO_PRO_CATEGORY_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.btnScanBarCode: //扫码获取条码
                hideSoftInputFromWindow();
                intent = new Intent(ProDetailActivity.this, ScanPayActivity.class);
                intent.putExtra(ConstantUtils.INTENT_KEY_PAY_TYPE, ConstantUtils.PAY_TYPE_SCAN_PRO_FOR_BARCODE);
                startActivityForResult(intent,TO_SCANPAYACTIVITY_REQUEST_CODE);
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

    private void saveData2DB() {
        String barCode = editBarCode.getText().toString().trim();
        if(TextUtils.isEmpty(barCode)){
            Toast.makeText(ProDetailActivity.this,"商品条码信息不能为空，请重新输入！！",Toast.LENGTH_SHORT).show();
            return;
        }else {
            if(fromPage ==ConstantUtils.FROM_POPUP_WINDOW_ADD_PRODUCT){
                TableGoodsDetailBean bean = new TableGoodsDetailBean();
                List  goodsList = bean.query(null,TableGoodsDetailBean.COLUMN_USER_ID +"=? AND "+TableGoodsDetailBean.COLUMN_GOODS_BARCODE+" =?",new String[]{Utils.get6MD5WithString("18501053570"),barCode},null,null,null);
                if(goodsList!=null && goodsList.size()>0){
                    Toast.makeText(ProDetailActivity.this,"您要添加的商品已存在，请重新输入！",Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        }
        String goodsName = editProName.getText().toString().trim();
        if(TextUtils.isEmpty(goodsName)){
            Toast.makeText(ProDetailActivity.this,"商品名称不能为空，请重新输入！",Toast.LENGTH_SHORT).show();
            return;
        }
        String goodsId = Utils.get6MD5WithString(goodsName);
        String goodsCName = tvProCategory.getText().toString().trim();
        String goodsCid = Utils.get6MD5WithString(goodsCName);
        String  price = editProPrice.getText().toString().trim();
        if(TextUtils.isEmpty(price)||!Utils.isZfNumber(price)){
            Toast.makeText(ProDetailActivity.this,"商品价格不能为空，请重新输入！",Toast.LENGTH_SHORT).show();
            return;
        }
        double goodsPrice = Double.parseDouble(price);
        String  proStockCount = editProStock.getText().toString().trim();
        if(TextUtils.isEmpty(price)||!Utils.isZhengNumber(proStockCount)){
            Toast.makeText(ProDetailActivity.this,"商品库存不能为0，请重新输入！",Toast.LENGTH_SHORT).show();
            return;
        }
        int stockCount = Integer.parseInt(proStockCount);
        String spKey = SP_PRODUCT_PIC_PATH +Utils.get6MD5WithString(goodsName);
        String imgUrlPath = PreferenceUtils.getPrefString(ProDetailActivity.this,spKey,"");
        Long createTime = System.currentTimeMillis();
        TableGoodsDetailBean bean = new TableGoodsDetailBean();
        bean.setUserId(Utils.get6MD5WithString("18501053570"));
        bean.setGoodsBarcode(barCode);
        bean.setGoodsId(goodsId);
        bean.setGoodsName(goodsName);
        bean.setGoodsCid(goodsCid);
        bean.setGoodsCName(goodsCName);
        bean.setGoodsCreateTime(createTime);
        bean.setGoodsImgUrl(imgUrlPath);
        bean.setGoodsPrice(goodsPrice);
        bean.setGoodsStockCount(stockCount);
        boolean isSucc = bean.insert(true,TableGoodsDetailBean.COLUMN_GOODS_ID,goodsId);
        if(isSucc){
            if(fromPage ==ConstantUtils.FROM_POPUP_WINDOW_ADD_PRODUCT){
                    //清空数据
                    editBarCode.setText("");
                    editProName.setText("");
                    tvProCategory.setText("默认分类");
                    editProPrice.setText("");
                    editProStock.setText("");
                showPic(SP_PRODUCT_PIC_PATH,"dd", ivProImg, 200, 200);
                Toast.makeText(ProDetailActivity.this,"保存商品数据成功!",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(ProDetailActivity.this,"保存商品数据成功!",Toast.LENGTH_SHORT).show();
                ProDetailActivity.this.finish();
            }
        }else {
            Toast.makeText(ProDetailActivity.this,"保存商品数据失败!",Toast.LENGTH_SHORT).show();
        }

    }
}
/**
 * case R.id.btnFinish:// 点击完成，提交参数到服务器
 addLogBean(true);
 if (TextUtils.isEmpty(tvNameInput.getText().toString().trim())) {
 // 没有输入真实姓名
 Toast.makeText(VerifyActivity.this, "请输入真实姓名！", Toast.LENGTH_SHORT).show();
 return;
 } else if (TextUtils.isEmpty(tvEnNameInput.getText().toString().trim())) {
 Toast.makeText(VerifyActivity.this, "请输入英文名！", Toast.LENGTH_SHORT).show();
 return;
 } else {

 String idcardNum = edtInputIdnum.getText().toString().trim();

 if (!TextUtils.isEmpty(idcardNum)) {
 if (rgChangeCard.getCheckedRadioButtonId() == R.id.rbIdcard && idcardNum.length() != 18) {
 DefaultToast.makeText(VerifyActivity.this, "您的身份证件号码长度不合法", Toast.LENGTH_SHORT).show();
 return;
 }
 // 所有输入合法
 // 检查照片
 String idcardPicPath = xtcore.utils.PreferenceUtils.getPrefString(this, SP_IDCARD_PIC_PATH, "");
 String userPIcPath = xtcore.utils.PreferenceUtils.getPrefString(this, SP_USER_PIC_PATH, "");
 if (checkPhotoFile(idcardPicPath, getString(R.string.id_photo_not_exist))
 && checkPhotoFile(userPIcPath, getString(R.string.user_photo_not_exist))) {
 // 照片存在
 postUserVerify(idcardPicPath, userPIcPath);
 }

 } else {
 DefaultToast.makeText(VerifyActivity.this, "请输入证件号码！", Toast.LENGTH_SHORT).show();
 return;
 }
 }
 break;
 */