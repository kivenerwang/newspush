package cn.hjl.newspush.rxpacked;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import cn.hjl.newspush.R;
import cn.hjl.newspush.utils.MyUtils;
import cn.hjl.newspush.widget.loadview.AVLoadingIndicatorView;
import rx.Subscriber;

/**
 * Created by fastabler on 2016/12/2.
 */

public abstract class _Subscriber<T> extends Subscriber<T> {

    private Context mContext;
    //    private ProgressDialog dialog;
    private String msg;
    private boolean showDialog = false;

    private Dialog dialog;


    protected boolean showDialog() {
        return showDialog;
    }

    public _Subscriber(Context context, String msg, boolean showDialog) {
        this.mContext = context;
        this.msg = msg;
        this.showDialog = showDialog;
    }

    public _Subscriber(Context context) {
        this(context, "请稍后...", false);
    }

    public _Subscriber(Context context, boolean showDialog) {
        this(context, "请稍后...", showDialog);
    }

    @Override
    public void onCompleted() {
        if (showDialog() && dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onError(Throwable e) {

        e.printStackTrace();

        _onError(MyUtils.analyzeNetworkError(e));

        if (dialog != null && showDialog()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (showDialog()) {
            if (mContext != null) {
                View layout = LayoutInflater.from(mContext).inflate(R.layout.progress_common_indicator, null);
                ((AVLoadingIndicatorView) layout.findViewById(R.id.indicator)).setIndicator("BallClipRotateMultipleIndicator");
                dialog = new ProgressDialog(mContext);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (!isUnsubscribed()){
                            // 当取消加载，给个方法进行取消网络请求
                            unSubscribe();
                            unsubscribe();
                        }
                    }
                });
                dialog.show();
                dialog.setContentView(layout);

//                dialog = new ProgressDialog(mContext);
//                dialog.setMessage(msg);
//                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                dialog.setCancelable(true);
//                dialog.setCanceledOnTouchOutside(false);
//                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        if (!isUnsubscribed()){
//                            // 当取消加载，给个方法进行取消网络请求
//                            unSubscribe();
//                            unsubscribe();
//                        }
//                    }
//                });
//                dialog.show();
            }

        }
    }

    /**
     * 当取消订阅时执行此方法
     * 一般用于取消网络请求返回状态
     */
    public void unSubscribe() {

    }


    @Override
    public void onNext(T o) {
        _onNext(o);
    }


    protected abstract void _onNext(T t);

    protected abstract void _onError(String message);
}
