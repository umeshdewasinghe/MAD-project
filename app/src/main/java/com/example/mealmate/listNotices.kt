package com.example.mealmate

import NoticeAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealmate.models.NoticeModel
import com.google.firebase.database.*

class listNotices : AppCompatActivity() {

    private lateinit var noticeRecycleView: RecyclerView
    private lateinit var tvNotice: TextView
    private lateinit var noticeList: ArrayList<NoticeModel>
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_notices)

        noticeRecycleView = findViewById(R.id.rvNotice)
        noticeRecycleView.layoutManager = LinearLayoutManager(this)
        noticeRecycleView.setHasFixedSize(true)
        tvNotice = findViewById(R.id.tvLoadingNotice)

        noticeList = arrayListOf<NoticeModel>()

        getNoticeData()


    }
    private fun getNoticeData() {
        noticeRecycleView.visibility = View.GONE
        tvNotice.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Notices")

        dbRef.addValueEventListener(object : ValueEventListener{




            override fun onDataChange(snapshot: DataSnapshot) {
                noticeList.clear()
                if(snapshot.exists()){
                    for (noticeSnap in snapshot.children){
                        val noticeData = noticeSnap.getValue(NoticeModel::class.java)
                        noticeList.add(noticeData!!)
                    }
                    val mAdaptor = NoticeAdapter(noticeList)
                    noticeRecycleView.adapter = mAdaptor

                    mAdaptor.setOnItemClickListener(object : NoticeAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@listNotices, noticeDetails::class.java)
                            intent.putExtra("nID",noticeList[position].noticeID)
                            intent.putExtra("nDate", noticeList[position].nDate)
                            intent.putExtra("nTopic", noticeList[position].nTopic)
                            intent.putExtra("nPlace", noticeList[position].nPlace)
                            intent.putExtra("nDescrtption", noticeList[position].nDescrtption)//need to add date

                            startActivity(intent)
                        }

                    })

                    noticeRecycleView.visibility = View.VISIBLE
                    tvNotice.visibility = View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}