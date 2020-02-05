package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Entity.PersonEntity
import kotlinx.android.synthetic.main.activity_add_person.*
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.my_text_view.view.button4
import kotlinx.android.synthetic.main.my_text_view.view.itemImage
import kotlinx.android.synthetic.main.my_text_view.view.itemName

class DatabaseActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var personViewModel: PersonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        personViewModel = ViewModelProvider(this).get(PersonViewModel::class.java)
        viewManager = LinearLayoutManager(this)
        val viewAdapter = MyAdapter(this, personViewModel)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        personViewModel.allPeople.observe(this, Observer { person ->
            // Update the cached copy of the words in the adapter.
            person?.let { viewAdapter.setPeople(it) }
        })
//         need this to handle swipe to remove from recycleview
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                personViewModel.remove(viewAdapter.getItem(position))
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

class MyAdapter(context: Context, val personViewModel: PersonViewModel) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var people = emptyList<PersonEntity>()
    private val inflater = LayoutInflater.from(context)

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val textView: View) : RecyclerView.ViewHolder(textView)
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {
        // create a new view
        val textView = inflater.inflate(R.layout.person_card, parent, false)
        return MyViewHolder(textView)
    }
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.itemName.text = people[position].name
        Log.i("addPic", "added picture ${people[position].name} from ${people[position].picture}")
        Glide.with(holder.textView.itemImage.context)
            .load(people[position].picture)
            .into(holder.textView.itemImage)
        holder.textView.itemImage.clipToOutline = true
        holder.textView.button4.setOnClickListener{
           personViewModel.remove(people[position])
        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = people.size

    internal fun setPeople(people: List<PersonEntity>) {
        this.people = people
        notifyDataSetChanged()
    }

    fun getItem(position: Int): PersonEntity {
        return people[position]
    }

}
