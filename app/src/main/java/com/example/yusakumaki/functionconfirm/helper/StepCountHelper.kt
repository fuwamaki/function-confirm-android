package com.example.yusakumaki.functionconfirm.helper

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Calendar
import java.util.concurrent.TimeUnit

object StepCountHelper {

    const val GOOGLE_SIGN_IN_REQUEST_CODE = 0
    const val GOOGLE_FIT_PERMISSION_REQUEST_CODE = 1
    private const val PERMISSION = "android.permission.ACTIVITY_RECOGNITION"

    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA)
        .build()

    // これが、歩数データ連携済みかどうか
    // memo: なんか、連携を切ってもtrueのままになってる。
    // memo: ☆歩数の取得に失敗している場合、リクエストできるようにしておいた方がいい
    fun hasFitnessPermission(context: Context): Boolean {
        return GoogleSignIn.hasPermissions(
            GoogleSignIn.getLastSignedInAccount(context),
            fitnessOptions
        )
    }

    fun checkGoogleSignIn(fragment: Fragment, onComplete: () -> Unit) {
        val googleAccount = GoogleSignIn.getLastSignedInAccount(fragment.requireContext())
//        if (!hasFitnessPermission(fragment.requireContext())) {
            GoogleSignIn.requestPermissions(fragment, 0, googleAccount, fitnessOptions)
//        } else {
//            onComplete()
//        }
    }

    @Suppress("DEPRECATION")
    fun hasGoogleFitApplication(context: Context): Boolean {
        val packageName = "com.google.android.apps.fitness"
        return try {
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES) != null
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun checkActivityPermission(
        activity: FragmentActivity,
        onComplete: (isGranted: Boolean) -> Unit
    ) {
        when (ContextCompat.checkSelfPermission(activity, PERMISSION)) {
            PackageManager.PERMISSION_GRANTED -> onComplete(true)
            else -> {
                activity.requestPermissions(arrayOf(PERMISSION), GOOGLE_FIT_PERMISSION_REQUEST_CODE)
            }
        }
    }

    @Suppress("DEPRECATION")
    suspend fun readTodayStepCount(context: Context): Int = withContext(Dispatchers.IO) {
        val googleAccount = GoogleSignIn.getLastSignedInAccount(context)
            ?: throw IllegalStateException("googleAccount is null")
        val client = Fitness.getHistoryClient(context, googleAccount)
            .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
        val dataPoints = Tasks.await(
            client,
            30,
            TimeUnit.SECONDS
        ).dataPoints
        dataPoints.forEach {
            Timber.d(it.dataSource.toString())
        }
        dataPoints.firstOrNull()?.getValue(Field.FIELD_STEPS)?.asInt() ?: 0
    }

    @Suppress("DEPRECATION")
    suspend fun readTodayStepOriginalCount(context: Context): Int = withContext(Dispatchers.IO) {
        val googleAccount = GoogleSignIn.getLastSignedInAccount(context)
            ?: throw IllegalStateException("googleAccount is null")
        // 当日の00:00のタイムスタンプを取得
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        val readRequest = DataReadRequest.Builder()
            .read(DataType.TYPE_STEP_COUNT_DELTA)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()

        val client = Fitness.getHistoryClient(context, googleAccount)
            .readData(readRequest)
        val dataSets = Tasks.await(
            client,
            30,
            TimeUnit.SECONDS
        ).dataSets
        dataSets
            .firstOrNull()
            ?.dataPoints
            ?.filter { it.originalDataSource.streamName != "user_input" }
            ?.filter { it.originalDataSource.device != null }
            ?.sumOf { it.getValue(Field.FIELD_STEPS).asInt() } ?: 0
    }
}