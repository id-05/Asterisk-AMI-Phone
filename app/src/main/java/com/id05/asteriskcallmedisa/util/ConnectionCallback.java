package com.id05.asteriskcallmedisa.util;

import com.id05.asteriskcallmedisa.data.AmiState;

public interface ConnectionCallback <V> {
    void onBegin(); //Асинхронная операция началась

    void onSuccess(AmiState amistate); //Получили результат

    void onFailure(AmiState amiState); //Получили ошибку

    void onEnd(); //Операция закончилась
}