package com.example.restaurapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.reservartions.Reservartions
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class ReservationsAdapter(private val data: Reservartions, private val onClickObject: ReservationsAdapter.MyOnClick):
    RecyclerView.Adapter<ReservationsAdapter.ViewHolder>() {

        lateinit var onLongClickObject: ReservationsAdapter.MyOnClick

        class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
            val imageView: ImageView = itemView.findViewById(R.id.imageview)
            val tvName: TextView = itemView.findViewById(R.id.tvName)
            val tvContent: TextView = itemView.findViewById(R.id.tvContent)
            val time: TextView = itemView.findViewById(R.id.time)

            val line: CardView = itemView.findViewById(R.id.cvLine) //some times itemView is used directly
        }

        interface MyOnClick {
            fun onClick(p0: View?, position: Int)
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.reservation_card, viewGroup, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.tvName.text = data.reservartions[position].title.toString()
            val pattern = "dd-MM-yy HH:mm"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val date: String = simpleDateFormat.format(data.reservartions[position].dateTime)
            viewHolder.time.text = date
            viewHolder.tvContent.text = "restaurant: " + data.getRestaurant(data.reservartions[position].restaurantId)

            Picasso.get().load("https://cdn-icons-png.flaticon.com/512/3132/3132693.png")
                .placeholder(R.drawable.placeholder).error(R.drawable.error).into(viewHolder.imageView);
            viewHolder.line.setOnClickListener(object: View.OnClickListener {
                override fun onClick(p0: View?) {
                    onClickObject.onClick(p0,position) // delegacija klica na lasten objekt-sledi razlaga
                }
            })

            viewHolder.line.setOnLongClickListener(object:View.OnLongClickListener{
                override fun onLongClick(p0: View?): Boolean {
                    onLongClickObject.onClick(p0, position)
                    return true
                }
            })
        }

        override fun getItemCount() = data.reservartions.size
    }