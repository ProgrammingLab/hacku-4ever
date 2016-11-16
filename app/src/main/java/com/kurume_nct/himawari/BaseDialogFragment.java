package com.kurume_nct.himawari;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

abstract public class BaseDialogFragment extends DialogFragment {
    public interface OnValueSetListener {
        void onValueSet(int requestCode, String val);
    }

    public OnValueSetListener getListener() {
        Fragment fragment = getTargetFragment();
        if (fragment instanceof OnValueSetListener) {
            return (OnValueSetListener) fragment;
        } else {
            return null;
        }
    }
}
