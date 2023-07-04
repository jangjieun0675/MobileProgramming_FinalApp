package com.example.myfinalapp

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myfinalapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var toolbar : ActionBarDrawerToggle
    lateinit var binding: ActivityMainBinding
    lateinit var retrofitFragment: TwoFragment
    lateinit var boardFragment : BoardFragment
    lateinit var sharedPreferences: SharedPreferences
    var authMenuItem: MenuItem? = null
    var mode = "retrofit"

    class MyFragmentAdapter(activity: FragmentActivity): FragmentStateAdapter(activity){
        val fragments:List<Fragment>
        init{
            fragments = listOf(OneFragment(),TwoFragment(),ThreeFragment())//스와이프할 순서대로 작성
        }
        override fun getItemCount(): Int {//프래그먼트 갯수 리턴
            return fragments.size
        }
        override fun createFragment(position: Int): Fragment {//프래그먼트를 하나씩 리턴
            return fragments[position]
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofitFragment = TwoFragment()
        boardFragment = BoardFragment()

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val bgColor:String? = sharedPreferences.getString("bg_color","")
        binding.drawer.setBackgroundColor(Color.parseColor(bgColor))

        val txColor:String? = sharedPreferences.getString("tx_color","")
        binding.tabs.setTabTextColors(Color.parseColor(txColor), Color.parseColor(txColor))


        supportFragmentManager.beginTransaction()
            .add(R.id.drawer, boardFragment)
            .hide(boardFragment)
            .commit()

        toolbar = ActionBarDrawerToggle(this,binding.drawer, R.string.drawer_opened, R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.syncState()

        binding.viewpager2.adapter =MyFragmentAdapter(this)

        val tabTitles = listOf("Home", "Search", "Info")
        TabLayoutMediator(binding.tabs,binding.viewpager2){
                tab,position -> tab.text = tabTitles[position]
        }.attach()
        binding.mainDrawer.setNavigationItemSelectedListener(this)

    }

    override fun onResume() {
        super.onResume()

        val bgColor:String? = sharedPreferences.getString("bg_color","")
        binding.drawer.setBackgroundColor(Color.parseColor(bgColor))

        val txColor:String? = sharedPreferences.getString("tx_color","")
        binding.tabs.setTabTextColors(Color.parseColor(txColor), Color.parseColor(txColor))

        binding.drawer.closeDrawers()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_board -> {
                supportFragmentManager.beginTransaction()
                    .show(boardFragment)
                    .hide(retrofitFragment)
                    .commit()
                mode = "board"
                supportActionBar?.title = "게시판"
                binding.drawer.closeDrawers() // 메뉴 선택 후 드로어를 닫기 위해 추가합니다.
                binding.viewpager2.visibility = View.GONE // viewpager2를 감춥니다.
            }
            R.id.item2 ->{
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:/119"))
                startActivity(intent)
            }
            R.id.item3 ->{
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.471492, 127.142326"))
                startActivity(intent)
            }
            R.id.menu_main_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }

        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_actionbar,menu)

        authMenuItem = menu!!.findItem(R.id.menu_auth)
        if(MyApplication.checkAuth()){
            authMenuItem!!.title = "${MyApplication.email}님"

        }
        else{
            authMenuItem!!.title="인증"
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onStart() {
        //Intent애서 finish() 돌아올 때 실행
        //onCreate -> onStart -> onCreateOptionsMenu
        super.onStart()
        if(authMenuItem!=null){
            if(MyApplication.checkAuth()){
                authMenuItem!!.title = "${MyApplication.email}님"
            }
            else{
                authMenuItem!!.title="인증"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toolbar.onOptionsItemSelected(item)){
            return true
        }
        if(item.itemId === R.id.menu_retrofit && mode !== "retrofit"){
            supportFragmentManager.beginTransaction()
                .show(retrofitFragment)
                .commit()
            supportFragmentManager.beginTransaction()
                .hide(boardFragment)
                .commit()
            mode="retrofit"

            supportActionBar?.title="약 정보 검색"
        }
        else if(item.itemId===R.id.menu_auth){
            var intent = Intent(this,AuthActivity::class.java)
            if(authMenuItem!!.title!!.equals("인증")){
                intent.putExtra("data","logout")
            }
            else{
                intent.putExtra("data","login")
            }
            startActivity(Intent(this,AuthActivity::class.java))
        }
        else if(item.itemId===R.id.menu_home){
            val intent = Intent(this, MainActivity::class.java) // MainActivity로 이동하는 Intent 생성
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

}
