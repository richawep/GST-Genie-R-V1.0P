
package pos.wepindia.com.retail.GenericClass;

import android.app.AlertDialog.Builder;
import android.content.Context;

import pos.wepindia.com.retail.R;


public class MessageDialog extends Builder {

	Builder dlgMessage;

	public MessageDialog(Context appContext) {
		super(appContext);
		dlgMessage = new Builder(appContext);
	}

	public MessageDialog(Context appContext, int dialogTheme) {
		super(appContext, dialogTheme);
		dlgMessage = new Builder(appContext,dialogTheme);
	}

	public void Show(String Title, String Message){
		dlgMessage
		.setIcon(R.mipmap.ic_company_logo)
		.setTitle(Title)
		.setMessage(Message)
		.setPositiveButton("OK", null)
		.show();
	}
	
}
