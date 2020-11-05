package com.cosmos.photonim.imbase.chat.file;

import android.os.Environment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.cosmos.photonim.imbase.R;
import com.cosmos.photonim.imbase.base.mvp.base.IPresenter;
import com.cosmos.photonim.imbase.chat.file.adapter.FileAdapter;
import com.cosmos.photonim.imbase.chat.file.adapter.FileItemData;
import com.cosmos.photonim.imbase.chat.file.ifile.IFileView;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvBaseAdapter;
import com.cosmos.photonim.imbase.utils.recycleadapter.RvListenerImpl;
import com.cosmos.photonim.imbase.view.ProcessDialogFragment;

import java.util.ArrayList;

public class FileFragment extends IFileView {
    private RecyclerView recyclerView;
    private FileAdapter fileAdapter;
    private ProcessDialogFragment processDialogFragment;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_file;
    }

    @Override
    protected void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    public void progress(boolean show) {
        if (show) {
            if (processDialogFragment == null) {
                processDialogFragment = new ProcessDialogFragment();
            }
            processDialogFragment.show(getFragmentManager(), "");
        } else {
            if (processDialogFragment != null) {
                processDialogFragment.dismiss();
            }
        }
    }

    @Override
    public void notifyData() {
        fileAdapter.notifyDataSetChanged();
    }

    @Override
    public IPresenter getIPresenter() {
        return new FilePresenter(this);
    }

    @Override
    public RecyclerView getRecycleView() {
        return recyclerView;
    }

    @Override
    public RvBaseAdapter getAdapter() {
        if (fileAdapter == null) {
            fileAdapter = new FileAdapter(presenter.init());
            presenter.getFileData(Environment.getExternalStorageDirectory().getAbsolutePath());
            fileAdapter.setRvListener(new RvListenerImpl() {
                @Override
                public void onClick(View view, Object data, int position) {
                    FileItemData file = (FileItemData) data;
                    if (view.getId() == R.id.llContainer && file.isDirectory()) {
                        presenter.getFileData(file.getFilePath());
                    } else if (view.getId() == R.id.cbCheck) {
                        if (!presenter.checkData(file)) {
                            fileAdapter.notifyItemChanged(file.getPosition());
                        }
                    }
                }
            });
        }
        return fileAdapter;
    }

    public boolean popDirectory() {
        if (presenter.getCurrentPath().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            return false;
        }
        return presenter.popFileData();
    }

    public ArrayList<FileItemData> getCheckedData() {
        return presenter.getSelectedFiles();
    }

    public void setOnFileCheckedListener(OnFileCheckedListener onFileCheckedListener) {
        presenter.setOnFileCheckedListener(onFileCheckedListener);
    }

    public interface OnFileCheckedListener {
        void onFileCheckStatusChanged(String size);
    }

}
