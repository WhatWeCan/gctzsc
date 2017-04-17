package com.hdl.gctzsc.activity.publish;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hdl.gctzsc.R;
import com.hdl.gctzsc.customview.ImageTools;
import com.hdl.gctzsc.customview.ToastUtils;
import com.hdl.gctzsc.utils.SysApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyCamera extends Activity {


    private CameraView cv = null;// 继承surfaceView的自定义view 用于存放照相的图片
    private Camera camera; // 声明照相机
    private Button cameraFolder;// 文件夹
    private Button cameraPhoto; // 照相
    private Button cameraOk; // 确认
    private Button cameraExit; // 关闭
    private LinearLayout linearLayout;//用于存放
    private LinearLayout linearLayoutCamera;
    private String cameraPath;

    private ArrayList<String> listPath = new ArrayList<String>();// 存放路径的list
    private int tag = 0; // 删除按钮tag值，从0开始
    private Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean b, Camera camera) {
            if (b) {
            } else {
            }
        }
    };
    // 回调用的picture，实现里边的onPictureTaken方法，其中byte[]数组即为照相后获取到的图片信息
    private Camera.PictureCallback picture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String timer = format.format(date);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(Environment.getExternalStorageDirectory(), "tzsc/images");
                //不存在就创建
                if (!dir.exists()) {
//                    dir.mkdir();
                    dir.mkdirs();
                }
                File img_file = new File(dir, "IMG_" + timer + ".jpg");
                Log.e("mylog", img_file.getPath());
                FileOutputStream fos = null;
                try {
                    cameraPath = img_file.getPath();
                    fos = new FileOutputStream(cameraPath);// 获得文件输出流
                    fos.write(data);// 写入文件
                    openCamera();// 重新打开相机
                    getImageView(cameraPath);// 获取照片
                } catch (Exception e) {
                    closeCamera();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                ToastUtils.showInfo(MyCamera.this, "SD卡加载失败了");
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        Toast.makeText(this, "请横屏拍照(效果最佳),点击屏幕可对焦", Toast.LENGTH_LONG).show();
        initView();
        setListener();
        // 打开相机
        openCamera();
    }

    /**
     * 设置自动对焦
     */
    private void setAutoFoucs() {
        cv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                camera.autoFocus(mAutoFocusCallback);
                return false;
            }
        });
    }

    /**
     * 设置监听器
     */
    private void setListener() {

        // 点击确定之后调用
        cameraOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listPath.size() > 0) {
                    Intent it = null;
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("listPath", listPath);
                    if (getIntent().getIntExtra("requestCode", 0) == 10) {
                        it = new Intent(MyCamera.this, PublishNewGoodsActivity.class);
                        it.putExtras(bundle);
                        MyCamera.this.setResult(11, it);
                    } else if (getIntent().getIntExtra("requestCode", 0) == 20) {
                        it = new Intent(MyCamera.this, PublishNewGoodsActivity.class);
                        it.putExtras(bundle);
                        MyCamera.this.setResult(22, it);
                    } else if (getIntent().getIntExtra("requestCode", 0) == 30) {
                        it = new Intent(MyCamera.this, PublishPaiGoodsActivity.class);
                        it.putExtras(bundle);
                        MyCamera.this.setResult(33, it);
                    } else if (getIntent().getIntExtra("requestCode", 0) == 40) {
                        it = new Intent(MyCamera.this, PublishPaiGoodsActivity.class);
                        it.putExtras(bundle);
                        MyCamera.this.setResult(44, it);
                    } else if (getIntent().getIntExtra("requestCode", 0) == 50) {
                        it = new Intent(MyCamera.this, PublishFeelGoodsActivity.class);
                        it.putExtras(bundle);
                        MyCamera.this.setResult(55, it);
                    } else if (getIntent().getIntExtra("requestCode", 0) == 60) {
                        it = new Intent(MyCamera.this, PublishFeelGoodsActivity.class);
                        it.putExtras(bundle);
                        MyCamera.this.setResult(66, it);
                    }
                    finish();
                } else {
                    ToastUtils.showInfo(MyCamera.this, "没有拍照,或选择任何图片");
                }
            }
        });
        //退出的时候 将拍过的照片删除
        cameraExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyCamera.this);
                builder.setTitle("取消拍照");
                builder.setMessage("您确定要取消拍照吗?");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (String path : listPath) {
                            removeImg(path);
                        }
                        closeCamera();//关闭照相机
                        finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        cameraPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, picture);
            }
        });
        cameraFolder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开系统图片浏览器
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
    }

    /**
     * 初始化布局
     */
    private void initView() {
        setContentView(R.layout.custom_camera);
        linearLayout = (LinearLayout) findViewById(R.id.ll_custom_camera_images_container);
        linearLayoutCamera = (LinearLayout) findViewById(R.id.ll_custom_camera_preciew);
        cameraFolder = (Button) findViewById(R.id.btn_custom_camera_folder);
        cameraPhoto = (Button) findViewById(R.id.btn_custom_camera_photo);
        cameraOk = (Button) findViewById(R.id.btn_custom_camera_ok);
        cameraExit = (Button) findViewById(R.id.btn_custom_camera_exit);
    }

    /**
     * 获取图片并预览
     *
     * @param path
     */
    private void getImageView(final String path) {
        int flag = tag++;
        final View img_item = getLayoutInflater().inflate(R.layout.custom_camera_imgitem, null);
        final ImageView imageView = (ImageView) img_item.findViewById(R.id.iv_custom_camera_item_imgs);
        final Button delete_img = (Button) img_item.findViewById(R.id.photoshare_item_delete);
        try {
            imageView.setImageBitmap(getImageBitmap(path));
            delete_img.setTag(flag);// 给按钮设置一个tag，主要为listPath的下标所用
            imageView.setTag(flag);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int key = Integer.parseInt(imageView.getTag().toString());
                String path = listPath.get(key);
                previewImg(path);
            }
        });
        delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                linearLayout.removeView(img_item);
                int k = Integer.parseInt(delete_img.getTag().toString());
                removeImg(path);
