package com.webnexs.tranznexsdriver.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.webnexs.tranznexsdriver.pojoClasses.TripDetails;
import com.squareup.picasso.Picasso;
import com.webnexs.tranznexsdriver.R;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class TripDetailsAdapter extends RecyclerView.Adapter<TripDetailsAdapter.CustomViewHolder>{


    private List<TripDetails> tripDetails;
    android.content.Context mContext;
    private Object Context;

    public TripDetailsAdapter(List<TripDetails> tripDetails ) {
        this.tripDetails = tripDetails;
        this.mContext = mContext;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_list, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        this.mContext=mContext;
        Picasso.get().load(tripDetails.get(position).getRider_image()).into(holder.image1);
        Picasso.get().load(tripDetails.get(position).getStatus_stamp()).into(holder.img_status);
        TripDetails trips = tripDetails.get(position);
        holder.date.setText(trips.getRequest_time());
        holder.rider_id.setText(trips.getRider_id());
        holder.fareAmount.setText("$"+trips.getFare());
        holder.rider_name.setText(trips.getRidername());
        holder.fromLOcation.setText(trips.getPickup_loc());
        holder.toLocation.setText(trips.getDrop_loc());
    }

    @Override
    public int getItemCount() {
        return tripDetails.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView date, fromLOcation,toLocation,rider_id,rider_name,fareAmount;
        public CircleImageView image1;
        public ImageView img_status;



        public CustomViewHolder(final View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.dateTime);
            fromLOcation = (TextView) view.findViewById(R.id.from_Location);
            toLocation = (TextView) view.findViewById(R.id.to_Location);
            rider_id = (TextView) view.findViewById(R.id.rider_id);
            fareAmount = (TextView) view.findViewById(R.id.fareAmount);
            rider_name = (TextView) view.findViewById(R.id.rider_name);
            image1 = (CircleImageView) itemView.findViewById(R.id.riderImg);
            img_status = (ImageView) itemView.findViewById(R.id.img_status);


            /*view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (getPosition()==0)
                    {
                    Employee employee = employees.get(0);
                    Integer id = employee.getId();
                       // mContext.startActivity(this,SecondActivity.class);
                        //startActivity(new Intent(mContext,SecondActivity.class));
                        Intent intent = new Intent(mContext, SecondActivity.class);
                        intent.putExtra("id", id);
                        mContext.startActivity(intent);

                       // mContext.startActivity(intent);
                        Toast.makeText(v.getContext(), " On CLick one", Toast.LENGTH_SHORT).show();

                    }
                    if (getPosition()==1)
                    {

                        Toast.makeText(v.getContext(), " On CLick two", Toast.LENGTH_SHORT).show();

                    }
                    if (getPosition()==2)
                    {

                        Toast.makeText(v.getContext(), " On CLick three", Toast.LENGTH_SHORT).show();

                    }
                }
            });*/
        }
    }
}
