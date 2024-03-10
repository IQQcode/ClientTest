package com.baidu.clienttest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.baidu.clienttest.databinding.ActivityBasicFunBinding

class BasicFunActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityBasicFunBinding
    private var lifecycleState: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasicFunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.routeButton.setOnClickListener(this)
        binding.toastButton.setOnClickListener(this)
        binding.dialogButton.setOnClickListener(this)
        binding.stateButton.setOnClickListener(this)
        binding.checkboxView.setOnClickListener(this)
    }

    private fun jump(view: View) {
        startActivity(Intent(this, DemoActivity::class.java))
    }

    private fun showToast(view: View?) {
        Toast.makeText(this, "Hello UT!", Toast.LENGTH_LONG).show()
    }

    private fun showDialog(view: View) {
        val alertDialog: AlertDialog = AlertDialog.Builder(this)
            .setMessage("Hello UT！")
            .setTitle("提示")
            .create()
        alertDialog.show()
    }

    private fun inverse(view: View?) {
        binding.checkboxView.setChecked(binding.checkboxView.isChecked)
    }

    private fun getLifecycleState(): String {
        return lifecycleState
    }


    override fun onStart() {
        super.onStart()
        lifecycleState = "onStart"
    }

    override fun onResume() {
        super.onResume()
        lifecycleState = "onResume"
    }

    override fun onPause() {
        super.onPause()
        lifecycleState = "onPause"
    }

    override fun onStop() {
        super.onStop()
        lifecycleState = "onStop"
    }

    override fun onRestart() {
        super.onRestart()
        lifecycleState = "onRestart"
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleState = "onDestroy"
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.routeButton -> {
                jump(v)
            }

            R.id.toastButton -> {
                showToast(v)
            }

            R.id.dialogButton -> {
                showDialog(v)
            }

            R.id.stateButton -> {
                Toast.makeText(this, getLifecycleState(), Toast.LENGTH_LONG).show()
            }

            R.id.checkboxView -> {
                inverse(v)
            }
        }
    }
}