//                listPath.remove(k);
                listPath.set(k, "deleted");
            }
        });

        linearLayout.addView(img_item);
        listPath.add(path);
    }

    /**
     * 删除图片
     *
     * @param path
     */
    private void removeImg(String path) {
        File fDelete = new File(path);
        if (fDelete.exists()) {
            //只有这个目录下的文件才删除哦
            if (path.contains("tzsc/images")) {
                fDelete.delete();
            }
        }
    }

    /**
     * 预览图片
     *
     * @param path
     */
    private void previewImg(String path) {
        Dialog dialog = new Dialog(MyCamera.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ImageView iv = new ImageView(MyCamera.this);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        iv.setImageBitmap(bitmap);
        dialog.setContentView(iv);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//设置背景为全透明
        dialog.show();
    }

    // 根据路径获取图片
    private Bitmap getImageBitmap(String path) throws FileNotFoundException,
            IOException {
        Bitmap bmp = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        opts.inSampleSize = ImageTools.computeSampleSize(opts, -1, 150 * 150);// 得到缩略图
        opts.inJustDecodeBounds = false;
        try {
            bmp = BitmapFactory.decodeFile(path, opts);
        } catch (OutOfMemoryError e) {
        }
        return bmp;
    }

    // 主要的surfaceView，负责展示预览图片，camera的开关
    class CameraView extends SurfaceView {

        private SurfaceHolder holder = null;

        public CameraView(Context context) {
            super(context);
            holder = this.getHolder();
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            holder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceChanged(SurfaceHolder holder, int format,
                                           int width, int height) {
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setPictureSize(1024, 768);
                    parameters.setPreviewSize(800, 480);
                    camera.setParameters(parameters);
                    camera.startPreview();
                }

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    camera = Camera.open();
                    try {
                        // 设置camera预览的角度，因为默认图片是倾斜90度的
                        camera.setDisplayOrientation(90);
                        // 设置holder主要是用于surfaceView的图片的实时预览，以及获取图片等功能
                        camera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        closeCamera();
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    if (camera != null) {
                        camera.stopPreview();
                    }
                    closeCamera();
                }
            });
        }
    }

    /**
     * 页面销毁的时候调用
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeCamera();
    }

    /**
     * 关闭照相机
     */
    private void closeCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    /**
     * 打开照相机
     */
    private void openCamera() {
        linearLayoutCamera.removeAllViews();
        cv = new CameraView(MyCamera.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        linearLayoutCamera.addView(cv, params);
        setAutoFoucs();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                getImageView(ImageTools.getAbsoluteImagePath(data.getData(), MyCamera.this));
            }
        }
    }
}
