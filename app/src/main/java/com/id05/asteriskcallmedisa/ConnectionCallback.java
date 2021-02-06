package com.id05.asteriskcallmedisa;

interface ConnectionCallback <V> {
    void onBegin(); //Асинхронная операция началась

    void onSuccess(String data, String param); //Получили результат

    void onFailure(Throwable t); //Получили ошибку

    void onEnd(); //Операция закончилась
}