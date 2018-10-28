package garg.sarthik.gosafe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ContactAdaptor extends RecyclerView.Adapter<ContactAdaptor.ViewHolder> {

    List<ContactData> contactDataList;
    Context ctx;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_contacts,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final ContactData contactData = contactDataList.get(position);
        holder.tvNumber.setText(contactData.getNumber());
        holder.tvName.setText(contactData.getName());

        final AlertDialog alertDialog_Bookmark = new AlertDialog.Builder(ctx)
                .setTitle("DO YOU WANT TO DELETE THIS NEWS?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ContactApplication.getDB().getContactDAO().deleteContactData(contactData);
                        contactDataList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(ctx, "CONTACT REMOVED", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                alertDialog_Bookmark.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvNumber;
        TextView tvName;
        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvNumber = itemView.findViewById(R.id.tvNumber);
        }
    }
}
