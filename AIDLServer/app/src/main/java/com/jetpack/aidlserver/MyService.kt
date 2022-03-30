package com.jetpack.aidlserver

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyService : Service() {

    private val impl: MyImpl = MyImpl()

    override fun onBind(intent: Intent): IBinder {
        return impl
    }

    class MyImpl: ICommon.Stub() {
        override fun calculate(num1: Int, num2: Int): Int {
            return num1 + num2 // this sum of two number added 450 + 450
        }
    }
}