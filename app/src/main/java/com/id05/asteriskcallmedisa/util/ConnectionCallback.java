package com.id05.asteriskcallmedisa.util;

import com.id05.asteriskcallmedisa.data.AmiState;

public interface ConnectionCallback {
    void onBegin();

    void onSuccess(AmiState amistate);

    void onFailure(AmiState amiState);

    void onEnd();
}