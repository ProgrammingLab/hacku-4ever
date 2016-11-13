package com.kurume_nct.himawari;

import android.support.v4.app.DialogFragment;

abstract public class BaseDialogFragment extends DialogFragment {
    public interface OnValueSetListener {
        void onValueSet(int requestCode, String val);
    }

    abstract OnValueSetListener getListener();
}
