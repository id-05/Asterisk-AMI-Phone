package com.id05.asteriskcallmedisa;

interface ConnectionCallback <V> {
    void onBegin(); //Асинхронная операция началась

    void onSuccess(AmiState amistate); //Получили результат

    void onFailure(AmiState amiState); //Получили ошибку

    void onEnd(); //Операция закончилась
}