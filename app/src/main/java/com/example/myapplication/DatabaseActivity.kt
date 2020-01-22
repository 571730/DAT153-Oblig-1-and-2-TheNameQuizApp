package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.my_text_view.view.*

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
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
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
            .inflate(R.layout.my_text_view, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(textView)
    }
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.itemName.text = myDataset[position].name
        holder.textView.itemImage.setImageBitmap(myDataset[position].image)
        holder.textView.button4.setOnClickListener{
            myDataset.removeAt(position)
            notifyDataSetChanged()
        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}
