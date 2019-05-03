package cz.erikstoklasa.schoolmarks;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class InfoDialog extends DialogFragment {
    //TODO Make comments
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(this.getString(R.string.about_developer) +"\n"+ this.getString(R.string.about_release_date)+"\n"+this.getString(R.string.about_copyright))
                .setNegativeButton(R.string.ok_dialog, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}
