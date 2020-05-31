package net.rdrei.android.dirchooser;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.gu.option.Option;
import com.gu.option.UnitFunction;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DirectoryChooserFragment extends DialogFragment {

    public static final String KEY_CURRENT_DIRECTORY = "CURRENT_DIRECTORY";
    private static final String TAG = DirectoryChooserFragment.class.getSimpleName();

    private Option<OnFragmentInteractionListener> mListener = Option.none();

    private Button mBtnConfirm;
    private ImageButton mBtnNavUp;
    private TextView mTxtvSelectedFolder;

    private ArrayAdapter<String> mListDirectoriesAdapter;
    private List<String> mFilenames;
    /**
     * The directory that is currently being shown.
     */
    private File mSelectedDir;
    private File[] mFilesInDir;

    private static void debug(final String message, final Object... args) {
        Log.d(TAG, String.format(message, args));
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mSelectedDir != null) {
            outState.putString(KEY_CURRENT_DIRECTORY, mSelectedDir.getAbsolutePath());
        }
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        assert getActivity() != null;
        final View view = inflater.inflate(R.layout.directory_chooser, container, false);

        mBtnConfirm = view.findViewById(R.id.btnConfirm);
        mBtnNavUp = view.findViewById(R.id.btnNavUp);
        mTxtvSelectedFolder = view.findViewById(R.id.txtvSelectedFolder);

        mBtnConfirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                if (isValidFile(mSelectedDir)) {
                    returnSelectedFolder();
                }
            }
        });

        view.<Button>findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                mListener.foreach(new UnitFunction<OnFragmentInteractionListener>() {
                    @Override
                    public void apply(final OnFragmentInteractionListener listener) {
                        listener.onCancelChooser();
                    }
                });
            }
        });

        ListView mListDirectories = view.findViewById(R.id.directoryList);
        mListDirectories.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, final View view,
                                    final int position, final long id) {
                debug("Selected index: %d", position);
                File file = mFilesInDir[position];

                if (file.isDirectory()) {
                    changeDirectory(file);
                }
            }
        });

        mBtnNavUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                final File parent;
                if (mSelectedDir != null
                        && (parent = mSelectedDir.getParentFile()) != null) {
                    changeDirectory(parent);
                }
            }
        });

        adjustResourceLightness();

        mFilenames = new ArrayList<>();
        mListDirectoriesAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, mFilenames);
        mListDirectories.setAdapter(mListDirectoriesAdapter);

        changeDirectory(Environment.getExternalStorageDirectory());

        return view;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = Option.some((OnFragmentInteractionListener) context);
        } else {
            Fragment owner = getTargetFragment();
            if (owner instanceof OnFragmentInteractionListener) {
                mListener = Option.some((OnFragmentInteractionListener) owner);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void adjustResourceLightness() {
        // change up button to light version if using dark theme
        int color = 0xFFFFFF;
        final Resources.Theme theme = getActivity().getTheme();

        if (theme != null) {
            final TypedArray backgroundAttributes = theme.obtainStyledAttributes(
                    new int[]{android.R.attr.colorBackground}
            );

            color = backgroundAttributes.getColor(0, 0xFFFFFF);
            backgroundAttributes.recycle();
        }

        // convert to greyscale and check if < 128
        if (color != 0xFFFFFF && getConvertedColor(color) < 128) {
            mBtnNavUp.setImageResource(R.drawable.navigation_up_light);
        }
    }

    private double getConvertedColor(int color) {
        return 0.21 * Color.red(color)
                + 0.72 * Color.green(color)
                + 0.07 * Color.blue(color);
    }

    /**
     * Change the directory that is currently being displayed.
     *
     * @param dir The file the activity should switch to. This File must be
     *            non-null and a directory, otherwise the displayed directory
     *            will not be changed
     */
    private void changeDirectory(final File dir) {
        if (dir == null) {
            debug("Could not change folder: dir was null");
        } else if (!dir.isDirectory()) {
            debug("Could not change folder: dir is no directory");
        } else {
            final File[] contents = dir.listFiles();
            if (contents != null) {
                int numFiles = contents.length;
                mFilesInDir = new File[numFiles];
                mFilenames.clear();
                for (int i = 0, counter = 0; i < numFiles; counter++, i++) {
                    mFilesInDir[i] = contents[counter];
                    mFilenames.add(contents[counter].getName());
                }
                Arrays.sort(mFilesInDir, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    }
                });
                Collections.sort(mFilenames, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareToIgnoreCase(o2);
                    }
                });
                mSelectedDir = dir;
                mTxtvSelectedFolder.setText(dir.getAbsolutePath());
                mListDirectoriesAdapter.notifyDataSetChanged();
                debug("Changed directory to %s", dir.getAbsolutePath());
            } else {
                debug("Could not change folder: contents of dir were null");
            }
        }
        refreshButtonState();
    }

    /**
     * Changes the state of the buttons depending on the currently selected file
     * or folder.
     */
    private void refreshButtonState() {
        final Activity activity = getActivity();
        if (activity != null && mSelectedDir != null) {
            mBtnConfirm.setEnabled(isValidFile(mSelectedDir));
            getActivity().invalidateOptionsMenu();
        }
    }

    /**
     * Returns the selected folder as a result to the activity the fragment's attached to. The
     * selected folder can also be null.
     */
    private void returnSelectedFolder() {
        if (mSelectedDir != null) {
            debug("Returning %s as result", mSelectedDir.getAbsolutePath());
            mListener.foreach(new UnitFunction<OnFragmentInteractionListener>() {
                @Override
                public void apply(final OnFragmentInteractionListener f) {
                    f.onSelectDirectory(mSelectedDir.getAbsolutePath());
                }
            });
        } else {
            mListener.foreach(new UnitFunction<OnFragmentInteractionListener>() {
                @Override
                public void apply(final OnFragmentInteractionListener f) {
                    f.onCancelChooser();
                }
            });
        }
    }

    /**
     * Returns true if the selected file or directory would be valid selection.
     */
    private boolean isValidFile(final File file) {
        return (file != null && file.isDirectory() && file.canRead());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        /**
         * Triggered when the user successfully selected their destination directory.
         */
        void onSelectDirectory(String path);

        /**
         * Advices the activity to remove the current fragment.
         */
        void onCancelChooser();
    }

}
