package com.example.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_add_person.view.*
import kotlinx.android.synthetic.main.my_text_view.*
import kotlinx.android.synthetic.main.my_text_view.view.*
import kotlinx.android.synthetic.main.my_text_view.view.button4
import kotlinx.android.synthetic.main.my_text_view.view.itemImage
import kotlinx.android.synthetic.main.my_text_view.view.itemName
import kotlinx.android.synthetic.main.person_card.view.*
import java.io.File

class DatabaseActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        val data = (application as AppSingleton).data
        viewManager = LinearLayoutManager(this)
        viewAdapter = MyAdapter(data)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        // need this to handle swipe to remove from recycleview
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (viewAdapter as MyAdapter).removeItem(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun goToAddPerson(view: View){
        val intent = Intent(this, AddPersonActivity::class.java)
        startActivity(intent)
    }
}

class MyAdapter(private val myDataset: ArrayList<Person>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val textView: View) : RecyclerView.ViewHolder(textView)
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.person_card, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(textView)
    }
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.itemName.text = myDataset[position].name
//        holder.textView.itemImage.setImageBitmap(myDataset[position].image)
        Glide.with(holder.textView.itemImage.context)
            .load(myDataset[position].image)
            .into(holder.textView.itemImage)
        holder.textView.itemImage.clipToOutline = true
        holder.textView.button4.setOnClickListener{
            myDataset.removeAt(position)
            notifyDataSetChanged()
        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

    fun removeItem(position: Int) {
        myDataset.removeAt(position)
        notifyDataSetChanged()
    }

}
