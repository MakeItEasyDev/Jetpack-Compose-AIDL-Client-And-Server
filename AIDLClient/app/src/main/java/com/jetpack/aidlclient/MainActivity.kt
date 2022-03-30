package com.jetpack.aidlclient

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jetpack.aidlclient.ui.theme.AIDLClientTheme
import com.jetpack.aidlserver.ICommon

class MainActivity : ComponentActivity() {
    private lateinit var common: ICommon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val firstValue = remember { mutableStateOf("") }
            val secondValue = remember { mutableStateOf("") }
            val result = remember { mutableStateOf(0) }

            AIDLClientTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "AIDL Client App",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                        }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = {
                                    val intent = Intent("com.jetpack.aidlserver.AIDL")
                                    bindService(convertImplicitIntentToExplicitIntent(intent), serviceCon, BIND_AUTO_CREATE)
                                }
                            ) {
                                Text(text = "Bind")
                            }

                            Spacer(modifier = Modifier.height(25.dp))

                            OutlinedTextField(
                                value = firstValue.value,
                                onValueChange = { firstValue.value = it },
                                label = { Text(text = "Enter Value") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(0.8f)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = secondValue.value,
                                onValueChange = { secondValue.value = it },
                                label = { Text(text = "Enter Value") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(0.8f)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    result.value = common.calculate(firstValue.value.toInt(), secondValue.value.toInt())
                                }
                            ) {
                                Text(text = "Calculate")
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Final Result: ${result.value}",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    private var serviceCon = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            //in this line call to service
            common = ICommon.Stub.asInterface(service)

        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            TODO("Not yet implemented")
        }
    }

    //Here we have to implicit intent to convert explicit intent
    private fun convertImplicitIntentToExplicitIntent(implicitIntent: Intent?): Intent? {
        val pm: PackageManager = packageManager
        val resolveInfoList = pm.queryIntentServices(
            implicitIntent!!, 0
        )
        if (resolveInfoList == null || resolveInfoList.size != 1) {
            return null
        }

        val serviceInfo = resolveInfoList[0]
        val component = ComponentName(serviceInfo.serviceInfo.packageName, serviceInfo.serviceInfo.name)
        val explicitIntent = Intent(implicitIntent)
        explicitIntent.component = component

        return explicitIntent
    }

}

















