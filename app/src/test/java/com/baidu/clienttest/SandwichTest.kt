package com.baidu.clienttest

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowAlertDialog
import org.robolectric.shadows.ShadowLog
import org.robolectric.shadows.ShadowToast
import org.robolectric.util.FragmentTestUtil


@RunWith(AndroidJUnit4::class)
class SandwichTest {

    @Rule
    @JvmField
    val activityRule: ActivityTestRule<BasicFunActivity> =
        ActivityTestRule(BasicFunActivity::class.java)

    private val TAG = "test"

    private lateinit var mainActivity: BasicFunActivity
    private lateinit var mJumpBtn: Button
    private lateinit var mToastBtn: Button
    private lateinit var mDialogBtn: Button
    private lateinit var mInverseBtn: Button
    private lateinit var checkBoxView: CheckBox

    @Before
    fun setUp() {
        // 输出日志
        ShadowLog.stream = System.out
        mainActivity = activityRule.activity
        mJumpBtn = mainActivity.findViewById<Button>(R.id.routeButton)
        mToastBtn = mainActivity.findViewById<Button>(R.id.toastButton)
        mDialogBtn = mainActivity.findViewById<Button>(R.id.dialogButton)
        mInverseBtn = mainActivity.findViewById<Button>(R.id.stateButton)
        checkBoxView = mainActivity.findViewById<CheckBox>(R.id.checkboxView)
    }

    @Test
    fun click_button_and_go_second_screen() {
        Robolectric.buildActivity(BasicFunActivity::class.java).use { controller ->
            // 将Activity移至恢复状态
            controller.setup()
            val activity: BasicFunActivity = controller.get()

            val button = activity.findViewById<Button>(R.id.stateButton)
            button.performClick()
            assertEquals(button.text, "UI组件状态验证")
        }
    }

    @Test
    fun testToast() {
        // 触发按钮点击
        mToastBtn.performClick()
        // 判断Toast已经弹出
        assertNotNull(ShadowToast.getLatestToast())
        //验证捕获的最近显示的Toast
        Assert.assertEquals("Hello UT!", ShadowToast.getTextOfLatestToast())

        // 捕获所有已显示的Toast
        val toasts: List<Toast> = shadowOf(RuntimeEnvironment.application).shownToasts
        Assert.assertNotEquals(Toast.LENGTH_SHORT.toLong(), toasts[0].duration.toLong())
    }


    /**
     * 验证点击事件是否触发了页面跳转，验证目标页面是否预期页面
     *
     * @throws Exception
     */
    @Test
    @Throws(Exception::class)
    fun testJump() {
        Assert.assertEquals(mJumpBtn.text.toString(), "Activity跳转")
        // 触发按钮点击
        mJumpBtn.performClick()

        // 获取对应的Shadow类
        val shadowActivity = Shadows.shadowOf(mainActivity)
        // 借助Shadow类获取启动下一Activity的Intent
        val nextIntent = shadowActivity.nextStartedActivity
        // 校验Intent的正确性
        Assert.assertEquals(nextIntent.component?.className, DemoActivity::class.java.getName())
    }


    /**
     * 验证Dialog是否正确弹出
     *
     * @throws Exception
     */
    @Test
    @Throws(Exception::class)
    fun showDialog() {
        // 捕获最近显示的Dialog
        var dialog = ShadowAlertDialog.getLatestAlertDialog()
        // 判断Dialog尚未弹出
        Assert.assertNull(dialog)

        // 点击按钮
        mDialogBtn.performClick()

        // 捕获最近显示的Dialog
        dialog = ShadowAlertDialog.getLatestAlertDialog()
        // 判断Dialog已经弹出
        Assert.assertNotNull(dialog)
        // 获取Shadow类进行验证
        val shadowDialog = Shadows.shadowOf(dialog)
        Assert.assertEquals("Hello UT！", shadowDialog.message)
    }

    @Test
    @Throws(Exception::class)
    fun testCheckBoxState() {
        // 验证CheckBox初始状态
        Assert.assertFalse(checkBoxView.isChecked)

        // 点击按钮反转CheckBox状态
        checkBoxView.performClick()
        // 验证状态是否正确
        Assert.assertTrue(checkBoxView.isChecked)

        // 点击按钮反转CheckBox状态
        checkBoxView.performClick()
        // 验证状态是否正确
        Assert.assertFalse(checkBoxView.isChecked)
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun testFragment() {
        val scenario = launchFragmentInContainer<SecondFragment>(
            initialState = Lifecycle.State.INITIALIZED
        )
        // EventFragment has gone through onAttach(), but not onCreate().
        // Verify the initial state.
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onFragment { fragment ->
            // 验证Fragment是否显示布局
            assertNotNull(fragment.view)

            val button: Button? = fragment.view?.findViewById<Button>(R.id.button_second)
            assertNotNull(button)
            // 验证设置的参数值是否显示了
            assertEquals("Previous", button?.text)
        }
    }

}