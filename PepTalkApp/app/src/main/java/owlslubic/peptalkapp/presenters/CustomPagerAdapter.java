package owlslubic.peptalkapp.presenters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;

/**
 * Created by owlslubic on 9/5/16.
 */
public class CustomPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<PepTalkObject> mPepTalkObjectList;
    private LayoutInflater mInflater;
    private TextView mTextViewTitle, mTextViewBody;

    public CustomPagerAdapter(Context context, List<PepTalkObject> peptalkList) {
        mContext = context;
        mPepTalkObjectList = peptalkList;
        mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mPepTalkObjectList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.fragment_peptalk, container, false);
        mTextViewTitle = (TextView) view.findViewById(R.id.textview_frag_title);
        mTextViewBody = (TextView) view.findViewById(R.id.textview_frag_body);
        if (mTextViewTitle != null && mTextViewBody != null){
            mTextViewTitle.setText(mPepTalkObjectList.get(position).getTitle());
            mTextViewBody.setText(mPepTalkObjectList.get(position).getBody());
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
