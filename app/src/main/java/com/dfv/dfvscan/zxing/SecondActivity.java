package com.dfv.dfvscan.zxing;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.dfv.dfvscan.Adapter.OrderCodeAdapter;
import com.dfv.dfvscan.R;
import com.dfv.dfvscan.Utils.LocalLog;
import com.dfv.dfvscan.bean.CodeList;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 公司设备扫描
 * 定制化显示扫描界面
 */
public class SecondActivity extends FragmentActivity {


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


    ArrayList<CodeList> datas;
    OrderCodeAdapter adapter;
    String stateCode;
    int delete_position;

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

        datas = new ArrayList<>();


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

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 22:
                    delete_position = msg.arg1;
                    datas.remove(delete_position);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

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
            SecondActivity.this.setResult(RESULT_OK, resultIntent);

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

            if (result.length() == 11) {
                getData(result);
            } else {
                Toast.makeText(SecondActivity.this, "编码格式不对", Toast.LENGTH_SHORT).show();
                captureFragment.restartPreviewAndDecode();
            }

        }


        private void getData(String result) {
            StringBuffer sb = new StringBuffer();
            String info = "";
            String branch = result.substring(0, 3);//所属部门
            String dvcBrand = result.substring(7, 9);//设备品牌
            String dvcType = result.substring(9, 11);//设备种类
            //000   东莞带入设备
            //001   董事长
            //002   总经办
            //003   财务部
            //004   人事行政部
            //005   生产部
            //006   商务部
            //007   成品仓
            //008   品控中心
            //009   测试中心
            //010   工程部
            //011   品质部
            //012   pmc
            //013   仓库
            //014   运营中心
            if (branch.equals("000")) {
                sb.append(" 东莞带入设备");
            } else if (branch.equals("001")) {
                sb.append(" 董事长");
            } else if (branch.equals("002")) {
                sb.append(" 总经办");
            } else if (branch.equals("003")) {
                sb.append(" 财务部");
            } else if (branch.equals("004")) {
                sb.append(" 人事行政部");
            } else if (branch.equals("005")) {
                sb.append(" 生产部");
            } else if (branch.equals("006")) {
                sb.append(" 商务部");
            } else if (branch.equals("007")) {
                sb.append(" 成品仓");
            } else if (branch.equals("008")) {
                sb.append(" 品控中心");
            } else if (branch.equals("009")) {
                sb.append(" 测试中心");
            } else if (branch.equals("010")) {
                sb.append(" 工程部");
            } else if (branch.equals("011")) {
                sb.append(" 品质部");
            } else if (branch.equals("012")) {
                sb.append(" pmc");
            } else if (branch.equals("013")) {
                sb.append(" 仓库");
            } else if (branch.equals("014")) {
                sb.append(" 运营中心");
            }
//            HL  海兰
//            LX  联想
//            ZZ  组装机
//            EP  EPSOW LQ
//            SN  SNBC BTP
//            XM  小米
            if (dvcBrand.equals("HL")) {
                sb.append(" 海兰");
            } else if (dvcBrand.equals("LX")) {
                sb.append(" 联想");
            } else if (dvcBrand.equals("ZZ")) {
                sb.append(" 组装机");
            } else if (dvcBrand.equals("EP")) {
                sb.append(" EPSOW LQ");
            } else if (dvcBrand.equals("SN")) {
                sb.append(" SNBC BTP");
            } else if (dvcBrand.equals("XM")) {
                sb.append(" 小米");
            }
//            01  一体式电脑
//            02  台式电脑
//            03  笔记本电脑
//            04  打印机
//            05  针式打印机
//            06  条码机
            if (dvcType.equals("01")) {
                sb.append("  一体式电脑");
            } else if (dvcType.equals("02")) {
                sb.append("  台式电脑");
            } else if (dvcType.equals("03")) {
                sb.append("  笔记本电脑");
            } else if (dvcType.equals("04")) {
                sb.append("  打印机");
            } else if (dvcType.equals("05")) {
                sb.append("  针式打印机");
            } else if (dvcType.equals("06")) {
                sb.append("  条码机");
            }
//            LocalLog.e(result + "：" + sb.toString());

            info = result + "：" + sb.toString();
            initDialog(info);

        }


        /**
         *自定义dialog
         */
        private void initDialog(final String msg) {
            final Dialog dlg = new Dialog(SecondActivity.this, R.style.ActionSheet);
            LayoutInflater inflater = (LayoutInflater) SecondActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    LocalLog.i(msg);
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
            SecondActivity.this.setResult(RESULT_OK, resultIntent);
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
                            Toast.makeText(SecondActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(SecondActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
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
