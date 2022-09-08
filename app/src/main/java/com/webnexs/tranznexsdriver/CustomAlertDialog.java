package com.webnexs.tranznexsdriver;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Souvik Hazra on 01-07-2017.
 */

public class CustomAlertDialog extends AlertDialog.Builder {

	private TextView dialogTitle, dialogContent;
	private Button leftBtn, rightBtn;
	private View contentView;
	private Activity ctx;
	private AlertDialog dialog;
	private LinearLayout dialogBody;

	public CustomAlertDialog(@NonNull Activity context) {
		super(context);

		ctx = context;
		contentView = context.getLayoutInflater().inflate(R.layout.custom_dialog_button, null);
		dialogTitle = (TextView) contentView.findViewById(R.id.dialogTitle);
		dialogContent = (TextView) contentView.findViewById(R.id.dialogContent);
		leftBtn = (Button) contentView.findViewById(R.id.dialogLeftBtn);
		rightBtn = (Button) contentView.findViewById(R.id.dialogRightBtn);
		dialogBody = contentView.findViewById(R.id.dialogBody);

		leftBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		rightBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		super.setView(contentView);
	}

	@Override
	public CustomAlertDialog setMessage(CharSequence message) {
		dialogContent.setText(message);
		return this;
	}

	@Override
	public CustomAlertDialog setTitle(CharSequence title) {
		dialogTitle.setText(title);
		return this;
	}

	@Override
	public CustomAlertDialog setPositiveButton(CharSequence title, final DialogInterface.OnClickListener listener) {
		rightBtn.setText(title);
		if (listener != null) {
			rightBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					listener.onClick(dialog, 0);
				}
			});
		}
		return this;
	}

	@Override
	public CustomAlertDialog setNegativeButton(CharSequence title, final DialogInterface.OnClickListener listener) {
		leftBtn.setText(title);
		leftBtn.setVisibility(View.VISIBLE);
		if (listener != null) {
			leftBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					listener.onClick(dialog, 0);
				}
			});
		}
		return this;
	}

	public CustomAlertDialog changeButtonFont(float size) {
		leftBtn.setTextSize(size);
		rightBtn.setTextSize(size);
		return this;
	}

	@Override
	public AlertDialog create() {
		dialog = super.create();

		return dialog;
	}

	@Override
	public CustomAlertDialog setView(View v) {
		dialogContent.setVisibility(View.GONE);
		dialogBody.addView(v);
		return this;
	}
}
