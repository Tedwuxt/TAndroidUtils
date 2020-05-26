package com.tao.keyhashutil

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {

    var pkgName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn_cal.setOnClickListener {
            pkgName = et_pkgname.text.toString()

            try {
                val info = packageManager.getPackageInfo(pkgName, PackageManager.GET_SIGNATURES)
                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    val KeyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT)

                    et_key.setText(KeyHash)
                }
            } catch (e: PackageManager.NameNotFoundException) {
            } catch (e: NoSuchAlgorithmException) {
            }
        }


        btn_market.setOnClickListener {
            startMarketView(getGooglePlayStoreUrl("com.id.go.arisan"))
        }

        btn_md5.setOnClickListener {

        }


        btn_app_sign_key.setOnClickListener {
            val pkgName = et_app_sign_key.text.toString().trim()


            val appSignature = AppSignatureHelper(this)
            val hash = appSignature.getAppSignatures(pkgName)

            et_app_sign_key.setText(hash)
        }
    }


    fun getGooglePlayStoreUrl(packageStr: String): String {
        val packageManager = this@MainActivity.applicationContext.packageManager
        val marketUri = Uri.parse("market://details?$packageStr")
        val marketIntent = Intent(Intent.ACTION_VIEW).setData(marketUri)
        return if (marketIntent.resolveActivity(packageManager) != null) {
            "market://details?$packageStr"
        } else {
            "https://play.google.com/store/apps/details?$packageStr"
        }
    }

    /**
     * 打开系统自带的View跳转链接
     *
     * @param context
     * @param link
     */
    fun startMarketView(link: String) {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val content_url = Uri.parse(link)
        System.out.print("startMarketView url: $content_url")
        intent.data = content_url
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setPackage("com.android.vending")  //指定应用市场
        startActivity(intent)

    }


}
