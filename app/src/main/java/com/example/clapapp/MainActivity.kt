package com.example.clapapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    // tạo 1 biến thì PHẢI khởi tạo giá trị cho biến đó
    // lateinit tạo biến CHƯA cần khởi tạo giá trị cho biến đó
    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var seekBar : SeekBar
    private lateinit var runnable: Runnable
    private lateinit var handler : Handler
    // Runnable(công viec) và Handler(người xử lí): 2 thuộc tính này đi cùng nhau
    // vì công việc thì cần có người xử lí để sắp xếp công việc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        mediaPlayer = MediaPlayer.create(this, R.raw.applauding)
        seekBar = findViewById(R.id.sbClapping)
        handler = Handler(Looper.getMainLooper()) // chạy trong luồng chính, tạo ràng buộc cho handler

        /* val button = findViewById<Button>(R.id.btnClap)
        button.setOnClickListener{
            mediaPlayer.start()
        }*/

        val buttonPlay = findViewById<FloatingActionButton>(R.id.fabPlay)
        buttonPlay.setOnClickListener {
            initializeSeekBar()    // đặt trong setOnClickListener
            mediaPlayer.start()
        }

        val buttonPause = findViewById<FloatingActionButton>(R.id.fabPause)
        buttonPause.setOnClickListener {
            mediaPlayer.pause()
        }

        val buttonStop = findViewById<FloatingActionButton>(R.id.fabStop)
        buttonStop.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer.reset() // reset về trạng thái ban đầu (ở đây trạng thái ban đầu của mediaPlayer là NULL)
            mediaPlayer.release() // giải phóng bộ nhớ và đóng các tài nguyên
            mediaPlayer = MediaPlayer.create(this, R.raw.applauding) // đặt lại gtri cho mediaPlayer
            handler.removeCallbacks(runnable)  // xóa các runnable (công việc) trong bộ nhớ
            seekBar.progress = 0
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {  //Được gọi khi giá trị thay đổi
                if(fromUser) mediaPlayer.seekTo(progress)    //seekTo: lấy giá trị hiện tại của seekBar
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { // Được gọi khi người dùng chạm vào seekBar

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { // Được gọi khi người dùng kết thúc hành đông chạm vào seekBar

            }

        })
    }
    private fun initializeSeekBar(){
        val tvPlay = findViewById<TextView>(R.id.tvPlayed)
        val tvDue = findViewById<TextView>(R.id.tvDue)

        val playTime = mediaPlayer.currentPosition / 1000; // mediaPlayer: thời gian được sử dụng là mili-giây
        tvPlay.text = "$playTime s"
        val duration = mediaPlayer.duration / 1000
        val dueTime = duration - playTime
        tvDue.text = "$dueTime s"

        seekBar.max = mediaPlayer.duration   // duration: khoang thoi gian
        runnable = Runnable {
            seekBar.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable, 1000) // delay 1s (1000 = 1 giay)
        }
        handler.postDelayed(runnable, 1000)
    }
}