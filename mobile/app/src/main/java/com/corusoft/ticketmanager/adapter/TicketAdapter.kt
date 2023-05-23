package com.corusoft.ticketmanager.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.corusoft.ticketmanager.R
import com.corusoft.ticketmanager.backend.dtos.tickets.TicketDTO

class TicketAdapter(
    private val context: Context,
    private val dataset: List<TicketDTO>
) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    class TicketViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val storeView: TextView = view.findViewById(R.id.textView13)
        val currencyView: TextView = view.findViewById(R.id.textView9)
        val nameView: TextView = view.findViewById(R.id.name)
        val imageView: ImageView = view.findViewById(R.id.item_image)
        val expandableLayout: RelativeLayout = view.findViewById(R.id.expandable_layout)
        val notexpandable_layout: LinearLayout = view.findViewById(R.id.notexpandable_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_ticket, parent, false)
        return TicketViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = dataset[position]
        holder.storeView.text = ticket.store
        holder.currencyView.text = ticket.currency
        holder.nameView.text = ticket.name
        holder.imageView.setImageBitmap(decodeBase64(ticket.picture))

        val isExpandable = dataset[position].expandable
        holder.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE

        holder.notexpandable_layout.setOnClickListener {
            val ticket = dataset[position]
            ticket.expandable = !ticket.expandable
            notifyItemChanged(position)
            holder.imageView.height
        }
    }

    override fun getItemCount() = dataset.size

    private fun decodeBase64(base64String: String?): Bitmap {

        // Decodifica la cadena Base64 a un array de bytes
        val decodeBytes = Base64.decode(base64String, Base64.DEFAULT)

        // Crea un objeto Bitmap a partir del array de bytes
        val bitmap = BitmapFactory.decodeByteArray(decodeBytes, 0, decodeBytes.size)

        return bitmap
    }

}
