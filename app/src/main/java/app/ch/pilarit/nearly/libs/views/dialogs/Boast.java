package app.ch.pilarit.nearly.libs.views.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

/**
 * {@link android.widget.Toast} decorator allowing for easy cancellation of notifications. Use
 * this class if you want subsequent Toast notifications to overwrite current
 * ones. </p>
 * 
 * By default, a current {@link app.ch.pilarit.nearly.libs.views.dialogs.Boast} notification will be cancelled by a
 * subsequent notification. This default behaviour can be changed by calling
 * certain methods like {@link #show(boolean)}.
 * 
 * 
 * How to use:
 * 	  	Boast.showText(CONTEXT, TEXT);
 * 		Boast.showText(CONTEXT, ResID);
 * You can get context from activity (activity.getApplicationContext())
 * TEXT is a string that you want to show in Toast notifications. 
 * ResID is a integer type. You can show a text in resource id. 
 * You can replace default notifications.
 * 
 * ** Recommenced **
 * You should be use runOnUiThread() for calling Boast notifications. But 'this'
 * must be current activity. 
 * 
 * Example:
 * 	this.runOnUiThread(new Runnable() {
 * 		@Override
 * 		public void run() {
 * 			Boast.showText(this.getApplicationContext(), "Hello!");
 * 		}
 * 	});
 */
public class Boast {
	/**
	 * Keeps track of certain {@link app.ch.pilarit.nearly.libs.views.dialogs.Boast} notifications that may need to be
	 * cancelled. This functionality is only offered by some of the methods in
	 * this class.
	 */
	private volatile static Boast globalBoast = null;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Internal reference to the {@link android.widget.Toast} object that will be displayed.
	 */
	private Toast internalToast;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Private constructor creates a new {@link app.ch.pilarit.nearly.libs.views.dialogs.Boast} from a given
	 * {@link android.widget.Toast}.
	 * 
	 * @throws NullPointerException
	 *             if the parameter is <code>null</code>.
	 */
	private Boast(Toast toast) {
		// null check
		if (toast == null) {
			throw new NullPointerException(
					"Boast.Boast(Toast) requires a non-null parameter.");
		}

		internalToast = toast;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////


	@SuppressLint("ShowToast")
	public static Boast makeText(Context context, CharSequence text,
			int duration) {
		return new Boast(Toast.makeText(context, text, duration));
	}


	@SuppressLint("ShowToast")
	public static Boast makeText(Context context, int resId, int duration)
			throws Resources.NotFoundException {
		return new Boast(Toast.makeText(context, resId, duration));
	}


	@SuppressLint("ShowToast")
	public static Boast makeText(Context context, CharSequence text) {
		return new Boast(Toast.makeText(context, text, Toast.LENGTH_SHORT));
	}


	@SuppressLint("ShowToast")
	public static Boast makeText(Context context, int resId)
			throws Resources.NotFoundException {
		return new Boast(Toast.makeText(context, resId, Toast.LENGTH_SHORT));
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////


	public static void showText(Context context, CharSequence text, int duration) {
		Boast.makeText(context, text, duration).show();
	}


	public static void showText(Context context, int resId, int duration)
			throws Resources.NotFoundException {
		Boast.makeText(context, resId, duration).show();
	}


	public static void showText(Context context, CharSequence text) {
		Boast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	
	public static void showText(Context context, int resId)
			throws Resources.NotFoundException {
		Boast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Close the view if it's showing, or don't show it if it isn't showing yet.
	 * You do not normally have to call this. Normally view will disappear on
	 * its own after the appropriate duration.
	 */
	public void cancel() {
		internalToast.cancel();
	}

	/**
	 * Show the view for the specified duration. By default, this method cancels
	 * any current notification to immediately display the new one. For
	 * conventional {@link android.widget.Toast#show()} queueing behaviour, use method
	 * {@link #show(boolean)}.
	 * 
	 * @see #show(boolean)
	 */
	public void show() {
		show(true);
	}

	/**
	 * Show the view for the specified duration. This method can be used to
	 * cancel the current notification, or to queue up notifications.
	 * 
	 * @param cancelCurrent
	 *            <code>true</code> to cancel any current notification and
	 *            replace it with this new one
	 * 
	 * @see #show()
	 */
	public void show(boolean cancelCurrent) {
		// cancel current
		if (cancelCurrent && (globalBoast != null)) {
			globalBoast.cancel();
		}

		// save an instance of this current notification
		globalBoast = this;

		internalToast.show();
	}

}