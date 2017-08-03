package com.dfv.dfvscan.zxing;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dfv.dfvscan.R;
import com.dfv.dfvscan.Utils.LocalLog;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 车机编码扫描
 * 定制化显示扫描界面
 */
public class DvcScanActivity extends FragmentActivity {


    @InjectView(R.id.lv)
    ListView lv;
    private CaptureFragment captureFragment;
    /**
     * 选择系统图片Request Code
     */
    public static final int REQUEST_IMAGE = 112;
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;

    /**
     * 请求CAMERA权限码
     */
    public static final int REQUEST_CAMERA_PERM = 101;


//    ArrayList<CodeList> datas;
//    OrderCodeAdapter adapter;
//    String stateCode;
//    int delete_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.inject(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

//        datas = new ArrayList<>();


    }

    public static boolean isOpen = false;

    @OnClick({R.id.btn_cancel_scan, R.id.linear1,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel_scan:
                finish();
                break;
            case R.id.linear1:
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                }
                break;


        }
    }

//    Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 22:
//                    delete_position = msg.arg1;
//                    datas.remove(delete_position);
//                    adapter.notifyDataSetChanged();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            DvcScanActivity.this.setResult(RESULT_OK, resultIntent);

//            showToast("扫描结果是+" + result);
//            CodeList list = new CodeList(result);
//            datas.add(list);
//            for (int i = 0; i < datas.size() - 1; i++) {
//                for (int j = datas.size() - 1; j > i; j--) {
//                    if (datas.get(j).getCode_number().equals(datas.get(i).getCode_number())) {
//                        datas.remove(j);
//                    }
//                }
//            }
//            adapter = new OrderCodeAdapter(SecondActivity.this, datas, handler);
//            lv.setAdapter(adapter);

            if (result.length() == 13) {
                getData(result);
            } else {
                Toast.makeText(DvcScanActivity.this, "编码格式不对", Toast.LENGTH_SHORT).show();
                captureFragment.restartPreviewAndDecode();
            }

        }


        private void getData(String result) {
            StringBuffer sb = new StringBuffer();
            String info = "";
            String work = result.charAt(0) + "";//工厂
            String year = result.charAt(1) + "";//年份
            String week = result.substring(2, 4);//生产周
            String devType = result.charAt(4) + "";//产品种类
            String carModel = result.substring(5, 7);//对应机型
            String number = result.substring(7, 11);//流水号
            String devModel = result.charAt(11) + "";//车机平台
            String rank = result.charAt(12) + "";//车机型号等级

            if (work.equals("D")) {
                sb.append(" 东莞");
            } else if (work.equals("N")) {
                sb.append(" 宁国");
            } else if (work.equals("S")) {
                sb.append(" 深圳");
            }

            if (year.equals("A")) {
                sb.append(" 2016年");
            } else if (year.equals("B")) {
                sb.append(" 2017年");
            } else if (year.equals("C")) {
                sb.append(" 2018年");
            } else if (year.equals("D")) {
                sb.append(" 2019年");
            } else if (year.equals("E")) {
                sb.append(" 2020年");
            } else if (year.equals("F")) {
                sb.append(" 2021年");
            }

            sb.append(" 第" + week + "周");

            if (devType.equals("P")) {
                sb.append("  正常生产计划");
            } else if (devType.equals("D")) {
                sb.append("  刷机计划");
            } else if (devType.equals("S")) {
                sb.append("  售后翻新入库品");
            } else if (devType.equals("Y")) {
                sb.append("  研发、样机入库品");
            }

            sb.append(" " + carModel);//机型对照


            sb.append(" " + number);//流水线号

//            平台说明：A--786平台8G；
//            B--786平台16G；C--R16平台16G；
//            D--8227平台16G;E--T3平台16G；

            if (devModel.equals("A")) {
                sb.append("  786平台8G");
            } else if (devModel.equals("B")) {
                sb.append("  786平台16G");
            } else if (devModel.equals("C")) {
                sb.append("  R16平台16G");
            } else if (devModel.equals("D")) {
                sb.append("  8227平台16G");
            } else if (devModel.equals("E")) {
                sb.append("  T3平台16G");
            }

            if (rank.equals("A")) {
                sb.append("  低配");
            } else if (rank.equals("B")) {
                sb.append("  中配");
            } else if (rank.equals("C")) {
                sb.append("  高配");
            } else if (rank.equals("D")) {
                sb.append("  顶配");
            } else if (rank.equals("E")) {
                sb.append("  无配");
            }

            info = result + "：" + sb.toString();
            initDialog(info);

        }


        /**
         *自定义dialog
         */
        private void initDialog(final String msg) {
            final Dialog dlg = new Dialog(DvcScanActivity.this, R.style.ActionSheet);
            LayoutInflater inflater = (LayoutInflater) DvcScanActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_normal_layout, null);
            final int cFullFillWidth = 10000;
            layout.setMinimumWidth(cFullFillWidth);
            TextView saveBt = (TextView) layout.findViewById(R.id.save_bt);
            TextView noBt = (TextView) layout.findViewById(R.id.no_bt);
            TextView infoTv = (TextView) layout.findViewById(R.id.message);
            if (!TextUtils.isEmpty(msg)) {
                infoTv.setText(msg);
            }
            saveBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalLog.e(msg);
                    dlg.dismiss();
                    captureFragment.restartPreviewAndDecode();
                }
            });
            noBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlg.dismiss();
                    captureFragment.restartPreviewAndDecode();
                }
            });

            Window w = dlg.getWindow();
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.gravity = Gravity.CENTER;//设置dialog在屏幕中显示的位置
            dlg.setCanceledOnTouchOutside(false);
            dlg.setContentView(layout);
            dlg.show();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            DvcScanActivity.this.setResult(RESULT_OK, resultIntent);
//            ScanActivity.this.finish();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
        /**
         * 选择系统图片并解析
         */
        else if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Toast.makeText(DvcScanActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(DvcScanActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_CAMERA_PERM) {
            Toast.makeText(this, "从设置页面返回...", Toast.LENGTH_SHORT).show();
        }
    }
}
